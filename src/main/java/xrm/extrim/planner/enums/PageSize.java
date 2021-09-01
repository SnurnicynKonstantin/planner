package xrm.extrim.planner.enums;

public enum PageSize {
    SMALL(10),
    MIDDLE(50),
    LARGE(100);

    public final int size;

    PageSize(int count) {
        this.size = count;
    }
}
