package com.learning.mltds.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class ImagePathReflectConfig implements WebMvcConfigurer {
//    /*
//     *addResourceHandler:访问映射路径，随便写一个
//     *addResourceLocations:资源绝对路径，就是那个修改后的保存图片的文件夹的位置
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**").addResourceLocations("file:C:/Users/yiyonghao/SpringbootProject/bs/backend/images");
//
//    }
//}