package xrm.extrim.planner.service.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import xrm.extrim.planner.controller.dto.FilterScopeUserDto;
import xrm.extrim.planner.controller.dto.UserSkillDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static xrm.extrim.planner.view.QUserView.userView;
import static xrm.extrim.planner.view.QUserSkillView.userSkillView;
import static xrm.extrim.planner.view.QVacationView.vacationView;

public final class UserFilter {
    private UserFilter() {
    }

    public static Predicate getUserFilterPredicate(FilterScopeUserDto filterScopeUserDto) {
        BooleanBuilder builder = new BooleanBuilder();
        addFindByInitials(builder, filterScopeUserDto.getInitials());
        addFindByDepartment(builder, filterScopeUserDto.getDepartment());
        addFindByPosition(builder, filterScopeUserDto.getPosition());
        addFindByIsOnVacation(builder, filterScopeUserDto.getIsOnVacation());
        addFindBySkills(builder, filterScopeUserDto.getSkills());
        addFindByPersonalInformation(builder, filterScopeUserDto.getPersonalInformation());
        return builder;
    }

    public static Predicate getUserFilterByVacation(LocalDate startMoment, LocalDate endMoment) {
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression predicate = vacationView.dateStart.goe(startMoment).and(vacationView.dateStart.loe(endMoment))
                .or(vacationView.dateEnd.goe(startMoment).and(vacationView.dateEnd.loe(endMoment)));

        JPQLQuery<Long> query = JPAExpressions.select(vacationView.userId)
                .from(vacationView)
                .where(predicate)
                .groupBy(vacationView.userId);
        return builder.and(userView.id.in(query));
    }

    private static void addFindByInitials(BooleanBuilder builder, String initials) {
        if(initials == null) {
            return;
        }

        String initialsLike = "%" + initials + "%";
        builder.and(userView.userName.like(initialsLike).or(userView.userSurname.like(initialsLike)).or(userView.userLogin.like(initialsLike)));
    }

    private static void addFindByPosition(BooleanBuilder builder, String positionDescription) {
        if(positionDescription == null) {
            return;
        }

        builder.and(userView.positionDescription.eq(positionDescription));
    }

    private static void addFindByDepartment(BooleanBuilder builder, String departmentDescription) {
        if(departmentDescription == null) {
            return;
        }

        builder.and(userView.departmentDescription.eq(departmentDescription));
    }

    private static void addFindByPersonalInformation(BooleanBuilder builder, String personalInformation) {
        Optional.ofNullable(personalInformation).ifPresent(info -> {
            String personalInformationLike = "%" + personalInformation + "%";
            builder.and(userView.userPersonalInformation.toLowerCase().like(personalInformationLike.toLowerCase()));
        });
    }

    private static void addFindByIsOnVacation(BooleanBuilder builder, Boolean isOnVacation) {
        if(isOnVacation == null) {
            return;
        }
        if (isOnVacation) {
            builder.and(userView.isOnVacation.isNotNull());
        }
        if (!isOnVacation) {
            builder.and(userView.isOnVacation.isNull());
        }
    }

    private static void addFindBySkills(BooleanBuilder builder,  List<UserSkillDto> skills) {
        if(skills == null || skills.isEmpty()) {
            return;
        }

        BooleanBuilder skillPredicateBuilder = new BooleanBuilder();
        for (UserSkillDto skill : skills) {
            BooleanExpression skillPredicate;
            if(skill.getRate() != null) {
                skillPredicate = userSkillView.skillId.eq(skill.getId())
                        .and(userSkillView.skillRate.eq(skill.getRate()));
            } else {
                skillPredicate = userSkillView.skillId.eq(skill.getId());
            }
            skillPredicateBuilder.or(skillPredicate);
        }

        JPQLQuery<Long> query = JPAExpressions.select(userSkillView.userId)
                        .from(userSkillView)
                        .where(skillPredicateBuilder)
                        .groupBy(userSkillView.userId)
                        .having(userSkillView.userId.count().eq((long) skills.size()));

        builder.and(userView.id.in(query));

    }
}
