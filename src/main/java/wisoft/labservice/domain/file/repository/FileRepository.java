package wisoft.labservice.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.labservice.domain.file.entity.FileResource;

public interface FileRepository extends JpaRepository<FileResource, String> {
}