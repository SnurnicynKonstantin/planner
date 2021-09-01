package xrm.extrim.planner.common;

import xrm.extrim.planner.domain.Group;

@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class GroupTestData {
    private GroupTestData() {
    }

    public static Group getGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Name");
        return group;
    }

    public static Group getGroup(Long id, String name) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        return group;
    }
}
