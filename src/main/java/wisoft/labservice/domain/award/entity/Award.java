package wisoft.labservice.domain.award.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.labservice.domain.common.BaseTimeEntity;
import wisoft.labservice.domain.file.entity.FileEntity;

@Entity
@Table(name = "awards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Award extends BaseTimeEntity {

    @Id
    @Column(length = 200)
    private String id;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String awardee;

    @Column(length = 200)
    private String competition;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "award_date")
    private LocalDate awardDate;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    private FileEntity imageFile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Orientation orientation = Orientation.LANDSCAPE;

    @Builder
    public Award(String id, String title, String awardee, String competition,
                 String summary, LocalDate awardDate, Integer year,
                 FileEntity imageFile, Orientation orientation, Integer displayOrder) {
        this.id = id;
        this.title = title;
        this.awardee = awardee;
        this.competition = competition;
        this.summary = summary;
        this.awardDate = awardDate;
        this.year = year;
        this.imageFile = imageFile;
        this.orientation = orientation != null ? orientation : Orientation.LANDSCAPE;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateAwardee(String awardee) {
        this.awardee = awardee;
    }

    public void updateCompetition(String competition) {
        this.competition = competition;
    }

    public void updateSummary(String summary) {
        this.summary = summary;
    }

    public void updateAwardDate(LocalDate awardDate) {
        this.awardDate = awardDate;
    }

    public void updateYear(Integer year) {
        this.year = year;
    }

    public void updateImageFile(FileEntity imageFile) {
        this.imageFile = imageFile;
    }

    public void updateOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public enum Orientation {
        PORTRAIT, LANDSCAPE
    }
}