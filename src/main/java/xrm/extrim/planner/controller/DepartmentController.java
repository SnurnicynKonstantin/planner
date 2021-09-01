package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.mapper.DepartmentMapper;
import xrm.extrim.planner.service.DepartmentService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/department")
@Api(description = "Operations pertaining to department")
public class DepartmentController {
    private static final String DEPARTMENT_ID = "Department id";

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all departments", response = DirectoryDto.class, responseContainer = "List")
    public List<DirectoryDto> getAll() {
        List<Department> departments = departmentService.getAll();
        return departmentMapper.listDepartmentsToDirectoryDto(departments);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get department by id", response = DirectoryDto.class)
    public DirectoryDto getDepartmentById(@ApiParam(DEPARTMENT_ID) @PathVariable("id") Long id) {
        Department department = departmentService.getById(id);
        return departmentMapper.departmentToDirectoryDto(department);
    }

    @PreAuthorize("hasRole('departmentOperations')")
    @PostMapping
    @ApiOperation(value = "Create department", response = DirectoryDto.class)
    public DirectoryDto createDepartment(@ApiParam("Department data") @RequestBody DirectoryDto departmentDto) {
        Department department = departmentService.create(departmentMapper.directoryDtoToDepartment(departmentDto));
        return departmentMapper.departmentToDirectoryDto(department);
    }

    @PreAuthorize("hasRole('departmentOperations')")
    @PutMapping("{id}")
    @ApiOperation(value = "Update department", response = DirectoryDto.class)
    public DirectoryDto updateDepartment(@ApiParam(DEPARTMENT_ID) @PathVariable("id") Long id,
                                         @ApiParam("Department data") @RequestBody DirectoryDto departmentDto) {
        Department department = departmentService.update(id, departmentMapper.directoryDtoToDepartment(departmentDto));
        return departmentMapper.departmentToDirectoryDto(department);
    }

    @PreAuthorize("hasRole('departmentOperations')")
    @DeleteMapping("{id}")
    @ApiOperation("Delete departments")
    public void deleteDepartment(@ApiParam(DEPARTMENT_ID) @PathVariable("id") Long id) {
        departmentService.delete(id);
    }
}
