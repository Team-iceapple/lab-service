package wisoft.labservice.domain.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.labservice.domain.home.entity.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, String> {

    // 관리자 뉴스 목록 (최신순)
    List<News> findAllByOrderByCreatedAtDesc();
}