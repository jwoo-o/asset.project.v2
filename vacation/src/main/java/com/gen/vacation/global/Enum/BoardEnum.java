package com.gen.vacation.global.Enum;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-11-23
 * Time: 오후 2:35
 */
public enum BoardEnum {

    NOTICE("notice"), QA("qa");

    private String value;

    BoardEnum(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
