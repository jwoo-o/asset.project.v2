package com.gen.vacation.global.enums;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-12-08
 * Time: 오후 6:06
 */
public enum ApprovalEnum {

    WAIT("WAIT","상신"),ING("ING","승인중"), APPROVE("APPROVE","승인완료"), REJECT("REJECT","반려"),
    CANCEL("CANCEL", "취소 요청"), CANCELSUCCESS("CANCELSUCCESS", "승인취소");


    private String value;
    private String desc;

    ApprovalEnum(String value,String desc) {

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
