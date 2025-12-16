package wisoft.labservice.domain.award.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import wisoft.labservice.domain.award.entity.Award;
import wisoft.labservice.domain.award.entity.Award.Orientation;

public record AdminAwardDetailResponse(
        String id,

        String title,

        String awardee,

        String competition,

        String summary,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Integer year,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("image_type")
        String imageType,

        Orientation orientation
) {

    public static AdminAwardDetailResponse from(Award award) {
        return new AdminAwardDetailResponse(
                award.getId(),
                award.getTitle(),
                award.getAwardee(),
                award.getCompetition(),
                award.getSummary(),
                award.getAwardDate(),
                award.getYear(),
                award.getImageFile() != null ? award.getImageFile().getFileUrl() : null,
                award.getImageFile() != null ? award.getImageFile().getType() : null,
                award.getOrientation()
        );
    }
}