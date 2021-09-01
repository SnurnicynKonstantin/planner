package xrm.extrim.planner.converter;

import xrm.extrim.planner.enums.RequestStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RequestStatusConverter implements AttributeConverter<RequestStatus, String> {
    @Override
    public String convertToDatabaseColumn(RequestStatus attribute) {
        return attribute == null ? RequestStatus.OPEN.name() : attribute.name();
    }

    @Override
    public RequestStatus convertToEntityAttribute(String dbData) {
        return RequestStatus.getByName(dbData);
    }
}
