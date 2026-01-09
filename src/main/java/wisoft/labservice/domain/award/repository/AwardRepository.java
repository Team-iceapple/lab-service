package wisoft.labservice.domain.award.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.award.entity.Award;


public interface AwardRepository extends JpaRepository<Award, String> {

    // Admin 전용
    // 전체 조회 (이미지 파일 포함)
    @Query("""
        SELECT a FROM Award a
        LEFT JOIN FETCH a.imageFile
        ORDER BY a.year DESC, a.createdAt DESC
    """)
    List<Award> findAllWithImageFile();

    // 단건 조회 (이미지 파일 포함)
    @Query("""
        SELECT a FROM Award a
        LEFT JOIN FETCH a.imageFile
        WHERE a.id = :awardId
    """)
    Optional<Award> findByIdWithImageFile(@Param("awardId") String awardId);

    // User 전용 (활성 데이터만)
    // 전체 조회 (이미지 파일 포함)
    @Query("""
        SELECT a FROM Award a
        LEFT JOIN FETCH a.imageFile
        WHERE a.isActive = true
        ORDER BY a.year DESC, a.createdAt DESC
    """)
    List<Award> findAllActiveWithImageFile();

    // 단건 조회 (이미지 파일 포함)
    @Query("""
        SELECT a FROM Award a
        LEFT JOIN FETCH a.imageFile
        WHERE a.id = :awardId
          AND a.isActive = true
    """)
    Optional<Award> findActiveByIdWithImageFile(@Param("awardId") String awardId);
}