package com.mmall.common;

/**
 * @Author: WangLi
 * @Date: 2018/9/15 15:35
 * @Package: com.mmall.common
 * @Description:
 */
public class Const {
    public static final String CURRENT_USER ="current_user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface Role{
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }
}
