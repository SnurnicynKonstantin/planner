package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import xrm.extrim.planner.controller.dto.RequestCommentDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.RequestComment;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RequestCommentMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "request", ignore = true)
    })
    RequestComment requestCommentDtoToRequestComment(RequestCommentDto commentDto);

    @Mappings({
            @Mapping(target = "createDate", source = "createDate"),
            @Mapping(target = "author", resultType = UserDto.class)
    })
    RequestCommentDto requestCommentToRequestCommentDto(RequestComment comment);

    List<RequestCommentDto> listRequestCommentToListDto(List<RequestComment> comments);
}
