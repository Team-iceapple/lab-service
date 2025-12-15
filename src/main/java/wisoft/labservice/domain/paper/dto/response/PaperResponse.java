package wisoft.labservice.domain.paper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.labservice.domain.paper.entity.Paper;

public record PaperResponse(
        String id,

        Integer year,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("image_type")
        String imageType
) {
    public static PaperResponse from(Paper paper) {
        return new PaperResponse(
                paper.getId(),
                paper.getYear(),
                paper.getImageFile() != null ? paper.getImageFile().getFileUrl() : null,
                paper.getImageFile() != null ? paper.getImageFile().getType() : null);
    }
}
