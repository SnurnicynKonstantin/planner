package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Announcement;


public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Announcement findByIsActiveTrue();
}
