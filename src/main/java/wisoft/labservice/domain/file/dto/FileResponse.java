package wisoft.labservice.domain.file.dto;


import lombok.Getter;
import wisoft.labservice.domain.file.entity.FileResource;

import java.time.LocalDateTime;

@Getter
public class FileResponse {

    private final String id;
    private final String title;
    private final String category;
    private final String fileUrl;
    private final String type;
    private final LocalDateTime createdAt;

    public FileResponse(FileResource f) {
        this.id = f.getId();
        this.title = f.getTitle();
        this.category = f.getCategory().name().toLowerCase();
        this.fileUrl = f.getFileUrl();
        this.type = f.getType();
        this.createdAt = f.getCreatedAt();
    }
}