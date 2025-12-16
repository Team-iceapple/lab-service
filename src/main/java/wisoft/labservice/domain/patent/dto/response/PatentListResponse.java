package wisoft.labservice.domain.patent.dto.response;

import java.util.List;

public record PatentListResponse(
        List<PatentResponse> patents
) {
}
