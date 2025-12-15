package wisoft.labservice.domain.paper.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import wisoft.labservice.domain.paper.entity.Paper;

public record AdminPaperDetailResponse(
        String id,

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

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("image_type")
        String imageType
) {
        public static AdminPaperDetailResponse from(Paper paper) {
                return new AdminPaperDetailResponse(
                        paper.getId(),
                        paper.getTitle(),
                        paper.getAuthors(),
                        paper.getPaperAbstract(),
                        paper.getConference(),
                        paper.getJournal(),
                        paper.getPublicationDate(),
                        paper.getLink(),
                        paper.getYear(),
                        paper.getImageFile() != null ? paper.getImageFile().getFileUrl() : null,
                        paper.getImageFile() != null ? paper.getImageFile().getType() : null
                );
        }
}
