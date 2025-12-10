package wisoft.labservice.domain.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import wisoft.labservice.domain.file.entity.FileEntity;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @Column(length = 200)
    private String id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PROGRESS;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ProjectMember> members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_file_id")
    private FileEntity thumbnailFile;

    @Column(length = 500)
    private String link;

    @Builder
    public Project(String id, String name, Integer year, ProjectStatus status,
                   String description, List<ProjectMember> members,
                   FileEntity thumbnailFile, String link) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.status = status != null ? status : ProjectStatus.PROGRESS;
        this.description = description;
        this.members = members;
        this.thumbnailFile = thumbnailFile;
        this.link = link;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateYear(Integer year) {
        this.year = year;
    }

    public void updateStatus(ProjectStatus status) {
        this.status = status;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateMembers(List<ProjectMember> members) {
        this.members = members;
    }

    public void updateThumbnailFile(FileEntity thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }

    public void updateLink(String link) {
        this.link = link;
    }

    public enum ProjectStatus {
        PROGRESS, DONE
    }
}