package com.gen.vacation.global.domain.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-04-07
 * Time: 오후 7:25
 */
@Converter
public class BooleanTo01Converter implements AttributeConverter<Boolean, String> {

    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "1" : "0";
    }

    public Boolean convertToEntityAttribute(String s) {
        return "1".equals(s);
    }
}
