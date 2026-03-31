
package wisoft.labservice.domain.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.project.dto.ProjectMemberDto;
import wisoft.labservice.domain.project.dto.request.AdminProjectCreateRequest;
import wisoft.labservice.domain.project.dto.request.AdminProjectUpdateRequest;
import wisoft.labservice.domain.project.dto.response.AdminProjectDetailResponse;
import wisoft.labservice.domain.project.dto.response.AdminProjectListResponse;
import wisoft.labservice.domain.project.service.ProjectService;

@RestController
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectService projectService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<AdminProjectListResponse> getProjects() {
        AdminProjectListResponse response = projectService.getProjectsForAdmin();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<AdminProjectDetailResponse> getProject(@PathVariable String projectId) {
        AdminProjectDetailResponse response = projectService.getProjectDetailForAdmin(projectId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createProject(
            @RequestParam Integer year,
            @RequestParam String status,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String members,
            @RequestParam String link,
            @RequestPart("thumbnail") MultipartFile thumbnail) throws JsonProcessingException {

        List<ProjectMemberDto> memberList = objectMapper.readValue(members, new TypeReference<>() {});

        AdminProjectCreateRequest request = new AdminProjectCreateRequest(year, status, name, description, memberList, link, true);

        String response = projectService.createProject(request, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @PathVariable String projectId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String members,
            @RequestParam(required = false) String link,
            @RequestParam(name = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(name = "is_active", required = false) Boolean isActive ) throws
            JsonProcessingException {

        List<ProjectMemberDto> memberList = null;
        if (members != null) {
            memberList = objectMapper.readValue(members, new TypeReference<List<ProjectMemberDto>>() {});
        }

        AdminProjectUpdateRequest request = new AdminProjectUpdateRequest(year, status, name, description, memberList, link, isActive);

        projectService.updateProject(projectId, request, thumbnail);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}