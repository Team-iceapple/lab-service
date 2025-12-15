package wisoft.labservice.domain.file.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.file.entity.FileCategory;
import wisoft.labservice.domain.file.entity.FileEntity;
import wisoft.labservice.domain.file.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Value("${app.storage.path}")
    private String storageRootPath;

    private static final List<String> ALLOWED_EXT = List.of("pdf", "png", "jpg", "jpeg");

    public FileEntity upload(MultipartFile file, String categoryRaw) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("INVALID_FILE");
        }

        FileCategory category = FileCategory.from(categoryRaw);

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new IllegalArgumentException("INVALID_FILE");
        }

        String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("INVALID_FILE");
        }

        String id = "f_" + UUID.randomUUID();
        String fileName = id + "." + ext;
        String folder = category.name().toLowerCase(); // home, paper...

        // 실제 저장 경로: {storageRootPath}/files/{category}/f_uuid.ext
        Path dirPath = Paths.get(storageRootPath, "files", folder);
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = dirPath.resolve(fileName).toFile();

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("SERVER_ERROR", e);
        }

        // 클라이언트한테 보여줄 URL (나중에 /media 매핑만 해주면 됨)
        String fileUrl = "/media/files/" + folder + "/" + fileName;

        FileEntity entity = FileEntity.builder()
                .id(id)
                .fileUrl(fileUrl)
                .type(ext)
                .build();

        return fileRepository.save(entity);
    }

    public FileEntity get(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FILE_NOT_FOUND"));
    }

    public void delete(String id) {
        FileEntity file = get(id);

        String fileName = file.getId() + "." + (file.getType().equals("pdf") ? "pdf" : "img");
        // ↑ 여기서 img일 때 실제 확장자까지 관리하고 싶으면 DB에 ext를 따로 저장해도 됨

        Path path = Paths.get(storageRootPath, "files", fileName);
        File f = path.toFile();
        if (f.exists()) {
            f.delete();
        }

        fileRepository.delete(file);
    }
}