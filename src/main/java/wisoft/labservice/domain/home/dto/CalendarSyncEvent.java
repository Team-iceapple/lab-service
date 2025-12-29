package wisoft.labservice.domain.home.dto;

import java.time.ZonedDateTime;

public record CalendarSyncEvent(
        String uid,
        String title,
        boolean allDay,
        ZonedDateTime startAt,
        ZonedDateTime endAt,
        int sequence,
        ZonedDateTime lastModified
) {
}
