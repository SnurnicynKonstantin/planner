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
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.mapper.PositionMapper;
import xrm.extrim.planner.service.PositionService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/position")
@Api(description = "Operations pertaining to position")
public class PositionController {
    private static final String POSITION_ID = "Position id";

    private final PositionService positionService;
    private final PositionMapper positionMapper;

    public PositionController(PositionService positionService, PositionMapper positionMapper) {
        this.positionService = positionService;
        this.positionMapper = positionMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all positions", response = DirectoryDto.class, responseContainer = "List")
    public List<DirectoryDto> getAllPositions() {
        List<Position> positions = positionService.getAll();
        return positionMapper.listPositionsToDirectoryDto(positions);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get position by id", response = DirectoryDto.class)
    public DirectoryDto getPositionById(@ApiParam(POSITION_ID) @PathVariable("id") Long id) {
        return positionMapper.positionToDirectoryDto(positionService.getById(id));
    }

    @PreAuthorize("hasRole('positionOperations')")
    @PostMapping
    @ApiOperation(value = "Create position", response = DirectoryDto.class)
    public DirectoryDto createPosition(@ApiParam("Position data") @RequestBody DirectoryDto positionDto) {
        Position position = positionService.create(positionMapper.directoryDtoToPosition(positionDto));
        return positionMapper.positionToDirectoryDto(position);
    }

    @PreAuthorize("hasRole('positionOperations')")
    @PutMapping("{id}")
    @ApiOperation(value = "Update position", response = DirectoryDto.class)
    public DirectoryDto updatePosition(@ApiParam(POSITION_ID) @PathVariable("id") Long id,
                                      @ApiParam("Position data") @RequestBody DirectoryDto positionDto) {
        Position position = positionService.update(id, positionMapper.directoryDtoToPosition(positionDto));
        return positionMapper.positionToDirectoryDto(position);
    }

    @PreAuthorize("hasRole('positionOperations')")
    @DeleteMapping("{id}")
    @ApiOperation("Delete position")
    public void deletePosition(@ApiParam(POSITION_ID) @PathVariable("id") Long id) {
        positionService.delete(id);
    }
}
