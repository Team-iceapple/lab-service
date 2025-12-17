package wisoft.labservice.domain.home.component;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wisoft.labservice.domain.home.dto.response.HomeCalendarResponse;
import wisoft.labservice.domain.home.service.CalendarIcsService;

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
            cache = service.loadEvents(calendarProperties.getIcsUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HomeCalendarResponse> getCachedEvents() {
        return cache;
    }
}
