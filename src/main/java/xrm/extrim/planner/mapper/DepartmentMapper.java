package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.domain.Department;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department directoryDtoToDepartment(DirectoryDto directoryDto);
    DirectoryDto departmentToDirectoryDto(Department department);
    List<DirectoryDto> listDepartmentsToDirectoryDto(List<Department> departments);
}
