package wisoft.labservice.domain.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.labservice.domain.home.entity.GallerySlide;

import java.util.List;
import java.util.Optional;

public interface GallerySlideRepository extends JpaRepository<GallerySlide, String> {
    List<GallerySlide> findAllByOrderByDisplayOrderAsc();
    Optional<GallerySlide> findTopByOrderByDisplayOrderDesc();
}