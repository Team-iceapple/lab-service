package wisoft.labservice.domain.paper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.labservice.domain.paper.entity.Paper;

public record AdminPaperResponse(
        String id,

        Integer year,

        String title,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("image_type")
        String imageType
) {
    public static AdminPaperResponse from(Paper paper) {
        return new AdminPaperResponse(
                paper.getId(),
                paper.getYear(),
                paper.getTitle(),
                paper.getImageFile() != null ? paper.getImageFile().getFileUrl() : null,
                paper.getImageFile() != null ? paper.getImageFile().getType() : null);
    }
}
