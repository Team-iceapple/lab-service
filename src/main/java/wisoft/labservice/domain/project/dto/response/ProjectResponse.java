package wisoft.labservice.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import wisoft.labservice.domain.project.dto.ProjectMemberDto;
import wisoft.labservice.domain.project.entity.Project;

public record ProjectResponse(
          String id,

          String name,

          List<ProjectMemberDto> members,

          String thumbnail,

          Integer year
  ) {
      public static ProjectResponse from(Project project)
   {
          return new ProjectResponse(
                  project.getId(),
                  project.getName(),
                  project.getMembers() != null ? project.getMembers().stream().map(ProjectMemberDto::from).collect(Collectors.toList()) : List.of(),
                  project.getThumbnailFile() != null ? project.getThumbnailFile().getFileUrl() : null,
                  project.getYear()
          );
      }
  }