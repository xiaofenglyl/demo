package com.example.DAO;

import com.example.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by asus-Iabx on 2017/4/16.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status, likecount ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status},#{likeCount})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId} order by id desc "})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    //@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = ( select max(id) from ( select ",SELECT_FIELDS, " from ", TABLE_NAME, "where entity_type=#{entityType} and entity_id=#{entityId} ) as total) "})
    //select * from comment where id=(select max(id) from(select * from comment where entity_type=1 and entity_id=12)as tt);
    @Select({"select * from comment where id=(select max(id) from(select * from comment where entity_type=#{entityType} and entity_id=#{entityId})as tt)"})
    Comment selectByEntityId(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    Comment getById(int id);

    @Update({"update ", TABLE_NAME, " set likecount = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    @Select({"select count(id) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
