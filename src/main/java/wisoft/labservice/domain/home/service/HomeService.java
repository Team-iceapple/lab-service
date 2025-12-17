package wisoft.labservice.domain.home.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wisoft.labservice.domain.award.repository.AwardRepository;
import wisoft.labservice.domain.home.dto.response.HomeResponse;
import wisoft.labservice.domain.home.dto.response.HomeResponse.CalendarDto;
import wisoft.labservice.domain.home.dto.response.HomeResponse.CalendarEventDto;
import wisoft.labservice.domain.home.dto.response.HomeResponse.HomeNewsDto;
import wisoft.labservice.domain.home.dto.response.HomeResponse.HomeProjectDto;
import wisoft.labservice.domain.home.dto.response.HomeResponse.LabInfoDto;
import wisoft.labservice.domain.home.dto.response.HomeStatsResponse;
import wisoft.labservice.domain.home.repository.NewsRepository;
import wisoft.labservice.domain.paper.repository.PaperRepository;
import wisoft.labservice.domain.patent.repository.PatentRepository;
import wisoft.labservice.domain.project.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ProjectRepository projectRepository;
    private final PaperRepository paperRepository;
    private final AwardRepository awardRepository;
    private final PatentRepository patentRepository;
    private final NewsRepository newsRepository;
    private final GalleryService galleryService;

    public HomeStatsResponse getHomeStats() {
        return new HomeStatsResponse(
                projectRepository.count(),
                paperRepository.count(),
                awardRepository.count(),
                patentRepository.count()
        );
    }

    public HomeResponse getHome(){
        //lab 정보
        List<String> imageUrls = galleryService.getSlides().stream()
                .map(slide -> slide.getFile_url())
                .collect(Collectors.toList());

        LabInfoDto labInfo = LabInfoDto.builder()
                .imageUrls(imageUrls)
                .build();


        //calendar
        List<CalendarEventDto> calendarEvents = List.of(
                CalendarEventDto.builder()
                        .id("2")
                        .title("프로젝트 발표")
                        .start("2025-12-15T10:00:00+09:00")
                        .end("2025-12-15T12:00:00+09:00")
                        .allDay(false)
                        .build()
        );

        CalendarDto calendar = CalendarDto.builder()
                .events(calendarEvents)
                .build();

        //Project
        List<HomeProjectDto> projects = projectRepository.findAll().stream()
                .limit(5)
                .map(HomeProjectDto::from)
                .collect(Collectors.toList());


        //news
        List<HomeNewsDto> news = newsRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(n -> n.getIsActive())
                .limit(5)
                .map(HomeNewsDto::from)
                .collect(Collectors.toList());

        return HomeResponse.builder()
                .lab(labInfo)
                .calendar(calendar)
                .projects(projects)
                .news(news)
                .build();

    }
}