package wisoft.labservice.domain.home.dto.response;

import lombok.Builder;
import lombok.Getter;
import wisoft.labservice.domain.home.entity.GallerySlide;

@Getter
@Builder
public class GallerySlideResponse {

    private String id;
    private String image_id;
    private String title;
    private String file_url;
    private String type;
    private Integer order;
    private String created_at;

    public static GallerySlideResponse from(GallerySlide slide) {
        String type = slide.getImage().getFile().getType().equals("pdf")
                ? "pdf"
                : "img";

        return GallerySlideResponse.builder()
                .id(slide.getId())
                .image_id(slide.getImage().getId())
                .title(slide.getImage().getTitle())
                .file_url(slide.getImage().getFile().getFileUrl())
                .type(type)
                .order(slide.getDisplayOrder())
                .created_at(slide.getCreatedAt().toString())
                .build();
    }
}