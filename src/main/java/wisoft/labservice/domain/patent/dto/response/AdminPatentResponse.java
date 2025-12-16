package wisoft.labservice.domain.patent.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import wisoft.labservice.domain.patent.entity.Patent;

public record AdminPatentResponse(
        String id,

        String name,

        Integer year,

        @JsonProperty("invention_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate inventionDate,

        String link,

        @JsonProperty("pdf_url")
        String pdfUrl
) {
        public static AdminPatentResponse from(Patent patent) {
                return new AdminPatentResponse(
                        patent.getId(),
                        patent.getName(),
                        patent.getYear(),
                        patent.getInventionDate(),
                        patent.getLink(),
                        patent.getPdfFile() != null ? patent.getPdfFile().getFileUrl() : null
                );
        }
}
