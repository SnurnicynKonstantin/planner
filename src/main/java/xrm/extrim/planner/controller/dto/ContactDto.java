package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xrm.extrim.planner.domain.Messengers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private String phoneNumber;
    private String email;
    private String office;
    private Messengers messengers;
}
