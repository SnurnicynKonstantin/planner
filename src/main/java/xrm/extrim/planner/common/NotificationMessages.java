package xrm.extrim.planner.common;

public enum NotificationMessages {
    CONTACT_UPDATE("Ваши контактные данные изменились!"),
    POSITION_UPDATE("Ваша позиция поменялась, теперь вы - %s!"),
    SKILL_APPROVE("Ваш скил %s был оценен!"),
    WHOLE_USER_DATA_UPDATE("Данные профиля были обновлены."),
    ROLE_UPDATE("Ваша роль поменялась, теперь вы - %s!"),
    YOUR_SKILL_WAS_APPROVED_BY(" Скил оценил: %s %s."),
    YOUR_PROFILE_WAS_CHANGED_BY(" Изменение данных произведено пользователем: %s %s."),
    DEPARTMENT_UPDATED("Ваш департамент изменился, теперь вы в %s!"),
    REQUEST_NEW_COMMENT("%s %s прокомментировал <a href=\"%s\" rel=\"noopener noreferrer\" target=\"_blank\">%s</a>."),
    REQUEST_EXECUTOR("Вас назначили исполнителем заявки: %s");

    private final String message;

    NotificationMessages(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
