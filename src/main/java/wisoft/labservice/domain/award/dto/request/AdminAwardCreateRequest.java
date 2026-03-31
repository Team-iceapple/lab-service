package wisoft.labservice.domain.award.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdminAwardCreateRequest(

        @NotBlank(message = "제목은 필수입니다")
        String title,

        @NotBlank(message = "수상자는 필수입니다")
        String awardee,

        @NotBlank(message = "대회명은 필수입니다")
        String competition,

        String summary,

        @NotNull(message = "날짜는 필수입니다")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "연도는 필수입니다")
        Integer year,

        @JsonProperty("is_active")
        Boolean isActive

) {
}