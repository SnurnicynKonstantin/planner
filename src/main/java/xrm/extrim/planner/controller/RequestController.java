package xrm.extrim.planner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.controller.dto.FilterRequestDto;
import xrm.extrim.planner.controller.dto.RequestCommentDto;
import xrm.extrim.planner.controller.dto.RequestDto;
import xrm.extrim.planner.domain.Request;
import xrm.extrim.planner.domain.RequestComment;
import xrm.extrim.planner.enums.PageSize;
import xrm.extrim.planner.enums.RequestStatus;
import xrm.extrim.planner.mapper.DirectoryMapper;
import xrm.extrim.planner.mapper.RequestCommentMapper;
import xrm.extrim.planner.mapper.RequestMapper;
import xrm.extrim.planner.service.RequestService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/request")
public class RequestController {
    private final RequestMapper requestMapper;
    private final RequestService requestService;
    private final RequestCommentMapper requestCommentMapper;
    private final DirectoryMapper directoryMapper;
    private final static String REQUEST_OPERATIONS = "hasRole('requestOperations')";
    private final static String REQUEST_ID = "Request id";

    public RequestController(RequestMapper requestMapper, RequestService requestService,
                             RequestCommentMapper requestCommentMapper, DirectoryMapper directoryMapper){
        this.requestMapper = requestMapper;
        this.requestService = requestService;
        this.requestCommentMapper = requestCommentMapper;
        this.directoryMapper = directoryMapper;
    }

    @GetMapping
    @ApiOperation("Get all requests")
    public List<RequestDto> getAllRequests() {
        List<Request> requests = requestService.getAll();
        return requestMapper.listRequestsToListRequestDto(requests);
    }

    @GetMapping("{id}")
    @ApiOperation("Get request by id")
    public RequestDto getRequestById(@ApiParam(REQUEST_ID) @PathVariable("id") Long id) {
        return requestMapper.requestDtoToRequest(requestService.getById(id));
    }

    @PostMapping
    @ApiOperation("Create request")
    public RequestDto createRequest(@ApiParam(REQUEST_ID) @RequestBody RequestDto requestDto) {
        return requestMapper.requestDtoToRequest(requestService.createRequest(requestDto));
    }

    @PutMapping("{id}")
    @ApiOperation("Update request")
    public RequestDto updateRequest(@ApiParam(REQUEST_ID) @PathVariable("id") Long id,
                                    @ApiParam("Request data") @RequestBody RequestDto requestDto) {
        Request result = requestService.updateRequest(requestMapper.requestToRequestDto(requestDto), id);
        return requestMapper.requestDtoToRequest(result);
    }

    @PreAuthorize(REQUEST_OPERATIONS)
    @DeleteMapping("{id}")
    @ApiOperation("Delete request")
    public void deleteRequest(@ApiParam(REQUEST_ID) @PathVariable("id") Long id) {
        requestService.delete(id);
    }

    @PostMapping("{id}/comment")
    @ApiOperation("Add comment")
    public void addComment(@ApiParam(REQUEST_ID) @PathVariable("id") Long id,
                           @ApiParam("Request data") @RequestBody RequestCommentDto commentDto) {
        requestService.setComment(requestCommentMapper.requestCommentDtoToRequestComment(commentDto), id);
    }

    @GetMapping("{id}/comment")
    @ApiOperation("Get all comments")
    public List<RequestCommentDto> getAllRequestComments(@ApiParam(REQUEST_ID) @PathVariable("id") Long id) {
        List<RequestComment> comments = requestService.getAllCommentsRequest(id);
        return requestCommentMapper.listRequestCommentToListDto(comments);
    }

    @PreAuthorize(REQUEST_OPERATIONS)
    @PutMapping("{request_id}/executor/{executor_id}")
    @ApiOperation("Set request executor")
    public RequestDto setExecutor(@ApiParam(REQUEST_ID) @PathVariable("request_id") Long requestId,
                                  @ApiParam("User executor id") @PathVariable("executor_id") Long executorId) {
        Request result = requestService.setExecutor(requestId, executorId);
        return requestMapper.requestDtoToRequest(result);
    }

    @PreAuthorize(REQUEST_OPERATIONS)
    @PutMapping("{id}/status")
    @ApiOperation("Change status request")
    public RequestDto changeStatus(@ApiParam(REQUEST_ID) @PathVariable("id") Long id,
                                   @ApiParam("New status") @RequestParam("status")RequestStatus status) {
        Request result = requestService.updateStatus(id, status);
        return requestMapper.requestDtoToRequest(result);
    }


    @PostMapping("filter")
    @ApiOperation("Filter requests")
    public Page<RequestDto> filter(@ApiParam("Filter data") @RequestBody FilterRequestDto filterDto,
                                   @ApiParam("Page size")  @RequestParam("size") PageSize size,
                                   @ApiParam("Page number") @RequestParam("page") Integer number) {
        Page<Request> result = requestService.filter(filterDto, size.size, number);
        return result.map(requestMapper::requestDtoToRequest);
    }

    @GetMapping("categories")
    @ApiOperation("Get all request categories")
    public List<DirectoryDto> getAllRequestCategories() {
        return directoryMapper.listCategoryToListDirectoryDto(requestService.getAllRequestCategories());
    }

    @GetMapping("statuses")
    @ApiOperation("Get all request statuses")
    public List<DirectoryDto> getAllRequestStatuses() {
        return directoryMapper.listStatusesToListDirectoryDto(RequestStatus.values());
    }
}
