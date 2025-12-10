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
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity file;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Builder
    public LabImage(String id, FileEntity file, String title,
                    String description, Integer displayOrder) {
        this.id = id;
        this.file = file;
        this.title = title;
        this.description = description;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void updateFile(FileEntity file) {
        this.file = file;
    }
}