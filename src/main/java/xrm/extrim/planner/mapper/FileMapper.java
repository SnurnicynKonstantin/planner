package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.FileDto;
import xrm.extrim.planner.domain.File;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDto FileToFileDto(File file);
    List<FileDto> listFileToFileDto(List<File> files);
}
