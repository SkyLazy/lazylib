package com.mmall.common;

public class Constants {
    public static final String CURRENT_USER = "current_user";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        public final Integer ROLE_CUSTOMER = 0;
        public final Integer ROLE_ADMIN = 1;
    }
}
