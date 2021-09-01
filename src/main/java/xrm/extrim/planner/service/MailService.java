package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.configuration.MailConfig;
import xrm.extrim.planner.domain.MailIDP;
import xrm.extrim.planner.domain.Task;
import xrm.extrim.planner.domain.MailVacationApprover;
import xrm.extrim.planner.domain.Request;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.VacationRequest;
import xrm.extrim.planner.repository.MailIDPRepository;
import xrm.extrim.planner.repository.MailVacationApproverRepository;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.repository.VacationRequestRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {
    private final UserService userService;
    private final JavaMailSender emailSender;
    private final MailIDPRepository mailIDPRepository;
    private final MailConfig mailConfig;
    private final VacationRequestRepository vacationRequestRepository;
    private final UserRepository userRepository;
    private final MailVacationApproverRepository mailVacationApproverRepository;

    @Autowired
    public MailService(UserService userService, JavaMailSender emailSender, MailIDPRepository mailIDPRepository,
                       MailConfig mailConfig, VacationRequestRepository vacationRequestRepository,
                       UserRepository userRepository, MailVacationApproverRepository mailVacationApproverRepository) {
        this.userService = userService;
        this.emailSender = emailSender;
        this.mailIDPRepository = mailIDPRepository;
        this.mailConfig = mailConfig;
        this.vacationRequestRepository = vacationRequestRepository;
        this.userRepository = userRepository;
        this.mailVacationApproverRepository = mailVacationApproverRepository;
    }

    @Scheduled(cron = "${mail.cron.timeTriggerVacationApprove}")
    public void resendVacationApproveEmails() {
        LocalDate overdueDate = LocalDate.now().plusDays(3);
        vacationRequestRepository.getAllOverdueVacationRequests(overdueDate).forEach(request -> {
            User creator = userRepository.findById(request.getCreatorId()).orElse(null);

            userRepository.findAllApproversNotApproveByRequestId(request.getId()).forEach(approver -> {
                if (!mailVacationApproverRepository.existsByApprover_IdAndRequest_Id(approver.getId(), request.getId())) {
                    markThatMailed(approver, request);
                    sendEmailToApprover(approver, creator);
                }
            });
        });
    }

    private void sendEmailToApprover(User approver, User creator) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(approver.getContact().getEmail());
        message.setSubject(mailConfig.getVacationRequestSubject());
        message.setText(String.format(mailConfig.getVacationRequestTextForApprovers(),
                approver.getName(), approver.getSurname(), creator.getName(), creator.getSurname()));
        emailSender.send(message);
    }

    private void markThatMailed(User approver, VacationRequest request) {
        MailVacationApprover mailVacationApprover = new MailVacationApprover();
        mailVacationApprover.setApprover(approver);
        mailVacationApprover.setRequest(request);
        mailVacationApproverRepository.save(mailVacationApprover);
    }

    @Scheduled(cron = "${mail.cron.timeTriggerIdp}")
    public void sendIDPEmails() {
        LocalDate currentDate = LocalDate.now();
        userService.findUsersByDaysToIDP(mailConfig.getDaysToIDP()).forEach(user -> {
            MailIDP mailIDPFromDB = mailIDPRepository.findAllByIdpDateBetweenAndUserId(currentDate, currentDate.plusDays(mailConfig.getDaysToIDP()), user.getId());

            if (mailIDPFromDB == null) {
                mailIDPFromDB = new MailIDP();
                mailIDPFromDB.setIdpDate(user.getIdpDate());
                mailIDPFromDB.setUserId(user.getId());

                SimpleMailMessage message = new SimpleMailMessage();
                String username = user.getName() + " " + user.getSurname();
                message.setTo(user.getContact().getEmail());
                message.setSubject(mailConfig.getIdpSubject());
                String tasksDescriptions = user.getTasks().stream().map(Task::getDescription).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.joining("\n"));
                message.setText(String.format(mailConfig.getIdpTextForUser(), user.getName(), user.getSurname(), mailConfig.getDaysToIDP(), tasksDescriptions, user.getIdpDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                emailSender.send(message);
                while (user.getManager() != null) {
                    message.setTo(user.getManager().getContact().getEmail());
                    message.setText(String.format(mailConfig.getIdpTextForManager(), user.getManager().getName(), user.getManager().getSurname(), mailConfig.getDaysToIDP(), username, tasksDescriptions, user.getIdpDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                    emailSender.send(message);
                    user = user.getManager();
                }
                mailIDPRepository.save(mailIDPFromDB);
            }
        });
    }

    public void sendVacationRequestEmails(List<User> approvers, User creator, Long requestId) {
        SimpleMailMessage message = new SimpleMailMessage();
        List<String> approversNames = new ArrayList<>();

        approvers.forEach(approver -> {
            approversNames.add(approver.getName() + " " + approver.getSurname() + ", ");
            message.setTo(approver.getContact().getEmail());
            message.setSubject(mailConfig.getVacationRequestSubject());
            message.setText(String.format(mailConfig.getVacationRequestTextForApprovers(), approver.getName(), approver.getSurname(), creator.getName(), creator.getSurname(), mailConfig.getVacationRequestUrl() + requestId));
            emailSender.send(message);
        });

        message.setTo(creator.getContact().getEmail());
        message.setSubject(mailConfig.getVacationRequestSubject());
        message.setText(String.format(mailConfig.getVacationRequestTextForCreator(), creator.getName(), creator.getSurname(), String.join("", approversNames).substring(0, String.join("", approversNames).length() - 2), mailConfig.getVacationRequestUrl() + requestId));
        emailSender.send(message);
    }

    public void sendApprovedVacationEmail(User creator) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(creator.getContact().getEmail());
        message.setSubject(mailConfig.getVacationRequestSubject());
        message.setText(String.format(mailConfig.getVacationRequestApprovedTextForCreator(), creator.getName(), creator.getSurname()));
        emailSender.send(message);
    }

    public void sendBirthdayEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getContact().getEmail());
        message.setSubject(mailConfig.getBirthdayRequestSubject());
        message.setText(String.format(mailConfig.getBirthdayRequestText(), user.getName(), user.getSurname()));
        emailSender.send(message);
    }

    public void sendBirthdayInfoEmail(List<User> users) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailConfig.getGeneralMailingAddress());
        message.setSubject(mailConfig.getBirthdayRequestSubject());
        String messageText = mailConfig.getBirthdayInfoRequestHeaderText();
        for (User user : users) {
            String userStr = String.format(mailConfig.getBirthdayInfoRequestPersonText(),
                    user.getBirthday().getDayOfMonth(),mailConfig.getMonthNumbers().get(user.getBirthday().getMonthValue()),user.getName(), user.getSurname());
            messageText = messageText.concat(userStr);
        }
        messageText = messageText.concat(mailConfig.getBirthdayInfoRequestEndText());
        message.setText(messageText);
        emailSender.send(message);
    }

    public void sendEmailToUsers(List<User> recipients, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        recipients.forEach(recipient -> {
            message.setTo(recipient.getContact().getEmail());
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        });
    }

    public void sendSetRequestExecutorEmail(Request request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getExecutor().getContact().getEmail());
        message.setSubject(mailConfig.getSetRequestExecutorSubject());
        message.setText(String.format(mailConfig.getSetRequestExecutorText(),
                request.getExecutor().getSurname(), request.getExecutor().getName(), request.getTitle()));
        emailSender.send(message);
    }
}
