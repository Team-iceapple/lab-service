package wisoft.labservice.domain.award.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.award.dto.request.AdminAwardCreateRequest;
import wisoft.labservice.domain.award.dto.request.AdminAwardUpdateRequest;
import wisoft.labservice.domain.award.dto.response.AdminAwardDetailResponse;
import wisoft.labservice.domain.award.dto.response.AdminAwardListResponse;
import wisoft.labservice.domain.award.service.AwardService;

@RestController
@RequestMapping("/admin/awards")
@RequiredArgsConstructor
public class AdminAwardController {

    private final AwardService awardService;

    @GetMapping
    public ResponseEntity<AdminAwardListResponse> getAwards() {
        AdminAwardListResponse response = awardService.getAwardsForAdmin();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{awardId}")
    public ResponseEntity<AdminAwardDetailResponse> getAward(@PathVariable String awardId) {
        AdminAwardDetailResponse response = awardService.getAwardDetailForAdmin(awardId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createAward(
            @Valid @ModelAttribute AdminAwardCreateRequest request,
            @RequestPart("image_file") MultipartFile imageFile) {
        System.out.println(">>> createAward called");
        awardService.createAward(request, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/{awardId}")
    public ResponseEntity<Void> updateAward(
            @PathVariable String awardId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String awardee,
            @RequestParam(required = false) String competition,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date,
            @RequestParam(required = false) Integer year,
            @RequestPart(name = "image_file", required = false) MultipartFile imageFile) {


        AdminAwardUpdateRequest request = new AdminAwardUpdateRequest(title, awardee, competition, summary, date, year);

        awardService.updateAward(awardId, request, imageFile);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable String awardId) {
        awardService.deleteAward(awardId);

        return ResponseEntity.noContent().build();
    }
}
