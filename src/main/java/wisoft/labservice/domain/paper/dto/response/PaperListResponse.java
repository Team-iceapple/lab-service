package wisoft.labservice.domain.paper.dto.response;

import java.util.List;

public record PaperListResponse(
        List<PaperResponse> papers
) {
}
