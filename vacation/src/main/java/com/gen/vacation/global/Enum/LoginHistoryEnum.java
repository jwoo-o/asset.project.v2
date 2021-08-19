package com.gen.vacation.global.Enum;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-25
 * Time: 오전 10:28
 */
public enum LoginHistoryEnum {

    ADMIN("admin"), USER("user"), SYSTEM("system");

    private String value;

    LoginHistoryEnum(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }

}
