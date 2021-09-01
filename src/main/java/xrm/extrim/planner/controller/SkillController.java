package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.mapper.SkillMapper;
import xrm.extrim.planner.service.SkillService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/skill")
@Api(description = "Operations pertaining to skills")
public class SkillController {
    private final SkillService skillService;
    private final SkillMapper skillMapper;

    @Autowired
    public SkillController(SkillService skillService, SkillMapper skillMapper) {
        this.skillService = skillService;
        this.skillMapper = skillMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all skills", response = Skill.class, responseContainer = "List")
    public List<SkillDto> getAllSkills() {
        return this.skillMapper.skillToSkillDto(skillService.getAllSkills());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get skill", response = Skill.class)
    public SkillDto getSkillById(@ApiParam("Skill id") @PathVariable("id") Long id) {
        return skillMapper.skillToSkillDto(skillService.getSkillById(id));
    }

    @PreAuthorize("hasRole('skillOperations')")
    @PostMapping
    @ApiOperation(value = "Create skill", response = Skill.class, responseContainer = "List")
    public Skill createSkill(@ApiParam("Data about the skill and its descriptions")
                                 @RequestBody SkillDto skillDto) {
        return skillService.createSkill(skillDto);
    }

    @PreAuthorize("hasRole('skillOperations')")
    @PutMapping("{id}")
    @ApiOperation("Update skill")
    public Skill updateSkill (@ApiParam("Skill id") @PathVariable("id") Long id,
                              @ApiParam("Changed data about the skill and its descriptions")
                              @RequestBody SkillDto skillDto) {
        return skillService.updateSkill(id, skillDto);
    }

    @PreAuthorize("hasRole('skillOperations')")
    @DeleteMapping("{id}")
    @ApiOperation("Delete skill")
    public void deleteSkill(@ApiParam("Skill id") @PathVariable("id") Long id) {
        skillService.deleteSkillById(id);
    }
}
