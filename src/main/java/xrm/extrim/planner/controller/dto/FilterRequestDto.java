package xrm.extrim.planner.controller.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.enums.RequestStatus;

@Data
public class FilterRequestDto {
    private String title;
    private RequestCategory category;
    private UserDto executor;
    private boolean noExecutor;
    private String orderBy;
    private Sort.Direction direction;
    private RequestStatus status;
}
