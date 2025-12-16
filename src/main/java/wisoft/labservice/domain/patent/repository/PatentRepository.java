package wisoft.labservice.domain.patent.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.labservice.domain.patent.entity.Patent;

public interface PatentRepository extends JpaRepository<Patent, String> {

    @Query("SELECT p FROM Patent p LEFT JOIN FETCH p.pdfFile " +
            "ORDER BY p.year DESC, p.createdAt DESC")
    List<Patent> findAllWithPdfFile();

    @Query("SELECT p FROM Patent p LEFT JOIN FETCH p.pdfFile WHERE p.id = :patentId")
    Optional<Patent> findByIdWithPdfFile(@Param("patentId") String patentId);
}
