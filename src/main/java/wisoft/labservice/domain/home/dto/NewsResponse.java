package wisoft.labservice.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wisoft.labservice.domain.home.entity.News;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewsResponse {

    private String id;
    private String title;
    private String detail;
    private Boolean isActive;
    private Boolean isPinned;
    private LocalDateTime createdAt;

    public static NewsResponse from(News news) {
        return new NewsResponse(
                news.getId(),
                news.getTitle(),
                news.getDetail(),
                news.getIsActive(),
                news.getIsPinned(),
                news.getCreatedAt()
        );
    }
}