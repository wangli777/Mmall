package com.mmall.common;

/**
 * @Author: WangLi
 * @Date: 2018/9/13 13:11
 * @Package: com.mmall.common
 * @Description:
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    public final int code;
    public final String desc;
    ResponseCode(int i, String desc) {
        this.code = i;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
