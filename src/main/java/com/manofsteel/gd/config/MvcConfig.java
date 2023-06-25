package com.manofsteel.gd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("redirect:/oauth2/authorization/google");
        registry.addViewController("/local/login").setViewName("redirect:http://localhost:3000/oauth2/authorization/google");
    }
}