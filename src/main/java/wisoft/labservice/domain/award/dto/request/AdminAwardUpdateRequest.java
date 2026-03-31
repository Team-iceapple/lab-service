package wisoft.labservice.domain.award.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record AdminAwardUpdateRequest(
        String title,

        String awardee,

        String competition,

        String summary,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Integer year,

        @JsonProperty("is_active")
        Boolean isActive
){
}
