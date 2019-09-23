package com.example.itokenwebadmin.config;

import com.example.itokenwebadmin.interceptor.WebAdminInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAdminInterceptorConfig implements WebMvcConfigurer {

    //将拦截器设置为Bean,在拦截其中才能使用@AutoWired注解自动注入
    @Bean
    WebAdminInterceptor webAdminInterceptor() {
        return new WebAdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webAdminInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static");
    }
}
