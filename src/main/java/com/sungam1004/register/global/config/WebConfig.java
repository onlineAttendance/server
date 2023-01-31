package com.sungam1004.register.global.config;

import com.sungam1004.register.global.manager.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenManager tokenManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor(tokenManager))
                .order(1) //낮을 수록 먼저 호출
                .addPathPatterns("/api/admin/**") //인터셉터를 적용할 url 패턴
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/admin/login"); //인터셉터에서 제외할 패턴 지정
    }

    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PATCH")
                .allowCredentials(true);
    }*/
}