package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import xrm.extrim.planner.controller.dto.ContactDto;
import xrm.extrim.planner.domain.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    Contact contactDtoToContact(ContactDto contactDto);
    ContactDto contactToContactDto(Contact contact);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Contact updateContact(@MappingTarget Contact userContact, Contact newContact);
}
