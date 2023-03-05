package com.sungam1004.register.global.config;

import com.sungam1004.register.global.interceptor.AdminLoginInterceptor;
import com.sungam1004.register.global.interceptor.UserLoginInterceptor;
import com.sungam1004.register.global.manager.TokenManager;
import com.sungam1004.register.global.resolver.UserIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
                .addPathPatterns("/api/users/**")
                .excludePathPatterns("/api/users/login", "/api/users/signup", "/api/users/posts/*",
                        "/api/users/images/**", "/css/**", "/*.ico", "/error", "/js/**");

        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login", "/css/**", "/*.ico", "/error", "/js/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userEmailArgumentResolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // "/"로 Get 요청이 들어오면, "home" view를 반환
        registry.addViewController("/admin").setViewName("admin/adminHome");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://onlineattendance-61655.web.app", "http://localhost:3000",
                        "http://localhost:8080", "http://localhost:8081", "http://localhost:8082",
                        "https://sungam.site")
                .allowedHeaders(HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION)
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(),
                        HttpMethod.PATCH.name(), HttpMethod.OPTIONS.name());
    }
}