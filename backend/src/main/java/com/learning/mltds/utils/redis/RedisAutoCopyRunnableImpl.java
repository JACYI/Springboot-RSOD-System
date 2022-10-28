package com.learning.mltds.utils.redis;

import com.learning.mltds.utils.LinuxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
public class RedisAutoCopyRunnableImpl implements Runnable {

    private Integer intervalTime;

    // 拷贝等待间隔，单位是毫秒
    public RedisAutoCopyRunnableImpl(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    @Override
    public void run(){
        LinuxUtil linuxUtil = new LinuxUtil();
        while (true){
            try {
                log.info("Redis 自动拷贝任务执行");
                linuxUtil.executeLinuxCmd("pwd && ls");
                Thread.sleep(intervalTime);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Redis自动拷贝出错");
                break;
            }
        }
    }

}