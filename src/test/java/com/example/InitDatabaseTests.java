package com.example;
/**
 * Created by asus-Iabx on 2017/4/6.
 */

import com.example.DAO.LoginTicketDAO;
import com.example.DAO.NewsDAO;
import com.example.DAO.UserDAO;
import com.example.model.LoginTicket;
import com.example.model.News;
import com.example.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    NewsDAO newsDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Test
    public void contextLoads() {
        Random r = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", r.nextInt(1000)));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            News news = new News();
            Date date=new Date();
            news.setCommentCount(i);
            date.setTime(date.getTime()+1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", r.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}",i));
            news.setLink(String.format("http://www.newcoder.com/%d.html",i));

            newsDAO.addNews(news);


            LoginTicket loginTicket=new LoginTicket();
            loginTicket.setStatus(0);
            loginTicket.setUserId(i+1);
            loginTicket.setExpired(date);
            loginTicket.setTicket(String.format("TICKET%d",i+1));

            loginTicketDAO.addTicket(loginTicket);
        }
        Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
    }
}