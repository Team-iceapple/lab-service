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
    
    @Column(name = "invention_date", nullable = false)
    private LocalDate inventionDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_file_id")
    private FileEntity pdfFile;
    
    @Column(length = 500)
    private String link;
    
    @Builder
    public Patent(String id, String name, Integer year, LocalDate inventionDate,
                  FileEntity pdfFile, String link) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.inventionDate = inventionDate;
        this.pdfFile = pdfFile;
        this.link = link;
    }
    
    public void updateName(String name) {
        this.name = name;
    }
    
    public void updateYear(Integer year) {
        this.year = year;
    }
    
    public void updateInventionDate(LocalDate inventionDate) {
        this.inventionDate = inventionDate;
    }
    
    public void updatePdfFile(FileEntity pdfFile) {
        this.pdfFile = pdfFile;
    }
    
    public void updateLink(String link) {
        this.link = link;
    }
}