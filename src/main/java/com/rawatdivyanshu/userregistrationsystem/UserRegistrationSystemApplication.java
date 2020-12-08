package com.rawatdivyanshu.userregistrationsystem;

import com.rawatdivyanshu.userregistrationsystem.Filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserRegistrationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRegistrationSystemApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        AuthFilter authFilter = new AuthFilter();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/api/user/get-user");
        return registrationBean;
    }

}
