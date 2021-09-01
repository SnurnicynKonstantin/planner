package xrm.extrim.planner.enums;

import xrm.extrim.planner.exception.PlannerException;

import java.util.Arrays;

public enum TaskType {
    NOT_INCREMENTED(1),
    INCREMENTED(2);

    private final short code;

    TaskType(Integer code) {
        this.code = code.shortValue();
    }

    public short getCode() {
        return code;
    }

    public static TaskType getByCode(short code) {
        return Arrays.stream(values())
                .filter(value -> value.code == code)
                .findFirst()
                .orElseThrow(() -> new PlannerException(String.format(Exception.WRONG_ENUM_CODE.getDescription(), code)));
    }
}
