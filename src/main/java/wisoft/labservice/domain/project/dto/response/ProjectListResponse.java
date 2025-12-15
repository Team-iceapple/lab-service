package wisoft.labservice.domain.project.dto.response;

import java.util.List;

public record ProjectListResponse(
          List<ProjectResponse> projects
  ) {
  }
