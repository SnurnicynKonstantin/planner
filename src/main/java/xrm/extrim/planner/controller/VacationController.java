package xrm.extrim.planner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.VacationDto;
import xrm.extrim.planner.service.VacationService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/vacation")
public class VacationController {
    private final static String VACATION_ID = "Vacation id";
    private final VacationService vacationService;

    @Autowired
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @PostMapping
    @ApiOperation("Create new vacation request")
    public VacationDto createVacation(@ApiParam("Data for create vacation request") @RequestBody VacationDto vacationDto) {
        return vacationService.createVacation(vacationDto);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete vacation request")
    public void deleteVacation(@ApiParam(VACATION_ID) @PathVariable(name = "id") Long vacationId) {
        vacationService.deleteVacation(vacationId);
    }

    @GetMapping
    @ApiOperation("Get all vacation requests")
    public List<VacationDto> getAllVacation() {
        return vacationService.getAllVacations();
    }

    @GetMapping("{id}")
    @ApiOperation("Get vacation request by id")
    public VacationDto getVacation(@ApiParam(VACATION_ID) @PathVariable(name = "id") Long vacationId) {
        return vacationService.getVacationById(vacationId);
    }

    @PutMapping("{id}")
    @ApiOperation("Update vacation request")
    public VacationDto updateVacation(@ApiParam(VACATION_ID) @PathVariable(name = "id") Long vacationId,
                                      @ApiParam("Data with new date from, date to and is draft value") @RequestBody VacationDto vacationDto) {
        return vacationService.updateVacation(vacationId, vacationDto);
    }

    @PutMapping("approve/{id}")
    @ApiOperation("Approve vacation request")
    public void approveVacation(@ApiParam(VACATION_ID) @PathVariable(name = "id") Long vacationId) {
        vacationService.approveVacation(vacationId);
    }

    @GetMapping("is-approved/{id}")
    @ApiOperation("Check vacation request on approve")
    public boolean isApprovedVacation(@ApiParam(VACATION_ID) @PathVariable(name = "id") Long vacationId) {
        return vacationService.isApprovedVacation(vacationId);
    }

    @GetMapping("user-requests/{id}")
    @ApiOperation("Get all user's vacation requests")
    public List<VacationDto> getVacationsByCreatorId(@ApiParam("User id") @PathVariable(name = "id") Long creatorId) {
        return vacationService.getVacationsByCreatorId(creatorId);
    }
}
