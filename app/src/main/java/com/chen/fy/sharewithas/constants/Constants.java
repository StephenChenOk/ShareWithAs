package com.chen.fy.sharewithas.constants;

public class Constants {

    /**
     * 每页包含的新闻数量
     */
    public static final int NEWS_NUM = 10;

    public static String SERVER_URL = "http://api.tianapi.com/";
    public static String ALL_NEWS_PATH = "allnews/";
    public static String GENERAL_NEWS_PATH = "generalnews/";

    public static String API_KEY = "ed546e6c3a7405d0e683a880e6c110a7";
    public static String ALL_NEWS_URL = SERVER_URL + ALL_NEWS_PATH;
    public static String GENERAL_NEWS_URL = SERVER_URL + GENERAL_NEWS_PATH;

    public static int NEWS_COL5 = 5;
    public static int NEWS_COL7 = 7;
    public static int NEWS_COL8 = 8;
    public static int NEWS_COL10 = 10;
    public static int NEWS_COL11 = 11;

    public static String NEWS_DETAIL_URL_KEY = "news_detail_url_key";

    private Constants() {
    }

}
