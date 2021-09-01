package xrm.extrim.planner.service;

import org.springframework.stereotype.Service;
import xrm.extrim.planner.domain.CategorySubscriber;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.CategorySubscriberRepository;
import xrm.extrim.planner.repository.RequestCategoryRepository;
import xrm.extrim.planner.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorySubscribersService {

    private final CategorySubscriberRepository categorySubscriberRepository;
    private final UserRepository userRepository;
    private final RequestCategoryRepository requestCategoryRepository;

    public CategorySubscribersService(CategorySubscriberRepository categorySubscriberRepository, UserRepository userRepository,
                                      RequestCategoryRepository requestCategoryRepository) {
        this.categorySubscriberRepository = categorySubscriberRepository;
        this.userRepository = userRepository;
        this.requestCategoryRepository = requestCategoryRepository;
    }

    public void subscribeUserToCategory(Long subscriberId, RequestCategory requestCategory) {
        User user = getUser(subscriberId);
        CategorySubscriber categorySubscriber = getCategorySubscriber(subscriberId, requestCategory);

        if(categorySubscriber == null){
            this.categorySubscriberRepository.save(new CategorySubscriber(user, requestCategory));
        }
    }

    public void unsubscribeUserFromCategory(Long subscriberId, RequestCategory requestCategory) {
        CategorySubscriber categorySubscriber = getCategorySubscriber(subscriberId, requestCategory);

        if(categorySubscriber == null){
            throw new PlannerException(Exception.CATEGORY_SUBSCRIBER_NOT_FOUND.getDescription());
        }

        this.categorySubscriberRepository.delete(categorySubscriber);
    }

    public CategorySubscriber getCategorySubscriber(Long subscriberId, RequestCategory requestCategory){
        User user = getUser(subscriberId);
        return this.categorySubscriberRepository
                .findBySubscriberAndRequestCategory(user, requestCategory);
    }

    public User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), userId)));
    }

    public List<RequestCategory> getUserCategories(Long userId){
        User subscriber = getUser(userId);
        return this.categorySubscriberRepository.findAllBySubscriber(subscriber).stream()
                .map(CategorySubscriber::getRequestCategory)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsersFromCategory(Long categoryId){
        return this.categorySubscriberRepository
                .findAllByRequestCategory(requestCategoryRepository.getById(categoryId))
                .stream()
                .map(CategorySubscriber::getSubscriber)
                .collect(Collectors.toList());
    }
}
