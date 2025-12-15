package wisoft.labservice.domain.home.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SlideOrderUpdateRequest {
    private List<SlideOrder> slides;

    @Getter
    public static class SlideOrder {
        private String id;
        private Integer order;
    }
}