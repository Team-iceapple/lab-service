package wisoft.labservice.domain.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.labservice.domain.home.dto.response.HomeResponse;
import wisoft.labservice.domain.home.service.HomeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public HomeResponse getHome() {
        return homeService.getHome();
    }
}
