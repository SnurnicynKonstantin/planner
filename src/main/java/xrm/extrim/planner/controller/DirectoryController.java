package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.mapper.DepartmentMapper;
import xrm.extrim.planner.mapper.PositionMapper;
import xrm.extrim.planner.service.DepartmentService;
import xrm.extrim.planner.service.PositionService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/directory")
@Api(description = "Operations pertaining to directories with codes")
public class DirectoryController {
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final DepartmentMapper departmentMapper;
    private  final PositionMapper positionMapper;

    @Autowired
    public DirectoryController(DepartmentService departmentService, PositionService positionService,
                               DepartmentMapper departmentMapper, PositionMapper positionMapper) {
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.departmentMapper = departmentMapper;
        this.positionMapper = positionMapper;
    }

    @GetMapping("position")
    @ApiOperation(value = "Get list of positions", response = DirectoryDto.class, responseContainer = "List")
    public List<DirectoryDto> getAllPositions() {
        List<Position> positions = positionService.getAll();
        return positionMapper.listPositionsToDirectoryDto(positions);
    }

    @GetMapping("department")
    @ApiOperation(value = "Get list of departments", response = DirectoryDto.class, responseContainer = "List")
    public List<DirectoryDto> getAllDepartments() {
        List<Department> departments = departmentService.getAll();
        return departmentMapper.listDepartmentsToDirectoryDto(departments);
    }
}
