package xrm.extrim.planner.service;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.NotificationMessages;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.FilterRequestDto;
import xrm.extrim.planner.controller.dto.RequestDto;
import xrm.extrim.planner.domain.Request;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.domain.RequestComment;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.enums.RequestStatus;
import xrm.extrim.planner.exception.ForbiddenException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.RequestMapper;
import xrm.extrim.planner.repository.RequestCategoryRepository;
import xrm.extrim.planner.repository.RequestCommentRepository;
import xrm.extrim.planner.repository.RequestRepository;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.service.predicate.RequestsFilter;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestCommentRepository requestCommentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final MailService mailService;
    private final RequestNotificationService requestNotificationService;
    private final RequestCategoryRepository requestCategoryRepository;
    private final RequestMapper requestMapper;

    public RequestService(RequestRepository requestRepository, RequestCommentRepository requestCommentRepository,
                          UserRepository userRepository, NotificationService notificationService, MailService mailService,
                          RequestCategoryRepository requestCategoryRepository,
                          RequestMapper requestMapper, RequestNotificationService requestNotificationService) {
        this.requestRepository = requestRepository;
        this.requestCommentRepository = requestCommentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mailService = mailService;
        this.requestNotificationService = requestNotificationService;
        this.requestCategoryRepository = requestCategoryRepository;
        this.requestMapper = requestMapper;
    }

    public List<RequestCategory> getAllRequestCategories() {
        return requestCategoryRepository.findAll();
    }

    public List<Request> getAll() {
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        if(currentUser.getRole().isRequestOperations()) {
            return requestRepository.findAll();
        }
        return requestRepository.findAllByCreator(currentUser);
    }

    public Request getById(Long id) {
        Request request = requestRepository.findById(id)
            .orElseThrow(() -> {
                throw new PlannerException(String.format(Exception.REQUEST_NOT_FOUND.getDescription(), id));
            });

        checkAccess(request);
        return request;
    }

    private void checkAccess(Request request) {
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        if(!request.getCreator().equals(currentUser) && !currentUser.getRole().isRequestOperations()) {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    public Request createRequest(RequestDto requestDto) {
        Request request = requestMapper.requestToRequestDto(requestDto);

        request.setCreateDate(LocalDateTime.now());
        request.setUpdateDate(LocalDateTime.now());
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        request.setCreator(currentUser);
        request.setStatus(RequestStatus.OPEN);
        request.setCategory(requestCategoryRepository.findByName(requestDto.getCategory().getName()));
        Request result = requestRepository.save(request);
        requestNotificationService.sendRequestCreatedEmail(result);
        return result;
    }

    public Request updateRequest(Request request, Long id) {
        Request requestDb = getById(id);
        BeanUtils.copyProperties(request, requestDb, "id", "createDate", "creator", "executor", "category");
        requestDb.setUpdateDate(LocalDateTime.now());
        return requestRepository.save(requestDb);
    }

    public void delete(Long id) {
        Request request = getById(id);
        requestRepository.delete(request);
    }

    public Request setComment(RequestComment comment, Long requestId) {
        Request request = getById(requestId);
        checkAccess(request);
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        comment.setAuthor(currentUser);
        comment.setRequest(request);
        comment.setCreateDate(LocalDateTime.now());
        RequestComment commentDb = requestCommentRepository.save(comment);
        request.getComments().add(commentDb);
        request.setUpdateDate(LocalDateTime.now());
        requestNotificationService.sendNotificationAboutComment(currentUser, request);
        return requestRepository.save(request);
    }

    public Request setExecutor(Long requestId, Long executorId) {
        Request request = getById(requestId);
        User user = getUserById(executorId);
        request.setExecutor(user);
        request.setUpdateDate(LocalDateTime.now());
        notificationService.sendNotification(user.getId(), String.format(NotificationMessages.REQUEST_EXECUTOR.getMessage(), request.getTitle()));
        mailService.sendSetRequestExecutorEmail(request);
        return requestRepository.save(request);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new PlannerException(String.format(Exception.USER_NOT_FOUND.getDescription(), userId));
        });
    }

    public Request updateStatus(Long id, RequestStatus status) {
        Request request = getById(id);
        request.setStatus(status);
        request.setUpdateDate(LocalDateTime.now());
        this.requestNotificationService.sendRequestUpdateStatusEmail(request);
        return requestRepository.save(request);
    }

    public List<RequestComment> getAllCommentsRequest(Long requestId) {
        Request request = getById(requestId);
        checkAccess(request);
        return requestCommentRepository.findAllByRequest_Id(requestId, Sort.by(Sort.Direction.DESC, "createDate"));
    }

    public Page<Request> filter(FilterRequestDto filterDto, int size, int page) {
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        Predicate filterPredicate = RequestsFilter.getRequestFilterPredicate(filterDto, currentUser);
        Pageable pageable = PageRequest.of(page, size, filterDto.getDirection(), filterDto.getOrderBy());
        return requestRepository.findAllByPredicate(filterPredicate, pageable);
    }
}
