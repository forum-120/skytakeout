package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// 1. 关键改动：导入 WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
// 2. 关键改动：实现 WebMvcConfigurer 接口
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public // 3. 加上 @Override
     void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**") // 拦截所有 /admin/** 路径
                .excludePathPatterns("/admin/employee/login"); // 仅放行登录接口

        // 4. 已删除多余的放行路径：
        // 你的拦截器只管 /admin/**，
        // 而 /doc.html, /webjars/** 等路径根本不在 /admin/** 之下，
        // 所以它们本来就不会被这个拦截器拦截，无需额外 exclude。
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        log.info("准备生成接口文档");
        return new OpenAPI()
                .info(new Info()
                        .title("苍穹外卖项目接口文档")
                        .version("2.0")
                        .description("苍穹外卖项目接口文档"));
    }


    // 5. 关键改动：删除了整个 addResourceHandlers 方法
    //    实现 WebMvcConfigurer 后，Spring Boot 会自动配置好
    //    knife4j 和你项目（如 static 目录）的静态资源映射。
}