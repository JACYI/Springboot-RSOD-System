package com.learning.mltds.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration

public class CORSFilter implements WebMvcConfigurer {



    @Override

    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")

                .allowedOriginPatterns("*")
//                .allowedOrigins("http://10.106.128.117:11121")

                .allowCredentials(true)

                .allowedMethods("GET", "POST", "DELETE", "PUT")

                .maxAge(3600);

    }



}

//@Component
//public class CORSFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpServletRequest req = (HttpServletRequest) request;
//        res.addHeader("Access-Control-Allow-Credentials", "true");
////        res.addHeader("Access-Control-Allow-Origin", req.getHeader("origin"));
//        res.addHeader("Access-Control-Allow-Origin", "http://10.106.128.116:8013");
//        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
//        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN,customercoderoute,authorization,conntectionid,Cookie");
//        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
//            response.getWriter().println("ok");
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//}
