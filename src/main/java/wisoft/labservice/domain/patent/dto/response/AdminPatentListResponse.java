package wisoft.labservice.domain.patent.dto.response;

import java.util.List;

public record AdminPatentListResponse(
        List<AdminPatentResponse> patents
) {
}
