package wisoft.labservice.domain.home.dto;

import java.time.ZonedDateTime;

public record CalendarEvent(
        String id,
        String title,
        boolean allDay,
        ZonedDateTime startAt,
        ZonedDateTime endAt
) {}
