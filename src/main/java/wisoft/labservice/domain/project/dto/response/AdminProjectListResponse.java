package wisoft.labservice.domain.project.dto.response;

import java.util.List;

public record AdminProjectListResponse(
          List<AdminProjectResponse> projects
  ) {
  }