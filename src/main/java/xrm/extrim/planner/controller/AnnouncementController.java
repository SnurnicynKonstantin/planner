package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.AnnouncementDto;
import xrm.extrim.planner.domain.Announcement;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.enums.PageSize;
import xrm.extrim.planner.exception.ForbiddenException;
import xrm.extrim.planner.mapper.AnnouncementMapper;
import xrm.extrim.planner.service.AnnouncementService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/announcement")
@Api(description = "Operations pertaining to announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final AnnouncementMapper announcementMapper;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService, AnnouncementMapper announcementMapper) {
        this.announcementService = announcementService;
        this.announcementMapper = announcementMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all announcements", response = AnnouncementDto.class, responseContainer = "List")
    public List<AnnouncementDto> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get announcement by id", response = AnnouncementDto.class)
    public AnnouncementDto getAnnouncementById(@ApiParam("Announcement id") @PathVariable("id") Long id) {
        return announcementService.getAnnouncementById(id);
    }

    @PostMapping
    @ApiOperation(value = "Create announcement", response = Announcement.class)
    public AnnouncementDto createAnnouncement(@ApiParam("Announcement data") @RequestBody AnnouncementDto announcementDto) {
        if (UserAuthenticationHelper.getAuthenticatedUserData().getRole().isAnnouncementOperations()) {
            return announcementService.createAnnouncement(announcementDto);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Edit announcement")
    public void editAnnouncement(@ApiParam("Announcement id") @PathVariable Long id,
                                 @ApiParam("New announcement data") @RequestBody AnnouncementDto announcementDto) {
        if (UserAuthenticationHelper.getAuthenticatedUserData().getRole().isAnnouncementOperations()) {
            announcementService.editAnnouncement(id, announcementDto);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete announcement")
    public void deleteAnnouncementById(@ApiParam("Announcement id") @PathVariable("id") Long id) {
        if (UserAuthenticationHelper.getAuthenticatedUserData().getRole().isAnnouncementOperations()) {
            announcementService.deleteAnnouncementById(id);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    @GetMapping("/active")
    @ApiOperation("Get active announcement")
    public AnnouncementDto getActiveAnnouncement() {
        return announcementMapper.announcementToAnnouncementDto(announcementService.getActiveAnnouncement());
    }

    @PostMapping("/paginated")
    @ApiOperation("Get paginated announcements")
    public Page<AnnouncementDto> getPaginatedAnnouncements(@ApiParam("Page number") @RequestParam Integer page,
                                                           @ApiParam("Page size") @RequestParam PageSize pageSize) {
        if (UserAuthenticationHelper.getAuthenticatedUserData().getRole().isAnnouncementOperations()) {
            Page<Announcement> announcements = announcementService.getPaginatedAnnouncements(page, pageSize.size);
            return announcements.map(announcementMapper::announcementToAnnouncementDto);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }
}
