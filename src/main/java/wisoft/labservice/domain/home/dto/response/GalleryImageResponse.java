package wisoft.labservice.domain.home.dto.response;

import lombok.Builder;
import lombok.Getter;
import wisoft.labservice.domain.home.entity.LabImage;

@Getter
@Builder
public class GalleryImageResponse {

    private String id;
    private String title;
    private String file_url;
    private String type;
    private String created_at;

    public static GalleryImageResponse from(LabImage image) {
        String type = image.getFile().getType().equals("pdf")
                ? "pdf"
                : "img";

        return GalleryImageResponse.builder()
                .id(image.getId())
                .title(image.getTitle())
                .file_url(image.getFile().getFileUrl())
                .type(type)
                .created_at(image.getCreatedAt().toString())
                .build();
    }
}