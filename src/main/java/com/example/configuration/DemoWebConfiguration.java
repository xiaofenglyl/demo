package com.example.configuration;

import com.example.intercepter.LoginrequiredIntercepter;
import com.example.intercepter.PassportIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by asus-Iabx on 2017/4/16.
 */
@Component
public class DemoWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private PassportIntercepter passportIntercepter;

    @Autowired
    private LoginrequiredIntercepter loginrequiredIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercepter);
        registry.addInterceptor(loginrequiredIntercepter).
                addPathPatterns("/msg/*").addPathPatterns("/like").addPathPatterns("/dislike");;
        super.addInterceptors(registry);
    }
}
