package wisoft.labservice.domain.project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {

    // Admin 전용
    // 전체 조회 (썸네일 파일 포함)
    @Query("""
        SELECT p FROM Project p
        LEFT JOIN FETCH p.thumbnailFile
        ORDER BY p.year DESC, p.id DESC
    """)
    List<Project> findAllWithThumbnailFile();

    // 단건 조회 (썸네일 파일 포함)
    @Query("""
        SELECT p FROM Project p
        LEFT JOIN FETCH p.thumbnailFile
        WHERE p.id = :projectId
    """)
    Optional<Project> findByIdWithThumbnailFile(@Param("projectId") String projectId);

    // User 전용 (활성 프로젝트만)
    // 전체 조회 (썸네일 파일 포함)
    @Query("""
        SELECT p FROM Project p
        LEFT JOIN FETCH p.thumbnailFile
        WHERE p.isActive = true
        ORDER BY p.year DESC, p.id DESC
    """)
    List<Project> findAllActiveWithThumbnailFile();

    // 단건 조회 (썸네일 파일 포함)
    @Query("""
        SELECT p FROM Project p
        LEFT JOIN FETCH p.thumbnailFile
        WHERE p.id = :projectId
          AND p.isActive = true
    """)
    Optional<Project> findActiveByIdWithThumbnailFile(@Param("projectId") String projectId);
}