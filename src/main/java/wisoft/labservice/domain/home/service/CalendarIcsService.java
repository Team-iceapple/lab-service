package wisoft.labservice.domain.home.service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DateProperty;
import net.fortuna.ical4j.model.property.Sequence;
import org.springframework.stereotype.Service;
import wisoft.labservice.domain.home.component.IcsFetcher;
import wisoft.labservice.domain.home.dto.CalendarSyncEvent;
import wisoft.labservice.domain.home.dto.response.HomeCalendarResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarIcsService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final IcsFetcher fetcher;

    public List<HomeCalendarResponse> loadEvents(String icsUrl) throws Exception {
        var calendar = fetcher.fetch(icsUrl);

        ZonedDateTime now = LocalDate.now(KST).atStartOfDay(KST);
        ZonedDateTime until = now.plusDays(7);

        Map<String, Set<LocalDate>> exceptionDates = new HashMap<>();
        for (Component c : calendar.getComponents(Component.VEVENT)) {
            VEvent v = (VEvent) c;
            String uid = v.getProperty("UID").map(Property::getValue).orElse("");

            v.getProperty("RECURRENCE-ID").ifPresent(p -> {
                if (p instanceof DateProperty<?> dp) {
                    TemporalAccessor t = dp.getDate();
                    LocalDate d = t instanceof LocalDate ld ? ld : toKst(t).toLocalDate();
                    exceptionDates.computeIfAbsent(uid, k -> new HashSet<>()).add(d);
                }
            });

            for (Property p : v.getProperties("EXDATE")) {
                for (String dateStr : p.getValue().split(",")) {
                    try {
                        String cleaned = dateStr.trim().replaceAll("T.*", "");
                        LocalDate d = LocalDate.parse(cleaned, DateTimeFormatter.BASIC_ISO_DATE);
                        exceptionDates.computeIfAbsent(uid, k -> new HashSet<>()).add(d);
                    } catch (Exception ignored) {}
                }
            }
        }

        Map<String, CalendarSyncEvent> byUid = new HashMap<>();

        for (Component c : calendar.getComponents(Component.VEVENT)) {
            VEvent base = (VEvent) c;
            String uid = base.getProperty("UID").map(Property::getValue).orElse("");
            boolean hasRule = !base.getProperties("RRULE").isEmpty();
            boolean hasRecurrenceId = base.getProperty("RECURRENCE-ID").isPresent();

            if (hasRule) {
                Property startP = base.getProperty("DTSTART").orElse(null);
                if (!(startP instanceof DateProperty<?> startProp)) {
                    continue;
                }

                TemporalAccessor dtstart = startProp.getDate();
                boolean isAllDay = dtstart instanceof LocalDate;
                Set<LocalDate> excluded = exceptionDates.getOrDefault(uid, Set.of());

                base.getProperty("RRULE").ifPresent(rruleProp -> {
                    LocalDate seed = isAllDay
                            ? (LocalDate) dtstart
                            : toKst(dtstart).toLocalDate();

                    List<LocalDate> dates = expandRrule(seed, rruleProp.getValue(), now.toLocalDate(),
                            until.toLocalDate());

                    for (LocalDate date : dates) {
                        if (excluded.contains(date)) {
                            continue;
                        }
                        ZonedDateTime instanceStart = isAllDay
                                ? date.atStartOfDay(KST)
                                : date.atTime(toKst(dtstart).toLocalTime()).atZone(KST);
                        CalendarSyncEvent event = convertRecurringInstance(base, instanceStart, isAllDay);
                        if (event == null) {
                            continue;
                        }
                        byUid.merge(event.uid() + "_" + date, event, this::pickLatest);
                    }
                });
            } else if (hasRecurrenceId) {
                CalendarSyncEvent event = convert(base);
                if (event == null || !isWithinNext7Days(event)) {
                    continue;
                }
                byUid.merge(uid + "_exc_" + event.startAt().toLocalDate(), event, this::pickLatest);
            } else {
                CalendarSyncEvent event = convert(base);
                if (event == null || !isWithinNext7Days(event)) {
                    continue;
                }
                byUid.merge(event.uid(), event, this::pickLatest);
            }
        }

        return byUid.values().stream()
                .sorted(Comparator
                        .comparing(CalendarSyncEvent::allDay).reversed()
                        .thenComparing(CalendarSyncEvent::startAt)
                )
                .limit(5)
                .map(this::toResponse)
                .toList();
    }

    private CalendarSyncEvent convertRecurringInstance(VEvent event, ZonedDateTime instanceStart, boolean isAllDay) {
        boolean cancelled = event.getProperty("STATUS")
                .map(Property::getValue)
                .map("CANCELLED"::equalsIgnoreCase)
                .orElse(false);
        if (cancelled) {
            return null;
        }

        boolean excluded = event.getProperty("DESCRIPTION")
                .map(Property::getValue)
                .map(v -> v.contains("제외"))
                .orElse(false);
        if (excluded) {
            return null;
        }

        String id = event.getProperty("UID").map(Property::getValue).orElse("");
        String title = event.getProperty("SUMMARY").map(Property::getValue).orElse("");
        int sequence = event.getSequence().map(Sequence::getSequenceNo).orElse(0);

        ZonedDateTime lastModified = event.getLastModified()
                .map(lm -> lm.getDate().atZone(KST))
                .orElse(null);

        if (isAllDay) {
            LocalDate instanceDate = instanceStart.toLocalDate();
            return new CalendarSyncEvent(id, title, true,
                    instanceDate.atStartOfDay(KST),
                    instanceDate.plusDays(1).atStartOfDay(KST).minusNanos(1),
                    sequence, lastModified);
        }

        return new CalendarSyncEvent(id, title, false, instanceStart, instanceStart, sequence, lastModified);
    }

    private List<LocalDate> expandRrule(LocalDate dtstart, String rruleValue, LocalDate windowStart,
                                        LocalDate windowEnd) {
        List<LocalDate> result = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        for (String part : rruleValue.split(";")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            }
        }

        String freq = params.getOrDefault("FREQ", "");
        int interval = Integer.parseInt(params.getOrDefault("INTERVAL", "1"));

        LocalDate untilDate = null;
        if (params.containsKey("UNTIL")) {
            try {
                untilDate = LocalDate.parse(params.get("UNTIL").replaceAll("[TZ].*", ""),
                        DateTimeFormatter.BASIC_ISO_DATE);
            } catch (Exception ignored) {
            }
        }
        LocalDate effectiveEnd = (untilDate != null && untilDate.isBefore(windowEnd)) ? untilDate : windowEnd;

        Integer count = null;
        if (params.containsKey("COUNT")) {
            try {
                count = Integer.parseInt(params.get("COUNT"));
            } catch (Exception ignored) {
            }
        }

        Map<String, DayOfWeek> dayMap = Map.of(
                "MO", DayOfWeek.MONDAY, "TU", DayOfWeek.TUESDAY,
                "WE", DayOfWeek.WEDNESDAY, "TH", DayOfWeek.THURSDAY,
                "FR", DayOfWeek.FRIDAY, "SA", DayOfWeek.SATURDAY, "SU", DayOfWeek.SUNDAY
        );
        Set<DayOfWeek> byDay = new LinkedHashSet<>();
        if (params.containsKey("BYDAY")) {
            for (String d : params.get("BYDAY").split(",")) {
                DayOfWeek dow = dayMap.get(d.replaceAll("[0-9+\\-]", ""));
                if (dow != null) {
                    byDay.add(dow);
                }
            }
        }
        if (byDay.isEmpty()) {
            byDay.add(dtstart.getDayOfWeek());
        }

        switch (freq) {
            case "DAILY" -> {
                int generated = 0;
                if (count != null) {
                    for (LocalDate d = dtstart; d.isBefore(windowStart) && generated < count;
                         d = d.plusDays(interval)) {
                        generated++;
                    }
                }
                long days = ChronoUnit.DAYS.between(dtstart, windowStart);
                long skip = (days / interval) * interval;
                LocalDate start = dtstart.plusDays(skip);
                if (start.isBefore(windowStart)) {
                    start = start.plusDays(interval);
                }
                for (LocalDate d = start; !d.isAfter(effectiveEnd); d = d.plusDays(interval)) {
                    if (count != null && generated >= count) {
                        break;
                    }
                    result.add(d);
                    generated++;
                }
            }
            case "WEEKLY" -> {
                LocalDate dtstartWeek = dtstart.with(DayOfWeek.MONDAY);
                int generated = 0;
                if (count != null) {
                    for (LocalDate d = dtstart; d.isBefore(windowStart) && generated < count; d = d.plusDays(1)) {
                        if (!byDay.contains(d.getDayOfWeek())) {
                            continue;
                        }
                        long weeks = ChronoUnit.WEEKS.between(dtstartWeek, d.with(DayOfWeek.MONDAY));
                        if (weeks >= 0 && weeks % interval == 0) {
                            generated++;
                        }
                    }
                }
                for (LocalDate d = windowStart; !d.isAfter(effectiveEnd); d = d.plusDays(1)) {
                    if (count != null && generated >= count) {
                        break;
                    }
                    if (d.isBefore(dtstart) || !byDay.contains(d.getDayOfWeek())) {
                        continue;
                    }
                    long weeks = ChronoUnit.WEEKS.between(dtstartWeek, d.with(DayOfWeek.MONDAY));
                    if (weeks >= 0 && weeks % interval == 0) {
                        result.add(d);
                        generated++;
                    }
                }
            }
            case "MONTHLY" -> {
                int generated = 0;
                if (count != null) {
                    for (LocalDate d = dtstart; d.isBefore(windowStart) && generated < count;
                         d = d.plusMonths(interval)) {
                        generated++;
                    }
                }
                long months = ChronoUnit.MONTHS.between(dtstart, windowStart);
                long skip = (months / interval) * interval;
                LocalDate start = dtstart.plusMonths(skip);
                if (start.isBefore(windowStart)) {
                    start = start.plusMonths(interval);
                }
                for (LocalDate d = start; !d.isAfter(effectiveEnd); d = d.plusMonths(interval)) {
                    if (count != null && generated >= count) {
                        break;
                    }
                    result.add(d);
                    generated++;
                }
            }
            case "YEARLY" -> {
                int generated = 0;
                if (count != null) {
                    for (LocalDate d = dtstart; d.isBefore(windowStart) && generated < count;
                         d = d.plusYears(interval)) {
                        generated++;
                    }
                }
                long years = ChronoUnit.YEARS.between(dtstart, windowStart);
                long skip = (years / interval) * interval;
                LocalDate start = dtstart.plusYears(skip);
                if (start.isBefore(windowStart)) {
                    start = start.plusYears(interval);
                }
                for (LocalDate d = start; !d.isAfter(effectiveEnd); d = d.plusYears(interval)) {
                    if (count != null && generated >= count) {
                        break;
                    }
                    result.add(d);
                    generated++;
                }
            }
        }

        return result;
    }

    private CalendarSyncEvent pickLatest(
            CalendarSyncEvent a,
            CalendarSyncEvent b
    ) {
        if (b.sequence() > a.sequence()) {
            return b;
        }
        if (b.sequence() < a.sequence()) {
            return a;
        }

        if (a.lastModified() == null) {
            return b;
        }
        if (b.lastModified() == null) {
            return a;
        }

        return b.lastModified().isAfter(a.lastModified()) ? b : a;
    }

    private HomeCalendarResponse toResponse(CalendarSyncEvent e) {
        return new HomeCalendarResponse(
                e.uid(),
                e.title(),
                e.allDay(),
                e.startAt().toString(),
                e.endAt().toString()
        );
    }

    private ZonedDateTime toKst(TemporalAccessor t) {

        if (t instanceof LocalDate) {
            throw new IllegalArgumentException("LocalDate (all-day) must not be converted to ZonedDateTime");
        }

        if (t instanceof ZonedDateTime zdt) {
            return zdt.withZoneSameInstant(KST);
        }

        if (t instanceof OffsetDateTime odt) {
            return odt.atZoneSameInstant(KST);
        }

        if (t instanceof Instant inst) {
            return inst.atZone(KST);
        }

        if (t instanceof LocalDateTime ldt) {
            return ldt.atZone(ZoneOffset.UTC).withZoneSameInstant(KST);
        }

        throw new IllegalArgumentException(
                "Unsupported DTSTART/DTEND type: " + t.getClass()
        );
    }

    private CalendarSyncEvent convert(VEvent event) {

        boolean cancelled = event.getProperty("STATUS")
                .map(Property::getValue)
                .map("CANCELLED"::equalsIgnoreCase)
                .orElse(false);
        if (cancelled) {
            return null;
        }

        boolean excluded = event.getProperty("DESCRIPTION")
                .map(Property::getValue)
                .map(v -> v.contains("제외"))
                .orElse(false);
        if (excluded) {
            return null;
        }

        String id = event.getProperty("UID")
                .map(Property::getValue)
                .orElse(null);

        String title = event.getProperty("SUMMARY")
                .map(Property::getValue)
                .orElse("");

        Property startP = event.getProperty("DTSTART").orElse(null);
        if (!(startP instanceof DateProperty<?> startProp)) {
            return null;
        }

        Property endP = event.getProperty("DTEND").orElse(null);
        DateProperty endProp = (endP instanceof DateProperty dp) ? dp : null;

        TemporalAccessor start = startProp.getDate();
        TemporalAccessor end = (endProp != null) ? endProp.getDate() : start;

        boolean isAllDay = start instanceof LocalDate;

        int sequence = event.getSequence()
                .map(Sequence::getSequenceNo)
                .orElse(0);

        ZonedDateTime lastModified = event.getLastModified()
                .map(lm -> lm.getDate()
                        .atZone(KST))
                .orElse(null);

        if (isAllDay) {
            LocalDate s = (LocalDate) start;
            LocalDate e = (end instanceof LocalDate ? (LocalDate) end : s).minusDays(1);

            return new CalendarSyncEvent(
                    id,
                    title,
                    true,
                    s.atStartOfDay(KST),
                    e.plusDays(1).atStartOfDay(KST).minusNanos(1),
                    sequence,
                    lastModified
            );
        }

        return new CalendarSyncEvent(
                id,
                title,
                false,
                toKst(start),
                toKst(end),
                sequence,
                lastModified
        );
    }

    private boolean isWithinNext7Days(CalendarSyncEvent e) {
        ZonedDateTime now = LocalDate.now(KST).atStartOfDay(KST);
        ZonedDateTime until = now.plusDays(7);

        return !e.endAt().isBefore(now)
                && !e.startAt().isAfter(until);
    }
}