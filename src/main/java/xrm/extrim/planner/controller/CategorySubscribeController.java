package xrm.extrim.planner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.mapper.UserMapper;
import xrm.extrim.planner.service.CategorySubscribersService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/category-subscribe-user")
public class CategorySubscribeController {
    private final CategorySubscribersService categorySubscribersService;
    private final UserMapper userMapper;

    public CategorySubscribeController(CategorySubscribersService categorySubscribersService, UserMapper userMapper) {
        this.categorySubscribersService = categorySubscribersService;
        this.userMapper = userMapper;
    }

    @GetMapping("{id}")
    @ApiOperation("Get user categories")
    public List<RequestCategory> getUserCategories(@ApiParam("User id") @PathVariable("id") Long userId){
        return categorySubscribersService.getUserCategories(userId);
    }

    @GetMapping("/category/{id}/users")
    @ApiOperation("Get all users, who sub to category")
    public List<UserDto> getAllUsersFromCategory(@ApiParam("Category id") @PathVariable("id") Long categoryId){
        return userMapper.userToUserDto(categorySubscribersService.getAllUsersFromCategory(categoryId));
    }

    @PostMapping("{id}/category")
    @ApiOperation("Subscribe user to category")
    public void subscribeUserToCategory(@ApiParam("User id") @PathVariable("id") Long userId,
                       @ApiParam("Category") @RequestBody RequestCategory requestCategory) {
        categorySubscribersService.subscribeUserToCategory(userId, requestCategory);
    }

    @DeleteMapping("{id}/category")
    @ApiOperation("Unsubscribe user from category")
    public void unsubscribeUserFromCategory(@ApiParam("User id") @PathVariable("id") Long userId,
                                            @ApiParam("Category") @RequestBody RequestCategory requestCategory) {
        categorySubscribersService.unsubscribeUserFromCategory(userId, requestCategory);
    }
}
