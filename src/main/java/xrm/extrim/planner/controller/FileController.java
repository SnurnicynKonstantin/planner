package xrm.extrim.planner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xrm.extrim.planner.controller.dto.FileDto;
import xrm.extrim.planner.controller.dto.FileFilterTypeDto;
import xrm.extrim.planner.controller.dto.FileTypeDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.File;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.FileFilterType;
import xrm.extrim.planner.enums.FileType;
import xrm.extrim.planner.mapper.FileMapper;
import xrm.extrim.planner.mapper.UserMapper;
import xrm.extrim.planner.service.FileService;
import xrm.extrim.planner.service.UserService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/file")
public class FileController {
    private final static String FILE_ID = "file_id";

    private final FileService fileService;
    private final UserService userService;
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    @Autowired
    public FileController(FileService fileService, UserService userService,
                          FileMapper fileMapper, UserMapper userMapper) {
        this.fileService = fileService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    @PostMapping("upload")
    @ApiOperation("Upload file")
    public FileDto uploadFile(@ApiParam("File") @RequestParam("file") MultipartFile multipartFile,
                              @ApiParam("Comment") @RequestParam(value = "comment", required = false) String comment,
                              @ApiParam("File type") @RequestParam("file_type") FileType fileType) {
        File file = fileService.uploadFile(multipartFile, comment, fileType);
        return fileMapper.FileToFileDto(file);
    }

    @GetMapping("download")
    @ApiOperation("Download file")
    public ResponseEntity<byte[]> downloadFile(@ApiParam(FILE_ID) @RequestParam Long fileId) {
        File file = fileService.getFileForDownload(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(file.getName()).build().toString());
        return ResponseEntity.ok().headers(httpHeaders).body(file.getBlob());
    }

    @PutMapping("{file_id}")
    @ApiOperation("Change comment or list attached users")
    public void editFile(@ApiParam(FILE_ID) @PathVariable(FILE_ID) Long fileId,
                         @ApiParam("New file comment") @RequestParam("file_comment") String fileComment,
                         @ApiParam("New lis attached users") @RequestBody List<UserDto> attachedUsers) {
        List<User> users = userService.validateUsers(attachedUsers);
        fileService.editFile(fileId, fileComment, users);
    }

    @PostMapping("add-user")
    @ApiOperation("Attach user to file")
    public void addUserToFile(@ApiParam("User id") @RequestParam("user_id") Long userId,
                              @ApiParam(FILE_ID) @RequestParam(FILE_ID) Long fileId) {
        fileService.addUserToFile(userId, fileId);
    }

    @PostMapping("add-list-users")
    @ApiOperation("Attach list users to file")
    public void addListUsersToFile(@ApiParam(FILE_ID) @RequestParam(FILE_ID) Long fileId,
                                   @ApiParam("Attached users") @RequestBody(required = false) List<UserDto> attachedUsers) {
        List<User> users = userService.validateUsers(attachedUsers);
        fileService.addListUsersToFile(fileId, users);
    }

    @DeleteMapping("delete")
    @ApiOperation("Delete file")
    public void deleteFile(@ApiParam(FILE_ID) @RequestParam Long fileId) {
        fileService.deleteFile(fileId);
    }

    @GetMapping("get-all")
    @ApiOperation("Get all available files")
    public List<FileDto> getAvailableFiles() {
        List<File> files = fileService.getAvailableFiles();
        return fileMapper.listFileToFileDto(files);
    }

    @GetMapping("filter")
    @ApiOperation("Filter files")
    public List<FileDto> filterFiles(@ApiParam("Filter type") @RequestParam FileFilterType filterType) {
        List<File> files = fileService.filterFiles(filterType);
        return fileMapper.listFileToFileDto(files);
    }

    @GetMapping("filter/all-types")
    @ApiOperation("Get all file filter types")
    public List<FileFilterTypeDto> getAllFileTypeFilter() {
        return fileService.getAllFilterTypes();
    }

    @GetMapping("types")
    @ApiOperation("Get all file types")
    public List<FileTypeDto> getAllFileTypes() {
        return fileService.getAllFileTypes();
    }

    @GetMapping("{file_id}/all-attached")
    @ApiOperation("Get all attached users")
    public List<UserDto> getAllAttachedUsers(@PathVariable(FILE_ID) Long id) {
        List<User> users = fileService.getAllAttachedUsers(id);
        return userMapper.userToUserDto(users);
    }
}
