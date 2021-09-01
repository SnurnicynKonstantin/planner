package xrm.extrim.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xrm.extrim.planner.controller.dto.RateDescriptionDto;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.service.RateDescriptionService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/rate-description")
public class RateDescriptionController {
    private RateDescriptionService rateDescriptionService;

    @Autowired
    public RateDescriptionController(RateDescriptionService rateDescriptionService) {
        this.rateDescriptionService = rateDescriptionService;
    }

    @GetMapping
    public List<RateDescription> getAllRateDescriptions() {
        return rateDescriptionService.getAllRateDescriptions();
    }

    @GetMapping("/{id}")
    public RateDescription getRateDescriptionById(@PathVariable("id") Long id) {
        return rateDescriptionService.getRateDescriptionById(id);
    }

    @GetMapping("/skill/{skill-id}")
    public List<RateDescription> getRateDescriptionsBySkillId(@PathVariable("skill-id") Long skillId) {
        return rateDescriptionService.findBySkillId(skillId);
    }

    @PreAuthorize("hasRole('skillOperations')")
    @PostMapping
    public RateDescription createRateDescription(@RequestBody RateDescriptionDto rateDescriptionDto) {
        return rateDescriptionService.createRateDescription(rateDescriptionDto);
    }

    @PreAuthorize("hasRole('skillOperations')")
    @PutMapping("{id}")
    public RateDescription updateRateDescription(@PathVariable("id") Long id, @RequestBody RateDescriptionDto rateDescriptionDto) {
        return rateDescriptionService.updateRateDescription(id, rateDescriptionDto);
    }

    @PreAuthorize("hasRole('skillOperations')")
    @DeleteMapping("{id}")
    public void deleteRateDescription(@PathVariable("id") Long id) {
        rateDescriptionService.deleteRateDescriptionById(id);
    }
}
