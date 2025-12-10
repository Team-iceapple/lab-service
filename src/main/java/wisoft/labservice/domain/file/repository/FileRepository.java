package wisoft.labservice.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.labservice.domain.file.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, String> {
}