package wisoft.labservice.domain.home.entity;

import jakarta.persistence.*;
import lombok.*;
import wisoft.labservice.domain.common.BaseTimeEntity;

@Entity
@Table(name = "gallery_slides")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GallerySlide extends BaseTimeEntity {

    @Id
    @Column(length = 200)
    private String id; // slideId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private LabImage image;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Builder
    public GallerySlide(String id, LabImage image, Integer displayOrder) {
        this.id = id;
        this.image = image;
        this.displayOrder = displayOrder;
    }

    public void updateOrder(Integer order) {
        this.displayOrder = order;
    }
}