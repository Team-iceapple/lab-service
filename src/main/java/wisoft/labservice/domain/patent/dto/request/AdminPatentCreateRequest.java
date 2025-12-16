package wisoft.labservice.domain.patent.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record AdminPatentCreateRequest(
        String name,

        Integer year,

        @JsonProperty("invention_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate inventionDate,

        String link
) {
}
