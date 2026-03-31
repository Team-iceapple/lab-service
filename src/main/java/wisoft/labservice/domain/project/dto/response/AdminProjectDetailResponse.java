package wisoft.labservice.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import wisoft.labservice.domain.project.dto.ProjectMemberDto;
import wisoft.labservice.domain.project.entity.Project;

public record AdminProjectDetailResponse(
        String id,
        Integer year,
        String status,
        String name,
        String description,
        List<ProjectMemberDto> members,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        String link,
        @JsonProperty("is_active") Boolean isActive
) {
    public static AdminProjectDetailResponse from(Project project) {
        return new AdminProjectDetailResponse(
                project.getId(),
                project.getYear(),
                project.getStatus().name().toLowerCase(),
                project.getName(),
                project.getDescription(),
                project.getMembers() != null
                        ? project.getMembers().stream().map(ProjectMemberDto::from).toList()
                        : List.of(),
                project.getThumbnailFile() != null ? project.getThumbnailFile().getFileUrl() : null,
                project.getLink(),
                project.getIsActive()
        );
    }
}