package wisoft.labservice.domain.patent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "patents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patent extends BaseTimeEntity {
    
    @Id
    @Column(length = 200)
    private String id;
    
    @Column(nullable = false, length = 300)
    private String name;
    
    @Column(nullable = false)
    private Integer year;

    @Column(length = 300)
    private String inventor;
    
    @Column(name = "invention_date", nullable = false)
    private LocalDate inventionDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_file_id")
    private FileEntity pdfFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_file_id")
    private FileEntity thumbnailFile;
    
    @Column(length = 500)
    private String link;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder
    public Patent(String id, String name, Integer year,
                  String inventor, LocalDate inventionDate,
                  FileEntity pdfFile, FileEntity thumbnailFile,
                  String link, Boolean isActive)
    {
        this.id = id;
        this.name = name;
        this.year = year;
        this.inventor = inventor;
        this.inventionDate = inventionDate;
        this.pdfFile = pdfFile;
        this.thumbnailFile = thumbnailFile;
        this.link = link;
        this.isActive = isActive != null ? isActive : true;
    }
    
    public void updateName(String name) {
        this.name = name;
    }
    
    public void updateYear(Integer year) {
        this.year = year;
    }

    public void updateInventor(String inventor) { this.inventor = inventor; }
    
    public void updateInventionDate(LocalDate inventionDate) {
        this.inventionDate = inventionDate;
    }
    
    public void updatePdfFile(FileEntity pdfFile) {
        this.pdfFile = pdfFile;
    }

    public void updateThumbnailFile(FileEntity thumbnailFile) { this.thumbnailFile = thumbnailFile; }
    
    public void updateLink(String link) {
        this.link = link;
    }

    public void updateIsActive(Boolean isActive) { this.isActive = isActive; }
}