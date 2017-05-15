package com.example.util;

import sun.security.provider.ConfigFile;

/**
 * Created by asus-Iabx on 2017/5/5.
 */
public class RedisKeyUtil {

    private static String SPILT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }
    public static String getLikeKey(int entityId, int entitytype)
    {
        return BIZ_LIKE + SPILT + String.valueOf(entitytype) + SPILT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entitytype)
    {
        return BIZ_DISLIKE + SPILT + String.valueOf(entitytype) + SPILT + String.valueOf(entityId);
    }
}
