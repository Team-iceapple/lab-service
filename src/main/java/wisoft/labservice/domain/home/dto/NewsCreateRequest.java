package wisoft.labservice.domain.home.dto;

import lombok.Getter;

@Getter
public class NewsCreateRequest {

    private String title;      // 필수
    private String detail;     // optional
    private Boolean isActive;  // optional (default = true)
    private Boolean isPinned;  // optional (default = false)
}