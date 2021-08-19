package com.gen.vacation.global.Enum;

public enum ApproverFlagEnum {

    WAIT("WAIT","대기"), APPROVE("APPROVE","승인"), REJECT("REJECT","반려");

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
