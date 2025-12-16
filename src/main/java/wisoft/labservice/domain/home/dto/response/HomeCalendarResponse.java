package wisoft.labservice.domain.home.dto.response;

public record HomeCalendarResponse(
        String id,
        String title,
        String start,
        String end,
        boolean allDay
) {}