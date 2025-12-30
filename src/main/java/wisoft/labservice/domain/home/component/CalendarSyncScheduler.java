package wisoft.labservice.domain.home.component;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wisoft.labservice.domain.home.dto.response.HomeCalendarResponse;
import wisoft.labservice.domain.home.service.CalendarIcsService;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class CalendarSyncScheduler {

    private final CalendarIcsService service;
    private final CalendarProperties calendarProperties;

    private volatile List<HomeCalendarResponse> cache = List.of();

    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void sync() {
        try {
            List<HomeCalendarResponse> events = service.loadEvents(calendarProperties.getIcsUrl());

            cache = events;
            log.info(buildLog(events));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HomeCalendarResponse> getCachedEvents() {
        return cache;
    }

    private String buildLog(List<HomeCalendarResponse> events) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n[CalendarSync] Cache Loaded\n")
                .append("- total: ").append(events.size()).append(" events\n")
                .append("--------------------------------------------------\n");

        for (int i = 0; i < events.size(); i++) {
            HomeCalendarResponse e = events.get(i);
            sb.append(String.format(
                    "[%d] %s ~ %s | %s | location=%s%n",
                    i,
                    e.start(),
                    e.end(),
                    e.title(),
                    e.allDay()
            ));
        }
        sb.append("--------------------------------------------------");

        return sb.toString();
    }
}
