package com.sungam1004.register.global.config;

import com.sungam1004.register.global.interceptor.AdminLoginInterceptor;
import com.sungam1004.register.global.interceptor.UserLoginInterceptor;
import com.sungam1004.register.global.manager.TokenManager;
import com.sungam1004.register.global.resolver.UserIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenManager tokenManager;
    private final UserIdArgumentResolver userEmailArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor(tokenManager))
                .order(1) //낮을 수록 먼저 호출
                .addPathPatterns("/api/user/**") //인터셉터를 적용할 url 패턴
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/admin/login"); //인터셉터에서 제외할 패턴 지정

        registry.addInterceptor(new AdminLoginInterceptor())
                .order(2) //낮을 수록 먼저 호출
                .addPathPatterns("/admin/**") //인터셉터를 적용할 url 패턴
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/admin/login"); //인터셉터에서 제외할 패턴 지정
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userEmailArgumentResolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // "/"로 Get 요청이 들어오면, "home" view를 반환
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/admin").setViewName("admin/adminHome");
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