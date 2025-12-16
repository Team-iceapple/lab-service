package wisoft.labservice.domain.award.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.labservice.domain.award.dto.response.AwardListResponse;
import wisoft.labservice.domain.award.service.AwardService;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping
    public ResponseEntity<AwardListResponse> getAwards() {
        AwardListResponse response = awardService.getAwardsForUser();

        return ResponseEntity.ok(response);
    }
}

