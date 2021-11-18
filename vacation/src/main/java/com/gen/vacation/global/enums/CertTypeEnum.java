package com.gen.vacation.global.enums;

public enum CertTypeEnum {

    CAREER("CAREER","경력증명서"),EMP("EMP","재직증명서");

    private String value;
    private String desc;

    CertTypeEnum(String value,String desc) {

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
}
