package wisoft.labservice.domain.project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {

    // 전체 조회 (썸네일 파일 포함)
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.thumbnailFile ORDER BY p.year DESC, p.id DESC")
    List<Project> findAllWithThumbnailFile();

    // 단건 조회 (썸네일 파일 포함)
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.thumbnailFile WHERE p.id = :projectId")
    Optional<Project> findByIdWithThumbnailFile(@Param("projectId") String projectId);
}
