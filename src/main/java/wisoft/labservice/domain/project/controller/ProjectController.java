package wisoft.labservice.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.labservice.domain.project.dto.response.ProjectDetailResponse;
import wisoft.labservice.domain.project.dto.response.ProjectListResponse;
import wisoft.labservice.domain.project.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ProjectListResponse> getProjects() {
        ProjectListResponse response = projectService.getProjectsForUser();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProject(@PathVariable String projectId) {
        ProjectDetailResponse response = projectService.getProjectDetailForUser(projectId);

        return ResponseEntity.ok(response);
    }
}
