package wisoft.labservice.domain.project.dto.request;

import java.util.List;
import wisoft.labservice.domain.project.dto.ProjectMemberDto;

public record AdminProjectCreateRequest(
          Integer year,

          String status,

          String name,

          String description,

          List<ProjectMemberDto> members,

          String link
  ) {
  }