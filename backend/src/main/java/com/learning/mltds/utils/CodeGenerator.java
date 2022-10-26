package com.learning.mltds.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

// 代码生成器，禁止运行
public class CodeGenerator {
    private static void generator() {
        FastAutoGenerator.create("jdbc:mysql://10.106.128.165:3306/mltds?serverTimezone=GMT%2b8", "root", "kuanguang")
                .globalConfig(builder -> {
                    builder.author("root") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.learning.mltds") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("mltds_user", "mltds_task", "mltds_sampleinfo", "mltds_objectinfo",
                            "mltds_imageinfo", "mltds_historyactivityinfo", "mltds_globalobjectinfo", "mltds_basemodel") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_", "mltds_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
    public static void main(String[] args) {
        generator();
    }

}
