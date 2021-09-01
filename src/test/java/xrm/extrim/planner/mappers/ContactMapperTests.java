package xrm.extrim.planner.mappers;

import xrm.extrim.planner.markers.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.controller.dto.ContactDto;
import xrm.extrim.planner.domain.Contact;


@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class ContactMapperTests extends PlannerTestBase {

    @Test
    public void ContactToContactDto() {
        Contact contact = new Contact();
        contact.setId(2L);
        contact.setEmail("12345");
        contact.setPhoneNumber("98765");

        ContactDto contactDto = contactMapper.contactToContactDto(contact);

        assert contactDto.getEmail().equals(contact.getEmail());
        assert contactDto.getPhoneNumber().equals(contact.getPhoneNumber());
    }

    @Test
    public void ContactDtoToContact() {
        ContactDto contactDto = new ContactDto();
        contactDto.setEmail("12345");
        contactDto.setPhoneNumber("98765");

        Contact contact = contactMapper.contactDtoToContact(contactDto);

        assert contact.getEmail().equals(contactDto.getEmail());
        assert contact.getPhoneNumber().equals(contactDto.getPhoneNumber());
    }
}
