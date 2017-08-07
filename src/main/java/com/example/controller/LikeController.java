package com.example.controller;

import com.example.async.EventModel;
import com.example.async.EventProducer;
import com.example.async.EventType;
import com.example.model.Comment;
import com.example.model.EntityType;
import com.example.model.HostHolder;
import com.example.model.News;
import com.example.service.CommentService;
import com.example.service.LikeService;
import com.example.service.NewsService;
import com.example.util.DemoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by asus-Iabx on 2017/5/5.
 */
@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;


    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId") int newsId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);
        News news = newsService.getById(newsId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));
        return DemoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        // 更新喜欢数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return DemoUtil.getJSONString(0, String.valueOf(likeCount));
    }


    @RequestMapping(path = {"/likec"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String likec(@Param("commentId") int commentId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        commentService.updateLikeCount(commentId, (int) likeCount);
        Comment comment = commentService.getById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(comment.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId));
        return DemoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislikec"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislikec(@Param("commentId") int commentId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        // 更新喜欢数
        commentService.updateLikeCount(commentId, (int) likeCount);
        return DemoUtil.getJSONString(0, String.valueOf(likeCount));
    }



}
