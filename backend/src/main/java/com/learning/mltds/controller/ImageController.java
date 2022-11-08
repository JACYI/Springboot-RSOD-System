package com.learning.mltds.controller;

import com.learning.mltds.config.CommonConfig;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images/mltds")
public class ImageController {

    @GetMapping(value = "/{catogory}/{type}/{taskId}/{imageName}",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable String catogory,
                           @PathVariable String type,
                           @PathVariable String taskId,
                           @PathVariable String imageName
                           ) {
        String localPath = Paths.get(CommonConfig.imageBaseUrl, catogory, type, taskId, imageName).toString();
//        System.out.println(localPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(localPath));
            out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", out);
        } catch (IOException e){
            System.out.println("本地读取图像失败，图像路径：" + localPath);
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
