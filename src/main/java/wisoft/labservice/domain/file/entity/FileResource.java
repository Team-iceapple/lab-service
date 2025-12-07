package wisoft.labservice.domain.file.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileResource {

    @Id
    @Column(name = "id", nullable = false)
    private String id; // f_uuid

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private FileCategory category;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "type", nullable = false, length = 20)
    private String type;    // pdf, img 등

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public FileResource(String id,
                        String title,
                        FileCategory category,
                        String fileUrl,
                        String type,
                        LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.fileUrl = fileUrl;
        this.type = type;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}