package xrm.extrim.planner.common;

import org.springframework.security.core.context.SecurityContextHolder;
import xrm.extrim.planner.domain.User;

public final class UserAuthenticationHelper {
    private UserAuthenticationHelper() {
    }

    public static User getAuthenticatedUserData() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
