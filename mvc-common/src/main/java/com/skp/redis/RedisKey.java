package com.skp.redis;

/**
 * Redis Key constant
 * @author jin song
 *
 */
public class RedisKey {

    /**
     * 用于Redis Key模板的通用分隔符
     */
    public static final String COMMON_SEPARATOR = ":";

    /**
     * 用户表key,user_uid,key=uid
     */
    public static final String USER_KEY         = "user" + COMMON_SEPARATOR + "%s";

    /**
     * openid表key,openid_openid,key=openid
     */
    public static final String OPENID_KEY       = "openid" + COMMON_SEPARATOR + "%s";
    /**
     * 用户表key,user_uid,key=uid
     */
    public static final String MOBILE_KEY       = "mobileNo" + COMMON_SEPARATOR + "%s";

}
