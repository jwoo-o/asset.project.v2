package com.gen.vacation.global.enums;

public enum ApproverFlagEnum {

    WAIT("WAIT","대기"), APPROVE("APPROVE","승인"), REJECT("REJECT","반려"), SUCCESS("SUCCESS","발급완료");

    private String value;
    private String desc;

    ApproverFlagEnum(String value,String desc) {

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
