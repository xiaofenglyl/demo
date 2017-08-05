package com.example.controller;

import com.example.model.*;
import com.example.service.CommentService;
import com.example.service.LikeService;
import com.example.service.NewsService;
import com.example.service.UserService;
import com.example.service.SequenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * Created by asus-Iabx on 2017/4/8.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    SequenceUtil sequenceUtil;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(@RequestParam(value = "userId", defaultValue = "0") int userId,
                        Model model,
                        @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop",pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId, Model model,
                            @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("pop",pop);
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        if(userId != 0)
        {
            List<News> newsList = newsService.getLatestNews(userId, offset, limit);
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            List<ViewObject> vos = new ArrayList<>();
            for (News news : newsList) {
                ViewObject vo = new ViewObject();
                vo.set("news", news);
                vo.set("user", userService.getuser(news.getUserId()));
                if (localUserId != 0) {
                    vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                } else {
                    vo.set("like", 0);
                }
                vos.add(vo);
            }
            return vos;
        }
        else
        {
            List<News> newsList = newsService.getLatestNews(userId, offset, limit);
            String zset_key = null;
            for(News news : newsList) {
                System.out.println(news.getId());
                Comment comment = null;
                if(news.getCommentCount() != 0)
                    comment = commentService.getCommentByEntityId(news.getId(),EntityType.ENTITY_NEWS);
                //System.out.println(comment.getId());
                zset_key = sequenceUtil.addNews(news,comment);
            }
            Set zset = sequenceUtil.sortNews(zset_key);
            System.out.println("key="+zset.size());
            Iterator<String> newsI = zset.iterator();
            LinkedList<News> newnewsList = new LinkedList<>();
            while(newsI.hasNext())
            {
                newnewsList.add(newsService.getById(Integer.parseInt(newsI.next())));
            }

            //List<News> newsList = newsService.getById();
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            List<ViewObject> vos = new ArrayList<>();
            for (News news : newnewsList) {
                ViewObject vo = new ViewObject();
                vo.set("news", news);
                vo.set("user", userService.getuser(news.getUserId()));
                if (localUserId != 0) {
                    vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                } else {
                    vo.set("like", 0);
                }
                vos.add(vo);
            }
            return vos;
        }


    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                            HttpServletResponse response,
                          HttpSession session
                          ) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements())
        {
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }

        for(Cookie cookie:request.getCookies())
        {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        return sb.toString();
    }

    @RequestMapping(value = {"/setting"})
    @ResponseBody
    public String set() {
        return "Setting is ok";
    }
}
