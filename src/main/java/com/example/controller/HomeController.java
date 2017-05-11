package com.example.controller;

import com.example.model.EntityType;
import com.example.model.HostHolder;
import com.example.model.News;
import com.example.model.ViewObject;
import com.example.service.LikeService;
import com.example.service.NewsService;
import com.example.service.UserService;
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
