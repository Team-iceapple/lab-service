package wisoft.labservice.domain.paper.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.paper.entity.Paper;

public interface PaperRepository extends JpaRepository<Paper, String> {

    // 전체 조회 (이미지 파일 포함)
    @Query("SELECT p FROM Paper p LEFT JOIN FETCH p.imageFile " +
            "ORDER BY p.year DESC, p.createdAt DESC")
    List<Paper> findAllWithImageFile();

    // 단건 조회 (이미지 파일 포함)
    @Query("SELECT p FROM Paper p LEFT JOIN FETCH p.imageFile WHERE p.id = :paperId")
    Optional<Paper> findByIdWithImageFile(@Param("paperId") String paperId);
}
