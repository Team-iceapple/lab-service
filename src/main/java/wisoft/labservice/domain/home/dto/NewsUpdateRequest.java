package wisoft.labservice.domain.home.dto;

import lombok.Getter;

@Getter
public class NewsUpdateRequest {

    private String title;
    private String detail;
    private Boolean isActive;
    private Boolean isPinned;
}