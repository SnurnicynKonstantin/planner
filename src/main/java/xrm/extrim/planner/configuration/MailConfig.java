package xrm.extrim.planner.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xrm.extrim.planner.enums.Mail;

import java.util.AbstractMap;
import java.util.Map;

@Configuration
@Data
public class MailConfig {

    @Value("${mail.cron.daysToIdp}")
    private int daysToIDP;

    @Value("${base.url}")
    private String baseUrl;

    private String idpSubject = Mail.IDP_SUBJECT.getText();

    private String idpTextForUser = Mail.IDP_TEXT_FOR_USER.getText();

    private String idpTextForManager = Mail.IDP_TEXT_FOR_MANAGER.getText();

    private String vacationRequestSubject = Mail.VACATION_REQUEST_SUBJECT.getText();

    private String vacationRequestTextForCreator = Mail.VACATION_REQUEST_TEXT_FOR_CREATOR.getText();

    private String vacationRequestTextForApprovers = Mail.VACATION_REQUEST_TEXT_FOR_APPROVERS.getText();

    private String vacationRequestApprovedTextForCreator = Mail.VACATION_REQUEST_APPROVED_TEXT_FOR_CREATOR.getText();

    private String birthdayRequestSubject = Mail.BIRTHDAY_REQUEST_SUBJECT.getText();

    private String birthdayRequestText = Mail.BIRTHDAY_REQUEST_TEXT.getText();

    private String birthdayInfoRequestHeaderText = Mail.BIRTHDAY_INFO_REQUEST_HEADER_TEXT.getText();

    private String birthdayInfoRequestPersonText = Mail.BIRTHDAY_INFO_REQUEST_PERSON_TEXT.getText();

    private String birthdayInfoRequestEndText = Mail.BIRTHDAY_INFO_REQUEST_END_TEXT.getText();

    private String setRequestExecutorSubject = Mail.SET_REQUEST_EXECUTOR_SUBJECT.getText();
    private String setRequestExecutorText = Mail.SET_REQUEST_EXECUTOR_TEXT.getText();

    @Value("${mail.address.generalMailing}")
    private String generalMailingAddress;

    private final Map<Integer, String> monthNumbers  = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(1,"января"),
            new AbstractMap.SimpleEntry<>(2,"февраля"),
            new AbstractMap.SimpleEntry<>(3,"марта"),
            new AbstractMap.SimpleEntry<>(4,"апреля"),
            new AbstractMap.SimpleEntry<>(5,"мая"),
            new AbstractMap.SimpleEntry<>(6,"июня"),
            new AbstractMap.SimpleEntry<>(7,"июля"),
            new AbstractMap.SimpleEntry<>(8,"августа"),
            new AbstractMap.SimpleEntry<>(9,"сентября"),
            new AbstractMap.SimpleEntry<>(10,"октября"),
            new AbstractMap.SimpleEntry<>(11,"ноября"),
            new AbstractMap.SimpleEntry<>(12,"декабря")
    );

    private String vacationRequestUrl;

    public String getVacationRequestUrl() {
        return baseUrl + "vacation/";
    }
}