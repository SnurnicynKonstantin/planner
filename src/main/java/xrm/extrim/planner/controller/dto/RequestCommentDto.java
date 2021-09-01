package xrm.extrim.planner.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestCommentDto {
    private Long id;
    private LocalDateTime createDate;
    private String text;
    private UserDto author;
}
