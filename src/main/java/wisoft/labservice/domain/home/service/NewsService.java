package wisoft.labservice.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.labservice.domain.home.dto.request.NewsCreateRequest;
import wisoft.labservice.domain.home.dto.request.NewsUpdateRequest;
import wisoft.labservice.domain.home.entity.News;
import wisoft.labservice.domain.home.repository.NewsRepository;
import wisoft.labservice.domain.common.exception.BusinessException;
import wisoft.labservice.domain.common.exception.ErrorCode;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsService {

    private final NewsRepository newsRepository;

    // 목록 조회
    @Transactional(readOnly = true)
    public List<News> findAll() {
        return newsRepository.findAllByOrderByCreatedAtDesc();
    }

    // 등록
    public News create(NewsCreateRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_PARAMETER, "title is required");
        }

        News news = News.builder()
                .id(UUID.randomUUID().toString())
                .title(req.getTitle())
                .detail(req.getDetail())
                .isActive(req.getIsActive())
                .isPinned(req.getIsPinned())
                .build();

        return newsRepository.save(news);
    }

    // 수정 (PATCH)
    public void update(String id, NewsUpdateRequest req) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NEWS_NOT_FOUND));

        if (req.getTitle() != null) {
            news.updateTitle(req.getTitle());
        }
        if (req.getDetail() != null) {
            news.updateDetail(req.getDetail());
        }
        if (req.getIsActive() != null) {
            news.updateActive(req.getIsActive());
        }
        if (req.getIsPinned() != null) {
            news.updatePinned(req.getIsPinned());
        }
    }

    // 삭제
    public void delete(String id) {
        if (!newsRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.NEWS_NOT_FOUND);
        }
        newsRepository.deleteById(id);
    }
}