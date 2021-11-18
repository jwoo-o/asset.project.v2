package com.gen.vacation.global.enums;

import java.util.Arrays;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2021-04-27
 * Time: 오후 1:10
 */
public enum VacationKind {

    YEAR("YEAR","연차"),
    GYEONGJOSA("GYEONGJOSA","경조사"),
    MORNING("MORNING","오전반차"),
    AFTERNOON("AFTERNOON","오후반차"),
    ETC("ETC","기타");


    private String value;
    private String desc;

    VacationKind(String value,String desc) {

        this.value = value;
        this.desc = desc;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }


    public static boolean notAnnual(String value) {
        return Arrays.stream(values())
                .filter(vacationKind -> VacationKind.GYEONGJOSA.equals(vacationKind) || VacationKind.ETC.equals(vacationKind))
                .anyMatch(v -> v.value.equals(value));

    }

}
