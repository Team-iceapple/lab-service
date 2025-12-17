package wisoft.labservice.domain.home.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DateProperty;
import org.springframework.stereotype.Service;
import wisoft.labservice.domain.home.component.IcsFetcher;
import wisoft.labservice.domain.home.dto.response.HomeCalendarResponse;

@Service
@RequiredArgsConstructor
public class CalendarIcsService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final IcsFetcher fetcher;

    public List<HomeCalendarResponse> loadEvents(String icsUrl) throws Exception {

        var calendar = fetcher.fetch(icsUrl);

        return calendar.getComponents(Component.VEVENT).stream()
                .map(c -> convert((VEvent) c))
                .filter(e -> e != null)
                .filter(this::isWithinNext7Days)
                .sorted((a, b) -> {
                    if (a.allDay() && !b.allDay()) return -1;
                    if (!a.allDay() && b.allDay()) return 1;
                    return a.start().compareTo(b.start());
                })
                .toList();
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
            // TZ 정보 없는 경우 → UTC 가정
            return ldt.atZone(ZoneOffset.UTC).withZoneSameInstant(KST);
        }

        throw new IllegalArgumentException(
                "Unsupported DTSTART/DTEND type: " + t.getClass()
        );
    }

    private HomeCalendarResponse convert(VEvent event) {

        // STATUS (Optional<Property>)
        boolean cancelled = event.getProperty("STATUS")
                .map(Property::getValue)
                .map(v -> "CANCELLED".equalsIgnoreCase(v))
                .orElse(false);
        if (cancelled) return null;

        // UID / SUMMARY
        String id = event.getProperty("UID")
                .map(Property::getValue)
                .orElse(null);

        String title = event.getProperty("SUMMARY")
                .map(Property::getValue)
                .orElse("");

        // DTSTART / DTEND: Optional<Property> -> DateProperty인지 확인
        Property startP = event.getProperty("DTSTART").orElse(null);
        if (!(startP instanceof DateProperty startProp)) return null;

        Property endP = event.getProperty("DTEND").orElse(null);
        DateProperty endProp = (endP instanceof DateProperty dp) ? dp : null;

        TemporalAccessor start = startProp.getDate();
        TemporalAccessor end = (endProp != null) ? endProp.getDate() : start;

        boolean isAllDay = start instanceof LocalDate;


        if (isAllDay) {
            LocalDate startDate = (LocalDate) start;
            LocalDate endDate = (end instanceof LocalDate ? (LocalDate) end : startDate).minusDays(1);

            return new HomeCalendarResponse(
                    id,
                    title,
                    true,
                    startDate.toString(),  // yyyy-MM-dd
                    endDate.toString()
            );
        }

        // 시간 일정: UTC/로컬 -> KST
        ZonedDateTime startAt = toKst(start);
        ZonedDateTime endAt = toKst(end);

        return new HomeCalendarResponse(
                id,
                title,
                false,
                startAt.toString(),      // ISO-8601 with +09:00
                endAt.toString()
        );
    }

    private boolean isWithinNext7Days(HomeCalendarResponse e) {

        ZonedDateTime now = ZonedDateTime.now(KST);
        ZonedDateTime until = now.plusDays(7).toLocalDate()
                .plusDays(1)
                .atStartOfDay(KST)
                .minusNanos(1);

        if (e.allDay()) {
            LocalDate start = LocalDate.parse(e.start());
            LocalDate end = LocalDate.parse(e.end());

            ZonedDateTime startAt = start.atStartOfDay(KST);
            ZonedDateTime endAt = end.plusDays(1).atStartOfDay(KST).minusNanos(1);

            return !endAt.isBefore(now) && !startAt.isAfter(until);
        }

        ZonedDateTime startAt = ZonedDateTime.parse(e.start());
        ZonedDateTime endAt = ZonedDateTime.parse(e.end());

        return !endAt.isBefore(now) && !startAt.isAfter(until);
    }
}
