package wisoft.labservice.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import wisoft.labservice.domain.project.dto.ProjectMemberDto;
import wisoft.labservice.domain.project.entity.Project;

public record ProjectDetailResponse(
          String id,

          String name,

          @JsonProperty("team_name")
          String teamName,

          List<ProjectMemberDto> members,

          @JsonProperty("pdf_url")
          String pdfUrl,

          String description,

          @JsonProperty("main_url")
          String mainUrl,

          Integer year
  ) {
      public static ProjectDetailResponse from(Project project) {
          return new ProjectDetailResponse(
                  project.getId(),
                  project.getName(),
                  null,
                  project.getMembers() != null ? project.getMembers().stream().map(ProjectMemberDto::from).collect(Collectors.toList()) : List.of(),
                  null,
                  project.getDescription(),
                  project.getLink(),
                  project.getYear()
          );
      }
  }