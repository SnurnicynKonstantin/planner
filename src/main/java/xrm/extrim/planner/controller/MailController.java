package xrm.extrim.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xrm.extrim.planner.service.MailService;
import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/mail")
public class MailController {
    private final MailService mailService;
    public static final String SUCCESSFUL_RESPONSE = "Emails were sent!";

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @ResponseBody
    @PostMapping("/sendIDPEmails")
    public String sendIDPEmails() {
        mailService.sendIDPEmails();
        return SUCCESSFUL_RESPONSE;
    }
}
