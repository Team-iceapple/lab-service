package wisoft.labservice.domain.award.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.labservice.domain.award.entity.Award;
import wisoft.labservice.domain.award.entity.Award.Orientation;

public record AdminAwardResponse(
        String id,

        Integer year,

        String title,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("image_type")
        String imageType,

        Orientation orientation
) {

    public static AdminAwardResponse from(Award award) {
        return new AdminAwardResponse(
                award.getId(),
                award.getYear(),
                award.getTitle(),
                award.getImageFile() != null ? award.getImageFile().getFileUrl() : null,
                award.getImageFile() != null ? award.getImageFile().getType() : null,
                award.getOrientation());
    }
}
