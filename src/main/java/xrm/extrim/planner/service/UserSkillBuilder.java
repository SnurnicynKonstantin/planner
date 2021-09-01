package xrm.extrim.planner.service;

import lombok.NoArgsConstructor;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;

@NoArgsConstructor
public class UserSkillBuilder {
    private Long id;
    private Integer rate;
    private boolean isConfirmed;
    private User user;
    private Skill skill;
    private Integer version;
    private String comment;
    private boolean isDeleted;

    public UserSkillBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserSkillBuilder setRate(Integer rate) {
        this.rate = rate;
        return this;
    }

    public UserSkillBuilder setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
        return this;
    }

    public UserSkillBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public UserSkillBuilder setSkill(Skill skill) {
        this.skill = skill;
        return this;
    }

    public UserSkillBuilder setVersion(int version) {
        this.version = version;
        return this;
    }

    public UserSkillBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public UserSkillBuilder setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public UserSkill build() {
        UserSkill result = new UserSkill();
        result.setId(this.id);
        result.setRate(this.rate);
        result.setConfirmed(this.isConfirmed);
        result.setUser(this.user);
        result.setVersion(this.version);
        result.setSkill(this.skill);
        result.setComment(this.comment);
        result.setDeleted(this.isDeleted);
        return result;
    }
}
