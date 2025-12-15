package wisoft.labservice.domain.project.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.file.entity.FileEntity;
import wisoft.labservice.domain.file.service.FileService;
import wisoft.labservice.domain.project.dto.ProjectMemberDto;
import wisoft.labservice.domain.project.dto.request.AdminProjectCreateRequest;
import wisoft.labservice.domain.project.dto.request.AdminProjectUpdateRequest;
import wisoft.labservice.domain.project.dto.response.AdminProjectDetailResponse;
import wisoft.labservice.domain.project.dto.response.AdminProjectListResponse;
import wisoft.labservice.domain.project.dto.response.AdminProjectResponse;
import wisoft.labservice.domain.project.dto.response.ProjectDetailResponse;
import wisoft.labservice.domain.project.dto.response.ProjectListResponse;
import wisoft.labservice.domain.project.dto.response.ProjectResponse;
import wisoft.labservice.domain.project.entity.Project;
import wisoft.labservice.domain.project.entity.Project.ProjectStatus;
import wisoft.labservice.domain.project.entity.ProjectMember;
import wisoft.labservice.domain.project.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FileService fileService;

    /**
     * 프로젝트 목록 조회 (사용자용)
     */
    public ProjectListResponse getProjectsForUser() {
        List<Project> projects = projectRepository.findAllWithThumbnailFile();

        List<ProjectResponse> projectResponses = projects.stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());

        return new ProjectListResponse(projectResponses);
    }

    /**
     * 프로젝트 상세 조회 (사용자용)
     */
    public ProjectDetailResponse getProjectDetailForUser(String projectId) {
        Project project = projectRepository.findByIdWithThumbnailFile(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        return ProjectDetailResponse.from(project);
    }

    /**
     * 프로젝트 목록 조회 (관리자용)
     */
    public AdminProjectListResponse getProjectsForAdmin() {
        List<Project> projects = projectRepository.findAllWithThumbnailFile();

        List<AdminProjectResponse> projectResponses = projects.stream()
                .map(AdminProjectResponse::from)
                .collect(Collectors.toList());

        return new AdminProjectListResponse(projectResponses);
    }

    /**
     * 프로젝트 상세 조회 (관리자용)
     */
    public AdminProjectDetailResponse getProjectDetailForAdmin(String projectId) {
        Project project = projectRepository.findByIdWithThumbnailFile(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        return AdminProjectDetailResponse.from(project);
    }

    /**
     * 프로젝트 생성 (관리자용)
     */
    @Transactional
    public String createProject(AdminProjectCreateRequest request, MultipartFile thumbnail) {
        FileEntity uploadedFile = fileService.upload(thumbnail, "PROJECT");

        List<ProjectMember> members = request.members() != null
                ? request.members().stream()
                .map(ProjectMemberDto::toEntity)
                .collect(Collectors.toList()) : List.of();

        ProjectStatus status = ProjectStatus.valueOf(request.status().toUpperCase());

        Project project = Project.builder()
                .id("pr_" + UUID.randomUUID())
                .year(request.year())
                .status(status)
                .name(request.name())
                .description(request.description())
                .members(members)
                .thumbnailFile(uploadedFile)
                .link(request.link())
                .build();

        Project saveProject = projectRepository.save(project);
        return saveProject.getId();
    }

    /**
     * 프로젝트 수정 (관리자용)
     */
    @Transactional
    public void updateProject(String projectId, AdminProjectUpdateRequest request, MultipartFile thumbnail) {
        Project project = projectRepository.findByIdWithThumbnailFile(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        if (request.year() != null) {
            project.updateYear(request.year());
        }
        if (request.status() != null) {
            ProjectStatus status = ProjectStatus.valueOf(request.status().toUpperCase());
            project.updateStatus(status);
        }
        if (request.name() != null) {
            project.updateName(request.name());
        }
        if (request.description() != null) {
            project.updateDescription(request.description());
        }
        if (request.members() != null) {
            List<ProjectMember> members = request.members().stream()
                    .map(ProjectMemberDto::toEntity)
                    .collect(Collectors.toList());
            project.updateMembers(members);
        }
        if (request.link() != null) {
            project.updateLink(request.link());
        }

        if (thumbnail != null && !thumbnail.isEmpty()) {
            if (project.getThumbnailFile() != null) {
                fileService.delete(project.getThumbnailFile().getId());
            }

            FileEntity newFile = fileService.upload(thumbnail, "PROJECT");
            project.updateThumbnailFile(newFile);
        }

        projectRepository.save(project);
    }

    /**
     * 프로젝트 삭제 (관리자용)
     */
    @Transactional
    public void deleteProject(String projectId) {
        Project project = projectRepository.findByIdWithThumbnailFile(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        String fileId = null;
        if (project.getThumbnailFile() != null) {
            fileId = project.getThumbnailFile().getId();
            project.updateThumbnailFile(null);
        }

        projectRepository.delete(project);

        if (fileId != null) {
            fileService.delete(fileId);
        }
    }
}