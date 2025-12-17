package wisoft.labservice.domain.home.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import wisoft.labservice.domain.home.entity.News;
import wisoft.labservice.domain.project.entity.Project;

@Getter
@Builder
@AllArgsConstructor
public class HomeResponse {
    private LabInfoDto lab;
    private CalendarDto calendar;
    private List<HomeProjectDto> projects;
    private List<HomeNewsDto> news;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LabInfoDto {
        @JsonProperty("image_urls")
        private List<String> imageUrls;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CalendarDto {
        private List<CalendarEventDto> events;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CalendarEventDto {
        private String id;
        private String title;
        private String start;
        private String end;
        private Boolean allDay;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HomeProjectDto {
        private String id;
        private String name;

        public static HomeProjectDto from(Project project) {
            return HomeProjectDto.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HomeNewsDto {
        private String id;
        private String title;
        private String detail;

        public static HomeNewsDto from(News news) {
            return HomeNewsDto.builder()
                    .id(news.getId())
                    .title(news.getTitle())
                    .detail(news.getDetail())
                    .build();
        }
    }

}