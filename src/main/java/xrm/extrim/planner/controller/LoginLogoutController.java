package xrm.extrim.planner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL)
public class LoginLogoutController {
    @ApiOperation("Login.")
    @PostMapping("/login")
    public void fakeLogin(@ApiParam("User") @RequestParam String username, @ApiParam("Password") @RequestParam String password) {
        throw new PlannerException(Exception.ILLEGAL_METHOD_CALL.getDescription());
    }

    @ApiOperation("Logout.")
    @PostMapping("/logout")
    public void fakeLogout() {
        throw new PlannerException(Exception.ILLEGAL_METHOD_CALL.getDescription());
    }
}
