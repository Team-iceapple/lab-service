package wisoft.labservice.domain.patent.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.labservice.domain.patent.entity.Patent;

public record PatentResponse(
        String id,

        Integer year,

        @JsonProperty("pdf_url")
        String imageUrl
) {
    public static PatentResponse from(Patent patent) {
        return new PatentResponse(
                patent.getId(),
                patent.getYear(),
                patent.getPdfFile() != null ? patent.getPdfFile().getFileUrl() : null);
    }
}

