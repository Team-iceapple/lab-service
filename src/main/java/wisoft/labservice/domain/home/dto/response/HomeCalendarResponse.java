package wisoft.labservice.domain.home.dto.response;

public record HomeCalendarResponse(
        String id,
        String title,
        boolean allDay,
        String start,
        String end
) {}