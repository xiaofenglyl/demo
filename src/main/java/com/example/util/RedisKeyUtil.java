package com.example.util;

/**
 * Created by asus-Iabx on 2017/5/5.
 */
public class RedisKeyUtil {

    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";
    private static String BIZ_FOLLOWER = "FOLLOWER";
    // 关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }
    public static String getLikeKey(int entityId, int entitytype)
    {
        return BIZ_LIKE + SPLIT + String.valueOf(entitytype) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entitytype)
    {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entitytype) + SPLIT + String.valueOf(entityId);
    }

    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    // 每个用户对某类实体的关注key
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
