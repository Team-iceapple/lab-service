package wisoft.labservice.domain.patent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.labservice.domain.patent.dto.response.PatentListResponse;
import wisoft.labservice.domain.patent.service.PatentService;

@RestController
@RequestMapping("/api/patents")
@RequiredArgsConstructor
public class PatentController {

    private final PatentService patentService;

    @GetMapping
    public ResponseEntity<PatentListResponse> getPatents() {
        PatentListResponse response = patentService.getPatentsForUser();

        return ResponseEntity.ok(response);
    }


}
