package wisoft.labservice.domain.home.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.labservice.domain.common.BaseTimeEntity;
import wisoft.labservice.domain.file.entity.FileEntity;


@Entity
@Table(name = "lab_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LabImage extends BaseTimeEntity {

    @Id
    @Column(length = 200)
    private String id; // imageId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity file;

    @Column(length = 255)
    private String title;

    @Builder
    public LabImage(String id, FileEntity file, String title) {
        this.id = id;
        this.file = file;
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}