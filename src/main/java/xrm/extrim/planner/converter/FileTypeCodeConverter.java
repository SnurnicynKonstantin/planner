package xrm.extrim.planner.converter;

import xrm.extrim.planner.enums.FileType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FileTypeCodeConverter implements AttributeConverter<FileType, Short> {
    @Override
    public Short convertToDatabaseColumn(FileType attribute) {
        return attribute == null ? FileType.UNDEFINED.getCode() : attribute.getCode();
    }

    @Override
    public FileType convertToEntityAttribute(Short dbData) {
        return FileType.getByCode(dbData);
    }
}
