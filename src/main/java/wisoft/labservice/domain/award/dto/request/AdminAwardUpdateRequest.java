package wisoft.labservice.domain.award.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AdminAwardUpdateRequest(
        String title,

        String awardee,

        String competition,

        String summary,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Integer year
){
}
