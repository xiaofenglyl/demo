package com.example.controller;

import com.example.async.EventModel;
import com.example.async.EventProducer;
import com.example.async.EventType;
import com.example.service.UserService;
import com.example.util.DemoUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by asus-Iabx on 2017/4/14.
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path="/reg/",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rememberme,
                      HttpServletResponse response)
    {
        try {
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);


                return DemoUtil.getJSONString(0, "注册成功");
            }
            else
                return DemoUtil.getJSONString(1,map);
        }catch (Exception e)
        {
            return DemoUtil.getJSONString(1,"注册异常");
        }
    }


    @RequestMapping(path={"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rememberme,
                        HttpServletResponse response)
    {
        try {
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new
                        EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                        .setExt("username", "牛客").setExt("to", "2832842085@qq.com"));
                return DemoUtil.getJSONString(0, "成功");
            }
            else
                return DemoUtil.getJSONString(1,map);
        }catch (Exception e)
        {
            return DemoUtil.getJSONString(1,"注册异常");
        }
    }



    @RequestMapping(path={"/logout/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket)
    {
        userService.logout(ticket);
        return "redirect:/";
    }
}


