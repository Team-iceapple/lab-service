package wisoft.labservice.domain.paper.dto.response;

import java.util.List;

public record AdminPaperListResponse(
        List<AdminPaperResponse> papers
) {
}
