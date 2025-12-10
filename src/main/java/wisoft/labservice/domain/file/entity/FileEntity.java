package wisoft.labservice.domain.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.labservice.domain.common.BaseTimeEntity;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity extends BaseTimeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id; // f_uuid

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "type", nullable = false, length = 20)
    private String type;    // pdf, img 등

    @Builder
    public FileEntity(String id,
                      String fileUrl,
                      String type) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.type = type;
    }
}