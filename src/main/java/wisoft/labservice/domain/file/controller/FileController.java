package wisoft.labservice.domain.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.file.dto.FileResponse;
import wisoft.labservice.domain.file.entity.FileResource;
import wisoft.labservice.domain.file.service.FileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
@Tag(name = "File API", description = "파일 업로드 / 조회 / 삭제 API")
public class FileController {

    private final FileService fileService;

    @Operation(
            summary = "파일 업로드",
            description = "multipart/form-data 로 파일을 업로드합니다. category(home, project, paper, award, seminar, patent) 필요"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> upload(
            @Parameter(description = "업로드할 파일 (jpg, png, pdf 등)", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "파일의 카테고리(home, project, paper, award, seminar, patent)", required = true)
            @RequestParam("category") String category,

            @Parameter(description = "파일의 제목 (옵션)", required = false)
            @RequestParam(value = "title", required = false) String title
    ) {
        FileResource saved = fileService.upload(file, title, category);
        return ResponseEntity.ok(new FileResponse(saved));
    }

    @Operation(summary = "파일 메타데이터 조회", description = "파일 ID로 파일 정보를 조회합니다.")
    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFile(
            @Parameter(description = "파일 ID", example = "f_1234-uuid")
            @PathVariable String fileId
    ) {
        FileResource file = fileService.get(fileId);
        return ResponseEntity.ok(new FileResponse(file));
    }

    @Operation(summary = "파일 삭제", description = "파일을 실제 스토리지에서 삭제하고 DB에서도 삭제합니다.")
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            @Parameter(description = "삭제할 파일 ID", example = "f_1234-uuid")
            @PathVariable String fileId
    ) {
        fileService.delete(fileId);
        return ResponseEntity.ok(
                Map.of(
                        "id", fileId,
                        "message", "파일이 삭제되었습니다.",
                        "deletedAt", LocalDateTime.now().toString()
                )
        );
    }
}