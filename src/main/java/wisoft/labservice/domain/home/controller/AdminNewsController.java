package wisoft.labservice.domain.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wisoft.labservice.domain.home.dto.NewsCreateRequest;
import wisoft.labservice.domain.home.dto.NewsResponse;
import wisoft.labservice.domain.home.dto.NewsUpdateRequest;
import wisoft.labservice.domain.home.service.NewsService;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@Tag(name = "Admin Home - News", description = "관리자 홈 뉴스 관리 API")
@RestController
@RequestMapping("/admin/home/news")
@RequiredArgsConstructor
public class AdminNewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 목록 조회", description = "관리자용 뉴스 목록을 조회합니다.")
    @GetMapping
    public List<NewsResponse> getNewsList() {
        return newsService.findAll()
                .stream()
                .map(NewsResponse::from)
                .toList();
    }

    @Operation(summary = "뉴스 등록")
    @PostMapping
    public NewsResponse create(@RequestBody NewsCreateRequest req) {
        return NewsResponse.from(newsService.create(req));
    }

    @Operation(summary = "뉴스 수정")
    @PatchMapping("/{id}")
    public void update(@PathVariable String id,
                       @RequestBody NewsUpdateRequest req) {
        newsService.update(id, req);
    }

    @Operation(summary = "뉴스 삭제")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        newsService.delete(id);
    }
}