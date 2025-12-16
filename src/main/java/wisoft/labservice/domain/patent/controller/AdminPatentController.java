package wisoft.labservice.domain.patent.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
import wisoft.labservice.domain.patent.dto.request.AdminPatentCreateRequest;
import wisoft.labservice.domain.patent.dto.request.AdminPatentUpdateRequest;
import wisoft.labservice.domain.patent.dto.response.AdminPatentListResponse;
import wisoft.labservice.domain.patent.dto.response.AdminPatentResponse;
import wisoft.labservice.domain.patent.service.PatentService;

@RestController
@RequestMapping("/admin/patents")
@RequiredArgsConstructor
public class AdminPatentController {

    private final PatentService patentService;

    @GetMapping
    public ResponseEntity<AdminPatentListResponse> getPatents() {
        AdminPatentListResponse response = patentService.getPatentsForAdmin();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{patentId}")
    public ResponseEntity<AdminPatentResponse> getPatent(@PathVariable String patentId) {
        AdminPatentResponse response = patentService.getPatentDetailForAdmin(patentId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createPatent(
            @RequestParam String name,
            @RequestParam Integer year,
            @RequestParam(name = "invention_date")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate inventionDate,
            @RequestPart("pdf_file") MultipartFile pdfFile,
            @RequestParam String link)
    {
        AdminPatentCreateRequest request = new AdminPatentCreateRequest(name, year, inventionDate, link);
        patentService.createPatent(request, pdfFile);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/{patentId}")
    public ResponseEntity<Void> updatePatent(
            @PathVariable String patentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @RequestParam(name = "invention_date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate inventionDate,
            @RequestPart(name = "pdf_file", required = false) MultipartFile pdfFile,
            @RequestParam(required = false) String link)
    {
        AdminPatentUpdateRequest request = new AdminPatentUpdateRequest(name, year, inventionDate, link);
        patentService.updatePatent(patentId, request, pdfFile);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{patentId}")
    public ResponseEntity<Void> deletePatent(@PathVariable String patentId) {
        patentService.deletePatent(patentId);

        return ResponseEntity.noContent().build();
    }
}
