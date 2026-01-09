package wisoft.labservice.domain.patent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.patent.entity.Patent;

public interface PatentRepository extends JpaRepository<Patent, String> {

    // Admin 전체 조회
    @Query("""
        SELECT p FROM Patent p
        LEFT JOIN FETCH p.pdfFile
        LEFT JOIN FETCH p.thumbnailFile
        ORDER BY p.year DESC, p.createdAt DESC
    """)
    List<Patent> findAllWithFiles();

    // Admin 단건 조회
    @Query("""
        SELECT p FROM Patent p
        LEFT JOIN FETCH p.pdfFile
        LEFT JOIN FETCH p.thumbnailFile
        WHERE p.id = :patentId
    """)
    Optional<Patent> findByIdWithFiles(@Param("patentId") String patentId);

    // User 전체 조회 (활성만)
    @Query("""
        SELECT p FROM Patent p
        LEFT JOIN FETCH p.pdfFile
        LEFT JOIN FETCH p.thumbnailFile
        WHERE p.isActive = true
        ORDER BY p.year DESC, p.createdAt DESC
    """)
    List<Patent> findAllActiveWithFiles();

    // User 단건 조회 (활성만)
    @Query("""
        SELECT p FROM Patent p
        LEFT JOIN FETCH p.pdfFile
        LEFT JOIN FETCH p.thumbnailFile
        WHERE p.id = :patentId
          AND p.isActive = true
    """)
    Optional<Patent> findActiveByIdWithFiles(@Param("patentId") String patentId);
}
