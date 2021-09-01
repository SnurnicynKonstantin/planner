package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.RateDescriptionDto;
import xrm.extrim.planner.domain.RateDescription;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateDescriptionMapper {
    RateDescription rateDescriptionDtoToRateDescription(RateDescriptionDto rateDescriptionDto);
    RateDescriptionDto rateDescriptionToRateDescriptionDto(RateDescription rateDescription);
    List<RateDescriptionDto> listRateDescriptionsToListRateDescriptionDtos(List<RateDescription> rateDescriptions);
    List<RateDescription> liseRateDescriptionDtoToListRateDescription(List<RateDescriptionDto> rateDescriptionDtos);
}
