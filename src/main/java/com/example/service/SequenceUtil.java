package com.example.service;

import com.example.model.Comment;
import com.example.model.News;
import com.example.service.NewsService;
import com.example.service.UserService;
import com.example.util.JedisAdapter;
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
    public  String addNews(News news, Comment comment)
    {
        String key = "sortekey";
        int Qanswer = news.getCommentCount();
        int Qscore = news.getLikeCount();
        //System.out.println("Qanswer" + Qanswer + "  " + "Qscore" + Qscore);
        Date a = new Date();
        double QageInHours = -(double)((news.getCreatedDate().getTime() - a.getTime())/1000/60/60);
        //System.out.println(news.getCreatedDate().getTime());
       // System.out.println(a.getTime());
        //System.out.println("time" + QageInHours);
        double Qupdated = 0;
        if(news.getCommentCount() != 0)
            Qupdated = (comment.getCreatedDate().getTime() - a.getTime())/1000/60/60;
        //System.out.println("Qupdates" + Qupdated);
        double newsNumber =(double) (1000*(Qanswer + Qscore)/5)/((QageInHours + 1) - Math.pow(((QageInHours + Qupdated)/2),2));
        //System.out.println("newsnumber" + newsNumber);
        jedisAdapter.zadd(key,newsNumber,String.valueOf(news.getId()));
        return key;
    }

    public  Set<News> sortNews(String key)
    {
        Set set = jedisAdapter.zrange(key,0,-1);
        return set;
        //Iterator<String> newsI = jedisAdapter.zrange("sortkey",0,10).iterator();
        //while(newsI.hasNext())
        //{
        //    newsI.next();
        //}

    }
}
