package wisoft.labservice.domain.paper.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.paper.dto.request.AdminPaperCreateRequest;
import wisoft.labservice.domain.paper.dto.request.AdminPaperUpdateRequest;
import wisoft.labservice.domain.paper.dto.response.AdminPaperDetailResponse;
import wisoft.labservice.domain.paper.dto.response.AdminPaperListResponse;
import wisoft.labservice.domain.paper.service.PaperService;

@RestController
@RequestMapping("/admin/papers")
@RequiredArgsConstructor
public class AdminPaperController {

    private final PaperService paperService;

    @GetMapping
    public ResponseEntity<AdminPaperListResponse> getPapers() {
        AdminPaperListResponse response = paperService.getPapersForAdmin();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paperId}")
    public ResponseEntity<AdminPaperDetailResponse> getPaper(@PathVariable String paperId) {
        AdminPaperDetailResponse response = paperService.getPaperDetailForAdmin(paperId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createPaper(
            @RequestParam String title,
            @RequestParam String authors,
            @RequestParam(name = "paper_abstract") String paperAbstract,
            @RequestParam String conference,
            @RequestParam String journal,
            @RequestParam(name = "publication_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate publicationDate,
            @RequestParam String link,
            @RequestParam Integer year,
            @RequestPart("image_file") MultipartFile imageFile) {
            AdminPaperCreateRequest request = new AdminPaperCreateRequest(title, authors, paperAbstract, conference, journal, publicationDate, link, year, true);

            paperService.createPaper(request, imageFile);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/{paperId}")
    public ResponseEntity<Void> updatePaper(
            @PathVariable String paperId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authors,
            @RequestParam(name = "paper_abstract", required = false) String paperAbstract,
            @RequestParam(required = false) String conference,
            @RequestParam(required = false) String journal,
            @RequestParam(name = "publication_date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate publicationDate,
            @RequestParam(required = false) String link,
            @RequestParam(required = false) Integer year,
            @RequestPart(name = "image_file", required = false) MultipartFile imageFile,
            @RequestParam(name = "is_active", required = false) Boolean isActive ) {


        AdminPaperUpdateRequest request = new AdminPaperUpdateRequest(title, authors, paperAbstract, conference, journal, publicationDate, link, year, isActive);

        paperService.updatePaper(paperId, request, imageFile);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{paperId}")
    public ResponseEntity<Void> deletePaper(@PathVariable String paperId) {
        System.out.println("here");
        paperService.deletePaper(paperId);

        return ResponseEntity.noContent().build();
    }
}
