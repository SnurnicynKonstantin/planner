package xrm.extrim.planner.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import xrm.extrim.planner.common.WsSender;
import xrm.extrim.planner.mapper.AnnouncementMapper;
import xrm.extrim.planner.mapper.ContactMapper;
import xrm.extrim.planner.mapper.DepartmentMapper;
import xrm.extrim.planner.mapper.FeedbackMapper;
import xrm.extrim.planner.mapper.PositionMapper;
import xrm.extrim.planner.mapper.ProjectHistoryMapper;
import xrm.extrim.planner.mapper.ProjectMapper;
import xrm.extrim.planner.mapper.RateDescriptionMapper;
import xrm.extrim.planner.mapper.RequestCommentMapper;
import xrm.extrim.planner.mapper.RequestMapper;
import xrm.extrim.planner.mapper.SkillMapper;
import xrm.extrim.planner.mapper.TaskMapper;
import xrm.extrim.planner.mapper.UserMapper;
import xrm.extrim.planner.mapper.UserSkillMapper;
import xrm.extrim.planner.mapper.VacationRequestMapper;
import xrm.extrim.planner.repository.AnnouncementRepository;
import xrm.extrim.planner.repository.CategorySubscriberRepository;
import xrm.extrim.planner.repository.ContactRepository;
import xrm.extrim.planner.repository.DepartmentRepository;
import xrm.extrim.planner.repository.FeedbackRepository;
import xrm.extrim.planner.repository.FileRepository;
import xrm.extrim.planner.repository.GroupRepository;
import xrm.extrim.planner.repository.MailIDPRepository;
import xrm.extrim.planner.repository.MailVacationApproverRepository;
import xrm.extrim.planner.repository.NotificationRepository;
import xrm.extrim.planner.repository.PositionRepository;
import xrm.extrim.planner.repository.ProjectHistoryRepository;
import xrm.extrim.planner.repository.ProjectRepository;
import xrm.extrim.planner.repository.RateDescriptionRepository;
import xrm.extrim.planner.repository.RequestCategoryRepository;
import xrm.extrim.planner.repository.RequestCommentRepository;
import xrm.extrim.planner.repository.RequestRepository;
import xrm.extrim.planner.repository.RoleRepository;
import xrm.extrim.planner.repository.SkillRepository;
import xrm.extrim.planner.repository.TaskRepository;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.repository.UserSkillsRepository;
import xrm.extrim.planner.repository.UserViewRepository;
import xrm.extrim.planner.repository.VacationApproverRepository;
import xrm.extrim.planner.repository.VacationRequestRepository;
import xrm.extrim.planner.service.CategorySubscribersService;
import xrm.extrim.planner.service.AnnouncementService;
import xrm.extrim.planner.service.DepartmentService;
import xrm.extrim.planner.service.FeedbackService;
import xrm.extrim.planner.service.FileService;
import xrm.extrim.planner.service.MailService;
import xrm.extrim.planner.service.NotificationService;
import xrm.extrim.planner.service.PositionService;
import xrm.extrim.planner.service.ProjectHistoryService;
import xrm.extrim.planner.service.ProjectService;
import xrm.extrim.planner.service.RequestNotificationService;
import xrm.extrim.planner.service.RequestService;
import xrm.extrim.planner.service.RoleService;
import xrm.extrim.planner.service.SkillService;
import xrm.extrim.planner.service.UserMigrateService;
import xrm.extrim.planner.service.UserService;
import xrm.extrim.planner.service.UserSkillService;
import xrm.extrim.planner.service.VacationService;

@SuppressWarnings({"PMD.TooManyFields"})
@SpringBootTest(classes = PlannerTestConfiguration.class)
public class PlannerTestBase {
    @Autowired
    protected UserService userService;
    @Autowired
    protected ProjectHistoryService projectHistoryService;
    @Autowired
    protected SkillService skillService;
    @Autowired
    protected NotificationService notificationService;
    @Autowired
    protected MailService mailService;
    @Autowired
    protected UserSkillService userSkillService;
    @Autowired
    protected PositionService positionService;
    @Autowired
    protected DepartmentService departmentService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected FileService fileService;
    @Autowired
    protected VacationService vacationService;
    @Autowired
    protected UserMigrateService userMigrateService;
    @Autowired
    protected FeedbackService feedbackService;
    @Autowired
    protected RequestService requestService;
    @Autowired
    protected ProjectService projectService;
    @Autowired
    protected CategorySubscribersService categorySubscribersService;
    @Autowired
    protected RequestNotificationService requestNotificationService;

    @Autowired
    protected AnnouncementService announcementService;

    @MockBean
    protected WsSender wsSender;
    @MockBean
    protected JavaMailSender javaMailSenderMock;
    @MockBean
    protected MailIDPRepository mailIDPRepositoryMock;
    @MockBean
    protected MailConfig mailConfigMock;
    @MockBean
    protected ConfluenceConfig config;
    @MockBean
    protected UserRepository userRepositoryMock;
    @MockBean
    protected UserSkillsRepository userSkillsRepositoryMock;
    @MockBean
    protected SkillRepository skillRepositoryMock;
    @MockBean
    protected ProjectHistoryRepository projectHistoryRepositoryMock;
    @MockBean
    protected TaskRepository taskRepositoryMock;
    @MockBean
    protected GroupRepository groupRepositoryMock;
    @MockBean
    protected RateDescriptionRepository descriptionRepositoryMock;
    @MockBean
    protected ContactRepository contactRepositoryMock;
    @MockBean
    protected NotificationRepository notificationRepositoryMock;
    @MockBean
    protected FileRepository fileRepositoryMock;
    @MockBean
    protected NotificationService notificationServiceMock;
    @MockBean
    protected PositionRepository positionRepository;
    @MockBean
    protected DepartmentRepository departmentRepository;
    @MockBean
    protected RoleRepository roleRepositoryMock;
    @MockBean
    protected VacationRequestRepository vacationRequestRepository;
    @MockBean
    protected VacationApproverRepository vacationApproverRepository;
    @MockBean
    protected UserViewRepository userViewRepository;
    @MockBean
    protected MailVacationApproverRepository mailVacationApproverRepository;
    @MockBean
    protected ProjectRepository projectRepository;
    @MockBean
    protected RequestRepository requestRepository;
    @MockBean
    protected RequestCommentRepository requestCommentRepository;
    @MockBean
    protected CategorySubscriberRepository categorySubscriberRepository;
    @MockBean
    protected AnnouncementRepository announcementRepository;
    @MockBean
    protected RequestCategoryRepository requestCategoryRepository;
    @MockBean
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected TaskMapper taskMapper;
    @Autowired
    protected UserSkillMapper userSkillMapper;
    @Autowired
    protected ContactMapper contactMapper;
    @Autowired
    protected SkillMapper skillMapper ;
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected ProjectHistoryMapper projectHistoryMapper;
    @Autowired
    protected RateDescriptionMapper rateDescriptionMapper;
    @Autowired
    protected PositionMapper positionMapper;
    @Autowired
    protected DepartmentMapper departmentMapper;
    @Autowired
    protected VacationRequestMapper vacationRequestMapper;
    @Autowired
    protected ProjectMapper projectMapper;
    @Autowired
    protected RequestMapper requestMapper;
    @Autowired
    protected RequestCommentMapper requestCommentMapper;
    @Autowired
    protected FeedbackMapper feedbackMapper;
    @Autowired
    protected AnnouncementMapper announcementMapper;
}
