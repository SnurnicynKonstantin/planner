package xrm.extrim.planner.enums;

import xrm.extrim.planner.exception.PlannerException;

import java.util.Arrays;

public enum RequestStatus {
    OPEN("Открыта"),
    IN_PROGRESS("В процессе"),
    CLOSED("Закрыта");


    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static RequestStatus getByName(String name) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new PlannerException(String.format(Exception.WRONG_ENUM_CODE.getDescription(), name)));
    }

}
