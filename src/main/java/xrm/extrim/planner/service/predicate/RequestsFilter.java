package xrm.extrim.planner.service.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sun.istack.NotNull;
import xrm.extrim.planner.controller.dto.FilterRequestDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.RequestStatus;

import static xrm.extrim.planner.domain.QRequest.request;

public final class RequestsFilter {
    private RequestsFilter() {
    }

    public static Predicate getRequestFilterPredicate(@NotNull FilterRequestDto filterDto, @NotNull User currentUser) {
        BooleanBuilder builder = new BooleanBuilder();
        addFindByTitle(builder, filterDto.getTitle());
        addFindByCategory(builder, filterDto.getCategory());
        addFindByExecutor(builder, filterDto.isNoExecutor(), filterDto.getExecutor());
        addFindByStatus(builder,filterDto.getStatus());
        addAccessFilter(builder, currentUser);
        return builder;
    }

    private static void addFindByTitle(BooleanBuilder builder, String title) {
        if(title == null) {
            return;
        }

        String titleLike = "%" + title + "%";
        builder.and(request.title.like(titleLike));
    }

    private static void addFindByCategory(BooleanBuilder builder, RequestCategory category) {
        if (category == null) {
            return;
        }

        builder.and(request.category.eq(category));
    }

    private static void addFindByExecutor(BooleanBuilder builder, boolean noExecutor, UserDto executor) {
        if(noExecutor) {
            builder.and(request.executor.isNull());
            return;
        }

        if(executor == null) {
            return;
        }

        builder.and(request.executor.login.eq(executor.getLogin()));
    }

    private static void addFindByStatus(BooleanBuilder builder, RequestStatus status) {
        if(status == null) {
            return;
        }

        builder.and(request.status.eq(status));
    }

    private static void addAccessFilter(BooleanBuilder builder, User currentUser) {
        if(!currentUser.getRole().isRequestOperations()) {
            builder.and(request.creator.eq(currentUser));
        }
    }
}
