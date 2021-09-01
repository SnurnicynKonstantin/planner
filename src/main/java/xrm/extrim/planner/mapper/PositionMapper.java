package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.domain.Position;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    Position directoryDtoToPosition(DirectoryDto directoryDto);
    DirectoryDto positionToDirectoryDto(Position position);
    List<DirectoryDto> listPositionsToDirectoryDto(List<Position> positions);
}
