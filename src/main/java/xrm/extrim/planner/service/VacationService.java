package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.VacationDto;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.VacationApprover;
import xrm.extrim.planner.domain.VacationRequest;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.VacationRequestMapper;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.repository.VacationApproverRepository;
import xrm.extrim.planner.repository.VacationRequestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationService {
    private final VacationRequestRepository vacationRequestRepository;
    private final VacationApproverRepository vacationApproverRepository;
    private final UserRepository userRepository;
    private final VacationRequestMapper vacationRequestMapper;
    private final MailService mailService;

    @Autowired
    public VacationService(VacationRequestRepository vacationRequestRepository, VacationApproverRepository vacationApproverRepository, VacationRequestMapper vacationRequestMapper, UserRepository userRepository, MailService mailService) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.vacationApproverRepository = vacationApproverRepository;
        this.vacationRequestMapper = vacationRequestMapper;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public VacationDto createVacation(VacationDto vacation) {
        User user = UserAuthenticationHelper.getAuthenticatedUserData();
        if (vacation.getApproversIds().contains(user.getId())) {
            throw new PlannerException(Exception.CANNOT_MANAGE_YOURSELF.getDescription());
        }
        if (vacation.getDateFrom().isBefore(LocalDate.now()) || vacation.getDateTo().isBefore(LocalDate.now())) {
            throw new PlannerException(Exception.VACATION_DATE_PAST.getDescription());
        }
        List<User> approvers = new ArrayList<>();
        VacationRequest vacationRequest = vacationRequestMapper.vacationRequestDtoToVacationRequest(vacation);
        vacationRequest.setCreatorId(user.getId());
        vacationRequest.setIsDraft(vacation.getIsDraft());
        VacationRequest savedVacationRequest = vacationRequestRepository.save(vacationRequest);
        vacation.setRequestId(savedVacationRequest.getId());
        vacation.getApproversIds().forEach(approverId -> {
            approvers.add(userRepository.getById(approverId));
            VacationApprover vacationApprover = new VacationApprover();
            vacationApprover.setRequestId(savedVacationRequest.getId());
            vacationApprover.setApproverId(approverId);
            vacationApproverRepository.save(vacationApprover);
        });
        if (!vacation.getIsDraft() && !vacation.getApproversIds().isEmpty()) {
            mailService.sendVacationRequestEmails(approvers, user, vacation.getRequestId());
        }
        return vacation;
    }

    public void deleteVacation(Long vacationRequestId) {
        vacationApproverRepository.deleteByRequestId(vacationRequestId);
        vacationRequestRepository.deleteById(vacationRequestId);
    }

    public List<VacationDto> getAllVacations() {
        return vacationRequestMapper.vacationRequestsToVacationRequestDtos(vacationRequestRepository.findAll())
                .stream().map(this::generateVacation).collect(Collectors.toList());
    }

    public VacationDto getVacationById(Long vacationRequestId) {
        return generateVacation(vacationRequestMapper.vacationRequestToVacationRequestDto(vacationRequestRepository.getById(vacationRequestId)));
    }

    public VacationDto updateVacation(Long vacationRequestId, VacationDto vacation) {
        User user = UserAuthenticationHelper.getAuthenticatedUserData();
        if (vacation.getApproversIds().contains(user.getId())) {
            throw new PlannerException(Exception.CANNOT_MANAGE_YOURSELF.getDescription());
        }
        if (vacation.getDateFrom().isBefore(LocalDate.now()) || vacation.getDateTo().isBefore(LocalDate.now())) {
            throw new PlannerException(Exception.WRONG_DATE_FORMAT.getDescription());
        }
        VacationRequest vacationRequest = vacationRequestRepository.getById(vacationRequestId);
        vacationRequest.setDateFrom(vacation.getDateFrom());
        vacationRequest.setDateTo(vacation.getDateTo());

        vacationRequest.setIsDraft(vacation.getIsDraft());
        List<User> approvers = new ArrayList<>();

        vacationApproverRepository.deleteByRequestId(vacationRequestId);
        vacation.getApproversIds().forEach(approverId -> {
            approvers.add(userRepository.getById(approverId));
            VacationApprover vacationApprover = new VacationApprover();
            vacationApprover.setRequestId(vacationRequestId);
            vacationApprover.setApproverId(approverId);
            vacationApprover.setApproved(false);
            vacationApproverRepository.save(vacationApprover);
        });


        VacationRequest savedVacationRequest = vacationRequestRepository.save(vacationRequest);
        if (!vacationRequest.getIsDraft() && !vacation.getApproversIds().isEmpty()) {
            mailService.sendVacationRequestEmails(approvers, user, savedVacationRequest.getId());
        }
        return vacationRequestMapper.vacationRequestToVacationRequestDto(savedVacationRequest);
    }

    public void approveVacation(Long vacationRequestId) {
        User user = UserAuthenticationHelper.getAuthenticatedUserData();
        if (!vacationApproverRepository.getApproverIdsByRequestId(vacationRequestId).contains(user.getId())) {
            throw new PlannerException(String.format(Exception.VACATION_REQUEST_ACCESS_DENIED.getDescription(), vacationRequestId));
        }
        VacationApprover vacationApprover = vacationApproverRepository.getByRequestIdAndApproverId(vacationRequestId, user.getId());
        vacationApprover.setApproved(true);
        vacationApproverRepository.save(vacationApprover);

        if (isApprovedVacation(vacationRequestId)) {
            mailService.sendApprovedVacationEmail(userRepository.getById(vacationRequestRepository.getById(vacationApprover.getRequestId()).getCreatorId()));
        }
    }

    public boolean isApprovedVacation(Long vacationRequestId) {
        return !vacationApproverRepository.getIsApprovedByRequestId(vacationRequestId).contains(false);
    }

    public List<VacationDto> getVacationsByCreatorId(Long creatorId) {
        return vacationRequestMapper.vacationRequestsToVacationRequestDtos(vacationRequestRepository.getAllByCreatorId(creatorId))
                .stream().map(this::generateVacation).collect(Collectors.toList());
    }

    private VacationDto generateVacation(VacationDto vacation) {
        vacation.setApproversIds(vacationApproverRepository.getApproverIdsByRequestId(vacation.getRequestId()));
        vacation.getApproversIds().forEach(approverId -> {
            User user = userRepository.getById(approverId);
            vacation.getApproversMarks().add(vacationApproverRepository.getByRequestIdAndApproverId(vacation.getRequestId(), approverId).isApproved());
            vacation.getApproversNames().add(user.getName() + " " + user.getSurname());
        });
        return vacation;
    }
}
