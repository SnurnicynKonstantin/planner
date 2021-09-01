package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.FileFilterTypeDto;
import xrm.extrim.planner.controller.dto.FileTypeDto;
import xrm.extrim.planner.domain.File;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.enums.FileFilterType;
import xrm.extrim.planner.enums.FileType;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.FileRepository;
import xrm.extrim.planner.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public File uploadFile(MultipartFile multipartFile, String comment, FileType fileType) {
        try {
            return createFile(multipartFile, comment, fileType);
        } catch (IOException e) {
            throw new PlannerException(e, Exception.FILE_UPLOAD_ERROR.getDescription());
        }
    }

    private File createFile(MultipartFile multipartFile, String comment, FileType fileType) throws IOException {
        User user = UserAuthenticationHelper.getAuthenticatedUserData();
        File file = new File();
        file.setUploadDate(LocalDate.now());
        file.setName(multipartFile.getOriginalFilename());

        if (fileRepository.existsByNameAndUploader_id(file.getName(), user.getId())) {
            throw new PlannerException(String.format(Exception.FILE_ALREADY_EXISTS.getDescription(), file.getName()));
        }

        file.setComment(comment);
        if (fileType != null) {
            file.setFileType(fileType);
        }

        file.setBlob(multipartFile.getBytes());
        file.setUploader(user);
        return fileRepository.save(file);
    }

    public void editFile(Long fileId, String fileComment, List<User> attachedUsers) {
        File file = getFileById(fileId);
        checkAccessDenied(file);

        if (fileComment != null) {
            file.setComment(fileComment);
        }
        file.setAttachedUsers(attachedUsers);
        fileRepository.save(file);
    }

    public File getFileForDownload(Long fileId) {
        File file = getFileById(fileId);
        checkAccessDenied(file);

        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        if (!file.getUploader().equals(currentUser) &&
                file.getAttachedUsers().stream().noneMatch(user -> user.equals(currentUser))) {
            throw new PlannerException(String.format(Exception.FILE_ACCESS_DENIED.getDescription(), fileId));
        }

        return file;
    }

    public void addUserToFile(Long userId, Long fileId) {
        File file = getFileById(fileId);
        checkAccessDenied(file);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new PlannerException(String.format(Exception.USER_NOT_FOUND.getDescription(), userId));
        }

        file.getAttachedUsers().add(user);
        fileRepository.save(file);
    }

    public void addListUsersToFile(Long fileId, List<User> users) {
        File file = getFileById(fileId);
        checkAccessDenied(file);

        file.setAttachedUsers(users);
        fileRepository.save(file);
    }

    public void deleteFile(Long fileId) {
        File file = getFileById(fileId);
        checkAccessDenied(file);
        fileRepository.deleteById(fileId);
    }

    private File getFileById(Long fileId) {
        File file = fileRepository.findById(fileId).orElse(null);
        if (file == null) {
            throw new PlannerException(String.format(Exception.FILE_NOT_FOUND.getDescription(), fileId));
        }
        return file;
    }

    private void checkAccessDenied(File file) {
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        boolean userIsAttached = file.getAttachedUsers().stream().anyMatch(user -> user.equals(currentUser));
        if (!file.getUploader().equals(currentUser) && !userIsAttached ) {
            throw new PlannerException(String.format(Exception.FILE_ACCESS_DENIED.getDescription(), file.getId()));
        }
    }

    public List<File> getAvailableFiles() {
        User user = userRepository.findByLogin(UserAuthenticationHelper.getAuthenticatedUserData().getLogin());
        List<File> files = user.getAttachedFiles();
        if (files.isEmpty()) {
            throw new PlannerException(Exception.NO_AVAILABLE_FILES.getDescription());
        }
        return files;
    }

    public List<File> filterFiles(FileFilterType filterType) {
        User user = UserAuthenticationHelper.getAuthenticatedUserData();
        return caseFilterFiles(user, filterType);
    }

    private List<File> caseFilterFiles(User user, FileFilterType filterType) {
        if(user == null) {
            throw new PlannerException(Exception.USER_NOT_FOUND.getDescription());
        }

        switch (filterType) {
            case UPLOADED:
                return fileRepository.findAllByUploader_Id(user.getId());
            case ATTACHED:
                return fileRepository.findAllByAttachedUsers_idIn(Collections.singletonList(user.getId()));
            default: throw new IllegalArgumentException();
        }
    }

    public List<FileFilterTypeDto> getAllFilterTypes() {
        return Arrays.stream(FileFilterType.values()).map(filterType -> {
            FileFilterTypeDto filterTypeDto = new FileFilterTypeDto();
            filterTypeDto.setName(filterType.name());
            filterTypeDto.setDescription(filterType.description);
            return filterTypeDto;
        }).collect(Collectors.toList());
    }

    public List<FileTypeDto> getAllFileTypes() {
        return Arrays.stream(FileType.values()).map(type -> {
            FileTypeDto file = new FileTypeDto();
            file.setName(type.name());
            file.setCode(type.getCode());
            file.setDescription(type.getDescription());
            return file;
        }).collect(Collectors.toList());
    }

    public List<User> getAllAttachedUsers(Long fileId) {
        File file = getFileById(fileId);
        checkAccessDenied(file);
        return file.getAttachedUsers();
    }
}
