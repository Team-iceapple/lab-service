package wisoft.labservice.domain.file.dto;


import lombok.Getter;
import wisoft.labservice.domain.file.entity.FileEntity;

import java.time.LocalDateTime;

@Getter
public class FileResponse {

    private final String id;
    private final String fileUrl;
    private final String type;
    private final LocalDateTime createdAt;

    public FileResponse(FileEntity f) {
        this.id = f.getId();
        this.fileUrl = f.getFileUrl();
        this.type = f.getType();
        this.createdAt = f.getCreatedAt();
    }
}