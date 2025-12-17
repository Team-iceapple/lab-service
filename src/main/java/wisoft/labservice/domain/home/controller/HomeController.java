package wisoft.labservice.domain.home.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.labservice.domain.home.component.CalendarSyncScheduler;
import wisoft.labservice.domain.home.dto.response.HomeCalendarResponse;
import wisoft.labservice.domain.home.dto.response.HomeStatsResponse;
import wisoft.labservice.domain.home.service.HomeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/home")
public class HomeController {

    private final HomeService homeService;
    private final CalendarSyncScheduler scheduler;

    @GetMapping("/stats")
    public HomeStatsResponse getHomeStats() {
        return homeService.getHomeStats();
    }

    @GetMapping("/calendar")
    public List<HomeCalendarResponse> getEvents() {
        return scheduler.getCachedEvents();
    }

}