package xrm.extrim.planner.enums;

import xrm.extrim.planner.exception.PlannerException;

import java.util.Arrays;

public enum Exception {
    USER_NOT_FOUND("Пользователь %s не найден."),
    ANNOUNCEMENT_NOT_FOUND("Уведомление %s не найдено."),
    REQUIRED_FIELD_EMPTY("Пожалуйста, заполните обязательное поле."),
    VACATION_DATE_PAST("Вы не можете использовать прошедшую дату при установке начала или конца отпуска."),
    FILE_NOT_FOUND("Файл %s не найден."),
    FILE_ACCESS_DENIED("Вам запрещен доступ к файлу %s."),
    VACATION_REQUEST_ACCESS_DENIED("Вам запрещен доступ к заявке на отпуск %s."),
    FILE_UPLOAD_ERROR("При загрузке файла произошла ошибка."),
    NO_AVAILABLE_FILES("Доступных вам файлов нет."),
    SKILL_NOT_FOUND("Скилл %s не найден."),
    TASK_NOT_FOUND("Задача %s не найдена."),
    ROLE_NOT_FOUND("Роль %s не найдена."),
    NOTIFICATION_NOT_FOUND("Уведомление %s не найдено."),
    NOT_YOUR_NOTIFICATION("Это не ваше уведомление, его невозможно отметить прочитанным."),
    EMPTY_PROJECT_HISTORY("История проектов пуста."),
    PROJECT_HISTORY_NOT_FOUND("История проектов %s не найдена."),
    WRONG_DATE_FORMAT("Некорректный формат даты."),
    INCORRECT_RATE_NUMBER("Номер оценки должен быть в диапазоне от 1 до 5."),
    RATE_DESCRIPTION_ALREADY_EXISTS("Описание оценки %s уже существует."),
    SKILL_ALREADY_EXISTS("Скилл %s уже существует."),
    GROUP_ALREADY_EXISTS("Группа %s уже существует."),
    FILE_ALREADY_EXISTS("Вы уже загружали файл %s."),
    USER_ALREADY_EXISTS("Пользователь %s уже существует."),
    MANAGER_ALREADY_EXISTS("Начальник %s уже существует."),
    USERS_HAVE_SKILL("У одного или более пользователей есть этот скилл."),
    TASK_TYPE_NOT_INCREMENTED("Тип задачи %s не увеличился."),
    ADDING_SKILLS_IN_WRONG_METHOD("Нельзя добавлять новые скиллы в этом методе."),
    SUB_TASK_NOT_SUPPORTED("Подзадача %s не поддерживается."),
    ACCESS_DENIED_FOR_NOT_ADMIN("Доступ разрешен только администраторам."),
    SUBTASK_NOT_COMPLETE("Подзадача %s не завершена."),
    ILLEGAL_METHOD_CALL("Этот метод не стоит вызывать. Он реализуется фильтрами Spring Security."),
    WRONG_ENUM_CODE("Код %s не существует."),
    ERROR_WHILE_PARSING_ATTRIBUTE("Ошибка в \"mapUserFromContext\" при расшифровке атрибута \"cn\"."),
    SKILL_WAS_DELETED("У пользователя %s cкилл %s удален"),
    USER_HAVE_NOT_SKILL("У пользователя %s нет скилла %s"),
    INFINITY_LOOP_MANAGERS("Пользователь не может быть руководителем для своих руководителей"),
    HIMSELF_MANAGER("Пользователь не может быть руководителем саому себе"),
    POSITION_NOT_FOUND("Позиция %s не найдена"),
    CATEGORY_SUBSCRIBER_NOT_FOUND("Подписки пользователя на категорию %s не найдено"),
    DEPARTMENT_NOT_FOUND("Подразделение %s не найдено"),
    POSITION_ALREADY_EXIST("Позиция %s уже существует"),
    DEPARTMENT_ALREADY_EXIST("Департамент %s уже существует"),
    DOES_NOT_HAVE_PERMISSION("У вас нет прав на выполение этой операции"),
    FEEDBACK_NOT_FOUND("Отзыв 5s не найден."),
    CANNOT_MANAGE_YOURSELF("Вы не можете назначить себя в качестве утверждающего отпуск."),
    REQUEST_NOT_FOUND("Заявка %s не найдена"),
    REQUEST_COMMENT_NOT_FOUND("Комментарий %s не найден"),
    PROJECT_NOT_FOUND("Проект %s не найден");

    private final String description;

    Exception(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Exception getByDescription(String description) {
        return Arrays.stream(values())
                .filter(value -> value.description.equals(description))
                .findFirst()
                .orElseThrow(() -> new PlannerException(String.format(Exception.WRONG_ENUM_CODE.getDescription(), description)));
    }
}
