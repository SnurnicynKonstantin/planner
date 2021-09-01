package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.AnnouncementDto;
import xrm.extrim.planner.domain.Announcement;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.AnnouncementMapper;
import xrm.extrim.planner.repository.AnnouncementRepository;

import java.util.List;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    public Announcement getActiveAnnouncement() {
        return announcementRepository.findByIsActiveTrue();
    }

    public List<AnnouncementDto> getAllAnnouncements() {
        return announcementMapper.listAnnouncementToListAnnouncementDto(announcementRepository.findAll());
    }

    public AnnouncementDto getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) {
            throw new PlannerException(String.format(Exception.ANNOUNCEMENT_NOT_FOUND.getDescription(), id));
        }
        return announcementMapper.announcementToAnnouncementDto(announcement);
    }

    public AnnouncementDto createAnnouncement(AnnouncementDto announcementDto) {
        if (announcementDto.getText().trim().equals("")) {
            throw new PlannerException(Exception.REQUIRED_FIELD_EMPTY.getDescription());
        }
        Announcement announcementFromDB = getActiveAnnouncement();
        if (announcementDto.getIsActive() && announcementFromDB != null) {
            announcementFromDB.setIsActive(false);
            announcementRepository.save(announcementFromDB);
        }
        Announcement announcement = announcementMapper.announcementDtoToAnnouncement(announcementDto);
        announcement.setCreatorId(UserAuthenticationHelper.getAuthenticatedUserData().getId());
        return announcementMapper.announcementToAnnouncementDto(announcementRepository.save(announcement));
    }

    public void editAnnouncement(Long id, AnnouncementDto announcementDto) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) {
            throw new PlannerException(String.format(Exception.ANNOUNCEMENT_NOT_FOUND.getDescription(), announcementDto.getId()));
        }
        Announcement announcementFromDB = getActiveAnnouncement();
        if (announcementFromDB != null && !announcementFromDB.getId().equals(id) && announcementDto.getIsActive()) {
            announcementFromDB.setIsActive(false);
            announcementRepository.save(announcementFromDB);
        }
        announcement.setText(announcementDto.getText());
        announcement.setIsActive(announcementDto.getIsActive());
        announcementRepository.save(announcement);
    }

    public void deleteAnnouncementById(Long id) {
        announcementRepository.deleteById(id);
    }

    public Page<Announcement> getPaginatedAnnouncements(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return announcementRepository.findAll(pageable);
    }
}
