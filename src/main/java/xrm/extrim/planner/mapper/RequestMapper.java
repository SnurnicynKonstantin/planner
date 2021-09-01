package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.controller.dto.RequestDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.Request;
import xrm.extrim.planner.enums.RequestStatus;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, DirectoryMapper.class})
public interface RequestMapper {

    @Mappings({
            @Mapping(target = "creator", resultType = UserDto.class),
            @Mapping(target = "executor", resultType = UserDto.class),
            @Mapping(target = "createDate", source = "createDate"),
            @Mapping(target = "updateDate", source = "updateDate"),
            @Mapping(target = "status", resultType = DirectoryDto.class),
    })
    RequestDto requestDtoToRequest(Request request);



    List<RequestDto> listRequestsToListRequestDto(List<Request> requests);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "updateDate", ignore = true),
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "status", resultType = RequestStatus.class),
    })
    Request requestToRequestDto(RequestDto requestDto);
}
