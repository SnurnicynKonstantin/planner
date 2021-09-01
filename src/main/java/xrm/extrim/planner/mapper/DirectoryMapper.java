package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.enums.RequestStatus;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DirectoryMapper {
    public DirectoryDto categoryToDirectoryDto(RequestCategory category) {
        if(category == null) {
            return null;
        }

        DirectoryDto result = new DirectoryDto();
        result.setName(category.getName());
        result.setDescription(category.getDescription());
        result.setId(category.getId());
        return result;
    }

    public abstract List<DirectoryDto> listCategoryToListDirectoryDto(List<RequestCategory> categories);

    public RequestCategory directoryDtoToRequestCategory(DirectoryDto dto) {
        if (dto == null) {
            return null;
        }
        RequestCategory requestCategory = new RequestCategory();
        requestCategory.setName(dto.getName());
        requestCategory.setDescription(dto.getDescription());
        return requestCategory;
    }

    public DirectoryDto statusToDirectoryDto(RequestStatus status) {
        if(status == null) {
            return null;
        }

        DirectoryDto dto = new DirectoryDto();
        dto.setName(status.name());
        dto.setDescription(status.getDescription());
        return dto;
    }

    public abstract List<DirectoryDto> listStatusesToListDirectoryDto(RequestStatus[] statuses);

    public RequestStatus directoryDtoToStatus(DirectoryDto dto) {
        if(dto == null) {
            return null;
        }

        return RequestStatus.getByName(dto.getName());
    }
}
