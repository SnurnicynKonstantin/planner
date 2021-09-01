package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import xrm.extrim.planner.controller.dto.AnnouncementDto;
import xrm.extrim.planner.domain.Announcement;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AnnouncementMapper {
    @Autowired
    private UserService userService;

    public Announcement announcementDtoToAnnouncement(AnnouncementDto announcementDto) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementDto.getId());
        announcement.setCreatorId(announcementDto.getCreatorId());
        announcement.setText(announcementDto.getText().trim());
        announcement.setIsActive(announcementDto.getIsActive());
        return announcement;
    };
    public AnnouncementDto announcementToAnnouncementDto(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        AnnouncementDto announcementDto = new AnnouncementDto();
        announcementDto.setId(announcement.getId());
        announcementDto.setCreatorId(announcement.getCreatorId());
        announcementDto.setText(announcement.getText());
        announcementDto.setIsActive(announcement.getIsActive());
        User user = userService.getUser(announcement.getCreatorId());
        announcementDto.setCreatorName(user.getName() + " " + user.getSurname());
        return announcementDto;
    };
    public abstract List<Announcement> listAnnouncementDtoToListAnnouncement(List<AnnouncementDto> announcementDtos);
    public abstract List<AnnouncementDto> listAnnouncementToListAnnouncementDto(List<Announcement> announcements);
}
