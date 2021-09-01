package xrm.extrim.planner.enums;

public enum FileFilterType {
    UPLOADED("Загруженные"),
    ATTACHED("Назначенные");

    public final String description;

    FileFilterType(String description) {
        this.description = description;
    }
}
