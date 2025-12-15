package wisoft.labservice.domain.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.labservice.domain.home.entity.LabImage;

import java.util.List;

public interface LabImageRepository extends JpaRepository<LabImage, String> {
    List<LabImage> findAllByOrderByCreatedAtAsc();
}