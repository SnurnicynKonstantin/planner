package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.File;

import java.util.Collection;
import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    boolean existsByNameAndUploader_id(String name, Long uploaderId);

    List<File> findAllByUploader_Id(Long uploaderId);

    List<File> findAllByAttachedUsers_idIn(Collection<Long> attachedUsersId);
}
