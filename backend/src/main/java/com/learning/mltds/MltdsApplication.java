package com.learning.mltds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


// 设置session 8小时有效期
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 8*60*60)
@SpringBootApplication
public class MltdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MltdsApplication.class, args);
    }

}
