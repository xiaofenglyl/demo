package com.example.controller;

import com.example.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-Iabx on 2017/4/4.
 */
//@Controller
public class IndexController {
    @RequestMapping("/")
    @ResponseBody
    public String index()
    {
        return "Hello world";
    }


    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type",defaultValue= "1") int type,
                          @RequestParam(value = "key",defaultValue="hello") String key)
    {
        return String.format("{%s},{%d},{%d},{%s}",groupId, userId, type, key);
    }

    @RequestMapping("/vm")
    public String news(Model model)
    {
        model.addAttribute("value1","vv1");
        List<String>  colors= Arrays.asList(new String[] {"red","green","blue"});
        Map<String,String> map=new HashMap<String,String>();
            for(int i=0;i<4;++i)
            {
                map.put(String.valueOf(i), String.valueOf(i*i));
            }
            model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("Jack"));
        return "news";
    }
}
