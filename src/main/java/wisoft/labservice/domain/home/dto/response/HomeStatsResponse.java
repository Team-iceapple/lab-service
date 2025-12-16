package wisoft.labservice.domain.home.dto.response;

public record HomeStatsResponse(
        long projectCount,
        long paperCount
//        long awardCount,
//        long patentCount
) {}