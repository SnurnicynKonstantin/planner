package xrm.extrim.planner.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xrm.extrim.planner.controller.dto.PlannerExceptionDto;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.ForbiddenException;
import xrm.extrim.planner.exception.PlannerException;

@ControllerAdvice(basePackages = "xrm.extrim.planner.controller")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class PlannerExceptionHandler {

    @ExceptionHandler(PlannerException.class)
    public ResponseEntity<PlannerExceptionDto> handleException(PlannerException ex) {
        log.error(ex.getPlannerMessage(), ex);
        PlannerExceptionDto plannerExceptionDto = new PlannerExceptionDto();
        plannerExceptionDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(plannerExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<PlannerExceptionDto> handleException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        PlannerExceptionDto plannerExceptionDto = new PlannerExceptionDto();
        plannerExceptionDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(plannerExceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<PlannerExceptionDto> handleException(ForbiddenException ex){
        log.error(ex.getMessage(), ex);
        PlannerExceptionDto plannerExceptionDto= new PlannerExceptionDto();
        plannerExceptionDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(plannerExceptionDto, HttpStatus.FORBIDDEN);
    }
}
