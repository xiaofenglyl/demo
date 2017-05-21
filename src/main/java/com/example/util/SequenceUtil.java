package com.example.util;

import com.example.model.Comment;
import com.example.model.News;
import com.example.service.NewsService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by asus-Iabx on 2017/5/21.
 */
@Service
public class SequenceUtil {
    @Autowired
    JedisAdapter jedisAdapter;
    public  void sortNews(News news, Comment comment)
    {
        int Qanswer = news.getCommentCount();
        int Qscore = news.getLikeCount();
        Date a = new Date(2014,6,7);
        double QageInHours = (news.getCreatedDate().getTime() - a.getTime())/1000/60/60;
        double Qupdated = (comment.getCreatedDate().getTime() - a.getTime())/1000/60/60;
        double newsNumber = ((Qanswer + Qscore)/5)/((QageInHours + 1) - Math.pow(((QageInHours - Qupdated)/2),1.5));
        jedisAdapter.zadd("sortkey",newsNumber,String.valueOf(news.getId()));
    }

    public Set<News> sort()
    {
        Set set = jedisAdapter.zrange("sortkey",0,10);
        return set;
        //Iterator<String> newsI = jedisAdapter.zrange("sortkey",0,10).iterator();
        //while(newsI.hasNext())
        //{
        //    newsI.next();
        //}

    }
}
