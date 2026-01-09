package wisoft.labservice.domain.paper.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record AdminPaperCreateRequest(
        String title,

        String authors,

        @JsonProperty("paper_abstract")
        String paperAbstract,

        String conference,

        String journal,

        @JsonProperty("publication_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate publicationDate,

        String link,

        Integer year,

        @JsonProperty("is_active")
        Boolean isActive
) {
}
