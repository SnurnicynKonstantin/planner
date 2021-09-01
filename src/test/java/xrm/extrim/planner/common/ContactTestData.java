package xrm.extrim.planner.common;

import xrm.extrim.planner.controller.dto.ContactDto;
import xrm.extrim.planner.domain.Messengers;

@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class ContactTestData {
    private static ContactDto contactDto1 = new ContactDto("+7 (912) 777-88-99", "contact1@yandex.ru","", new Messengers());

    private static ContactDto contactDto2 = new ContactDto("+7 (912) 666-77-99", "contact2@yandex.ru","", new Messengers());

    private ContactTestData() {
    }

    public static ContactDto getContactDto1() {
        return contactDto1;
    }

    public static ContactDto getContactDto2() {
        return contactDto2;
    }
}
