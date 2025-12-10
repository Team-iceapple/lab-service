package wisoft.labservice.domain.paper.entity;

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
@Table(name = "papers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Paper extends BaseTimeEntity {
    
    @Id
    @Column(length = 200)
    private String id;
    
    @Column(nullable = false, length = 300)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String authors;
    
    @Column(name = "abstract", columnDefinition = "TEXT")
    private String paperAbstract;
    
    @Column(length = 200)
    private String conference;
    
    @Column(length = 200)
    private String journal;
    
    @Column(name = "publication_date")
    private LocalDate publicationDate;
    
    @Column(length = 500)
    private String link;
    
    @Column(nullable = false)
    private Integer year;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_file_id")
    private FileEntity imageFile;
    
    @Builder
    public Paper(String id, String title, String authors, String paperAbstract,
                 String conference, String journal, LocalDate publicationDate,
                 String link, Integer year, FileEntity imageFile) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.paperAbstract = paperAbstract;
        this.conference = conference;
        this.journal = journal;
        this.publicationDate = publicationDate;
        this.link = link;
        this.year = year;
        this.imageFile = imageFile;
    }
    
    public void updateTitle(String title) {
        this.title = title;
    }
    
    public void updateAuthors(String authors) {
        this.authors = authors;
    }
    
    public void updateAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }
    
    public void updateConference(String conference) {
        this.conference = conference;
    }
    
    public void updateJournal(String journal) {
        this.journal = journal;
    }
    
    public void updatePublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public void updateLink(String link) {
        this.link = link;
    }
    
    public void updateYear(Integer year) {
        this.year = year;
    }
    
    public void updateImageFile(FileEntity imageFile) {
        this.imageFile = imageFile;
    }
}
