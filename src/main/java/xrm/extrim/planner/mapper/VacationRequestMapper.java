package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xrm.extrim.planner.controller.dto.VacationDto;
import xrm.extrim.planner.domain.VacationRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class VacationRequestMapper {
    @Mapping(target = "requestId", source = "vacationRequest.id")
    @Mapping(target = "creatorId", source = "vacationRequest.creatorId")
    public abstract VacationDto vacationRequestToVacationRequestDto(VacationRequest vacationRequest);

    public abstract VacationRequest vacationRequestDtoToVacationRequest(VacationDto vacationDto);

    @Mapping(target = "requestId", source = "vacationRequests.id")
    @Mapping(target = "creatorId", source = "vacationRequest.creatorId")
    public abstract List<VacationDto> vacationRequestsToVacationRequestDtos(List<VacationRequest> vacationRequests);

    public abstract List<VacationRequest> vacationRequestDtosToVacationRequests(List<VacationDto> vacationRequestsDtos);
}
