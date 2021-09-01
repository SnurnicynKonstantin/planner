package xrm.extrim.planner.enums;

import xrm.extrim.planner.exception.PlannerException;

import java.util.Arrays;

public enum Mail {
    IDP_SUBJECT("Индивидуальный план развития"),
    IDP_TEXT_FOR_USER("Здравствуйте, %s %s!\nЧерез %d или меньше дней у вас состоится встреча по поводу индивидуального плана развития. Ниже приведены ваши задачи:\n%s\nДата встречи: %s"),
    IDP_TEXT_FOR_MANAGER("Здравствуйте, %s %s!\nЧерез %d или меньше дней у вашего подчиненного сотрудника %s состоится встреча по поводу индивидуального плана развития. Ниже приведены задачи сотрудника:\n%s\nДата встречи: %s"),
    VACATION_REQUEST_SUBJECT("Заявка на отпуск"),
    VACATION_REQUEST_TEXT_FOR_CREATOR("Здравствуйте, %s %s!\nВаша заявка на отпуск успешно сгенерирована. Запрос на подтверждение отправлен вашим руководителям: %s.\nСтатус подтверждения заявки можно посмотреть по ссылке: %s ."),
    VACATION_REQUEST_TEXT_FOR_APPROVERS("Здравствуйте, %s %s!\nВаш подчиненный сотрудник %s %s оставил заявку на отпуск.\nПодтвердите или отклоните заявку, перейдя по ссылке: %s ."),
    VACATION_REQUEST_APPROVED_TEXT_FOR_CREATOR("Здравствуйте, %s %s!\nВаша заявка на отпуск была одобрена всеми вашими руководителями.\nХорошего отдыха!"),
    BIRTHDAY_REQUEST_SUBJECT("С Днём Рождения!"),
    BIRTHDAY_REQUEST_TEXT("С днем рожденья, %s %s!\nПусть в жизни будет все, что нужно для счастья, а в работе — все, что нужно для успеха."),
    BIRTHDAY_INFO_REQUEST_HEADER_TEXT("Всем Привет!\nВ этом месяце поздравляем с Днём Рождения:\n"),
    BIRTHDAY_INFO_REQUEST_PERSON_TEXT("%s %s – %s %s\n"),
    BIRTHDAY_INFO_REQUEST_END_TEXT("Желаем всего самого наилучшего, чтобы всё в жизни складывалось легко, удачно и невероятно красиво.\nЧтобы все желания и мечты сбывались легко и на одном дыхании!"),
    SET_REQUEST_EXECUTOR_SUBJECT("Заявки"),
    SET_REQUEST_EXECUTOR_TEXT("Здравствуйте, %s %s!\nВас назначили исполнителем заявки: %s"),
    REQUEST_CREATED_SUBJECT("Новая заявка для %s: %s"),
    REQUEST_CREATED_TEXT("Для вас создана новая заявка -  %s"),
    REQUEST_UPDATE_STATUS_SUBJECT("Обновление по %s"),
    REQUEST_UPDATE_STATUS_TEXT("У вашей заявки %s новый статус: %s");


    private final String text;

    Mail(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Mail getByText(String text) {
        return Arrays.stream(values())
                .filter(value -> value.text.equals(text))
                .findFirst()
                .orElseThrow(() -> new PlannerException(String.format(Exception.WRONG_ENUM_CODE.getDescription(), text)));
    }
}
