package com.learning.mltds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 跨域配置文件，用于前后端域名交互
@Configuration
public class CorsConfig {
//    private CorsConfiguration buildConfig() {
//        System.out.println("跨域配置。。。");
//        CorsConfiguration corsConfiguration = new  CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
//        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
//        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
//        return corsConfiguration;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        System.out.println("启动跨域方法。。。。。");
//        UrlBasedCorsConfigurationSource source = new  UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig());  // 4
//        return new CorsFilter(source);
//    }
//    // 当前跨域请求最大有效时长。这里默认1天
//    private static final long MAX_AGE = 24 * 60 * 60;
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
//        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
//        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
//        corsConfiguration.setMaxAge(MAX_AGE);
//        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
//        return new CorsFilter(source);
//    }
}
