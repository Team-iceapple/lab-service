package wisoft.labservice.domain.paper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.labservice.domain.paper.dto.response.PaperListResponse;
import wisoft.labservice.domain.paper.service.PaperService;

@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;


    @GetMapping
    public ResponseEntity<PaperListResponse> getPapers() {
        PaperListResponse response = paperService.getPapersForUser();

        return ResponseEntity.ok(response);
    }


}
