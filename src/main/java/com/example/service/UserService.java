package com.example.service;

import com.example.DAO.LoginTicketDAO;
import com.example.DAO.UserDAO;
import com.example.model.LoginTicket;
import com.example.model.User;
import com.example.util.DemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by asus-Iabx on 2017/4/8.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userdao;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String,Object> register(String username, String password)
    {
        Map<String,Object> map=new HashMap<String,Object>();
        if(org.apache.commons.lang.StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if(org.apache.commons.lang.StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user=userdao.selectByName(username);
        if(user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(DemoUtil.MD5(password+user.getSalt()));
        userdao.addUser(user);

        String ticket= addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }


    public Map<String,Object> login(String username, String password)
    {
        Map<String,Object> map=new HashMap<String,Object>();
        if(org.apache.commons.lang.StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if(org.apache.commons.lang.StringUtils.isBlank(password))
        {
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user=userdao.selectByName(username);
        if(user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }
        if(!DemoUtil.MD5(password+user.getSalt()).equals(user.getPassword()))
        {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        String ticket= addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }


    private String addLoginTicket(int userId)
    {
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        ticket.setStatus(0);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);

        return ticket.getTicket();
    }


    public void logout(String ticket)
    {
        loginTicketDAO.updateStatus(ticket,1);
    }





    public void addUser(User user)
    {
        userdao.addUser(user);
    }
    public User getuser(int id)
    {
        return userdao.selectById(id);
    }
}
