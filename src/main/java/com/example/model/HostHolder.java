package com.example.model;

import org.springframework.stereotype.Component;

/**
 * Created by asus-Iabx on 2017/4/16.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
