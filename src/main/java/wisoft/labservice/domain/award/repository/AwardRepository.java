package wisoft.labservice.domain.award.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.award.entity.Award;

public interface AwardRepository extends JpaRepository<Award, String> {

    @Query("SELECT a FROM Award a LEFT JOIN FETCH a.imageFile " +
            "ORDER BY a.year DESC, a.createdAt DESC")
    List<Award> findAllWithImageFile();

    @Query("SELECT a FROM Award a LEFT JOIN FETCH a.imageFile WHERE a.id = :awardId")
    Optional<Award> findByIdWithImageFile(@Param("awardId") String awardId);
}
