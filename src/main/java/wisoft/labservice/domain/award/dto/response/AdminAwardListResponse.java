package wisoft.labservice.domain.award.dto.response;

import java.util.List;

public record AdminAwardListResponse(
        List<AdminAwardResponse> awards
) {
}
