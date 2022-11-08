package com.learning.mltds.utils;

import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ImageinfoDTO;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.entity.Objectinfo;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileUtils {
    public static Map<String, List<String>> listFiles(String path) {
        if (path == null)
            return new HashMap<>();

        List<String> fileList = new ArrayList<>();
        List<String> dirList = new ArrayList<>();
        for (File file : new File(path).listFiles()) {
            //如果是文件夹
            if (file.isDirectory()){
                dirList.add(file.toString());
            } else {
                fileList.add(file.getName());
                //下面是带有路径的写法
                //list.add(file.getPath());
            }
        }
        Map<String, List<String>> list = new HashMap<>();
        list.put("isDir", dirList);
        list.put("isFile", fileList);
        return list;
    }

    public static boolean saveDetectionResult2Txt(List<DetectionResultDTO> detectionResultDTOS) {
        String inferSaveTxtPath = CommonConfig.inferSaveTxtPath;
        System.out.println("检测结果保存地址：" + inferSaveTxtPath);
        File txtPath = new File(inferSaveTxtPath);
        if(!txtPath.exists())
            txtPath.mkdirs();

        for(DetectionResultDTO detectionResultDTO : detectionResultDTOS){
//            ImageinfoDTO imageinfoDTO = detectionResultDTO.getImageinfoDTO();
            String imageName = detectionResultDTO.getImageName();
            List<ObjectinfoDTO> objectinfoDTOS= detectionResultDTO.getObjectinfoList();

            try {
                File txtFile = new File(Paths.get(inferSaveTxtPath, imageName + ".ok.txt").toString());
                if (!txtFile.exists())
                    txtFile.createNewFile();
                FileWriter writer = new FileWriter(txtFile);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                // 对象信息写入文件
                for(ObjectinfoDTO objectinfoDTO : objectinfoDTOS){
                    List<String> lineBox = new ArrayList<>(Arrays.asList(
                            objectinfoDTO.getClassname() + "," + objectinfoDTO.getTypename(),
                            objectinfoDTO.getConfidence().toString(),
                            objectinfoDTO.getBboxP1X().toString(),
                            objectinfoDTO.getBboxP1Y().toString(),
                            objectinfoDTO.getBboxP2X().toString(),
                            objectinfoDTO.getBboxP2Y().toString(),
                            objectinfoDTO.getBboxP3X().toString(),
                            objectinfoDTO.getBboxP3Y().toString(),
                            objectinfoDTO.getBboxP4X().toString(),
                            objectinfoDTO.getBboxP4Y().toString()));
                    String writeLine = String.join(" ", lineBox);
                    bufferedWriter.write(writeLine.trim() + '\n');
                }
                bufferedWriter.flush();
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        for(String imageName : detectionResults.keySet()){
//            Map<String, Object> detectionResult = (Map<String, Object>) detectionResults.get(imageName);
//            Map<String, Object> imageResult = (Map<String, Object>)detectionResult.get("image_info");
//            MapUtils.removeEmptyMap(imageResult);  // 清除空值
//        }
        return false;
    }
//    public static List<String> listFiles(String path, boolean isRecursion) {
//        if (path == null)
//            return new ArrayList<>();
//
//        ArrayList<String> list = new ArrayList<>();
//        for (File file : new File(path).listFiles()) {
//            //如果是文件夹
//            if (file.isDirectory()){
//                list.addAll(listFiles(file.getPath()));
//            } else {
//                list.add(file.getName());
//                //下面是带有路径的写法
//                //list.add(file.getPath());
//            }
//        }
//        return list;
//    }

    public static void main(String[] args) {
        String testFile = new String("C:\\Users\\yiyonghao\\Desktop\\FGVC\\IGARSS");
        for (String file : listFiles(testFile).get("isFile")) {
            System.out.println(file);
        }
    }
}
