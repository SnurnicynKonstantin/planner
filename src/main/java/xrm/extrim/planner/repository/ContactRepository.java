package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
