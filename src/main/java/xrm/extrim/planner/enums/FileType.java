package xrm.extrim.planner.enums;

import java.util.Arrays;

public enum FileType {
    SUMMARY(1, "Резюме"),
    IMAGE(2, "Изображение"),
    UNDEFINED(3, "Не определено");

    private final short code;
    private final String description;

    FileType(Integer code, String description) {
        this.code = code.shortValue();
        this.description = description;
    }

    public short getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static FileType getByCode(Short code) {
        if(code == null) {
            return UNDEFINED;
        }
        return Arrays.stream(values())
                .filter(value -> value.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(Exception.WRONG_ENUM_CODE.getDescription(), code)));
    }
}
