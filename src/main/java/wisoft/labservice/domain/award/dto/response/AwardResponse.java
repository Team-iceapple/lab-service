package wisoft.labservice.domain.award.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.labservice.domain.award.entity.Award;
import wisoft.labservice.domain.award.entity.Award.Orientation;

public record AwardResponse(
        String id,

        Integer year,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("image_type")
        String imageType,

        Orientation orientation
) {
    public static AwardResponse from(Award award) {
        return new AwardResponse(
                award.getId(),
                award.getYear(),
                award.getImageFile() != null ? award.getImageFile().getFileUrl() : null,
                award.getImageFile() != null ? award.getImageFile().getType() : null,
                award.getOrientation());
    }
}
