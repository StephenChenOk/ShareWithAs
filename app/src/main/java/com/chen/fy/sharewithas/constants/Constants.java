package com.chen.fy.sharewithas.constants;

public class Constants {

    /**
     * 每页包含的新闻数量
     */
    public static final int NEWS_NUM = 10;

    private static String SERVER_URL = "http://api.tianapi.com/";
    private static String GENERAL_NEWS_PATH = "generalnews/";

    public static String API_KEY = "ed546e6c3a7405d0e683a880e6c110a7";
    public static String GENERAL_NEWS_URL = SERVER_URL + GENERAL_NEWS_PATH;

    /**
     * 登录功能URL
     */
    public static final String LOGIN_SERVER_URL = "http://xiongtianmin.online/album/user/login";
    /**
     * 注册、修改信息URL
     */
    public static final String USER_SERVER_URL = "http://xiongtianmin.online/album/user";
    /**
     * 头像URL
     */
    public static final String HEAD_ICON_SERVER_URL = "http://xiongtianmin.online/album/user/uploadHead";

}
