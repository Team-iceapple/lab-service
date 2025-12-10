package wisoft.labservice.domain.home.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {
    
    @Id
    @Column(length = 200)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String detail;
    
    @Column(name = "is_pinned")
    private Boolean isPinned = false;
    
    @Builder
    public News(String id, String title, String detail, Boolean isPinned) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.isPinned = isPinned != null ? isPinned : false;
    }
    
    public void updateTitle(String title) {
        this.title = title;
    }
    
    public void updateDetail(String detail) {
        this.detail = detail;
    }
    
    public void togglePin() {
        this.isPinned = !this.isPinned;
    }
    
    public void setPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }
}