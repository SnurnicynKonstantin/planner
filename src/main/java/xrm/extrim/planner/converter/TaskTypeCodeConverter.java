package xrm.extrim.planner.converter;

import xrm.extrim.planner.enums.TaskType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TaskTypeCodeConverter implements AttributeConverter<TaskType, Short> {

    @Override
    public Short convertToDatabaseColumn(TaskType attribute) {
        return attribute == null ? TaskType.NOT_INCREMENTED.getCode() : attribute.getCode();
    }

    @Override
    public TaskType convertToEntityAttribute(Short dbData) {
        return TaskType.getByCode(dbData);
    }
}
