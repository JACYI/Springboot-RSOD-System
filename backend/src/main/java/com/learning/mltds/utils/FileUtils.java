package com.learning.mltds.utils;

import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.entity.Objectinfo;
import com.learning.mltds.vo.FileMenuVO;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FileUtils {

    // 从当前路径下读取文件夹和文件信息 返回 FileMenuVO 对象
    public static FileMenuVO listFiles(String path) {
        if (path == null)
            return new FileMenuVO();
        FileMenuVO fileMenuVO = new FileMenuVO(path);

        for (File file : new File(path).listFiles()) {
            fileMenuVO.addMenuFile(file.isDirectory(), file.getName());
            //下面是带有路径的写法
            //fileMenuVO.addMenuFile(file.isDirectory(), file.getPath());
        }
//        Map<String, List<String>> list = new HashMap<>();
//        list.put("isDir", dirList);
//        list.put("isFile", fileList);
        return fileMenuVO;
    }

    // 保存单张图像的检测结果到 txt 文件
    public static boolean saveDetectionResult2Txt(String filename, List<Objectinfo> objectinfos) {
        String inferSaveTxtPath = CommonConfig.inferSaveTxtPath;
        System.out.println("检测结果保存地址：" + inferSaveTxtPath);
        File txtPath = new File(inferSaveTxtPath);
        if(!txtPath.exists())
            txtPath.mkdirs();

//        for(Objectinfo objectinfo : objectinfos){
//            ImageinfoDTO imageinfoDTO = detectionResultDTO.getImageinfoDTO();
//            String imageName = detectionResultDTO.getImageName();
//            List<ObjectinfoDTO> objectinfoDTOS = objectinfos.getObjectInfos();

        try {
            File txtFile = new File(Paths.get(inferSaveTxtPath, filename + ".ok.txt").toString());
            if (!txtFile.exists())
                txtFile.createNewFile();
            FileWriter writer = new FileWriter(txtFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // 对象信息写入文件
            for(Objectinfo objectinfo : objectinfos){
                List<String> lineBox = new ArrayList<>(Arrays.asList(
                        objectinfo.getClassname() + "," + objectinfo.getTypename(),
                        objectinfo.getConfidence().toString(),
                        objectinfo.getBboxP1X().toString(),
                        objectinfo.getBboxP1Y().toString(),
                        objectinfo.getBboxP2X().toString(),
                        objectinfo.getBboxP2Y().toString(),
                        objectinfo.getBboxP3X().toString(),
                        objectinfo.getBboxP3Y().toString(),
                        objectinfo.getBboxP4X().toString(),
                        objectinfo.getBboxP4Y().toString()));
                String writeLine = String.join(" ", lineBox);
                bufferedWriter.write(writeLine.trim() + '\n');
            }
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // windows专用，Linux需要修改 TODO
    public static String getDirName(String path) {
        if(new File(path).isDirectory())
            return path;
        return path.substring(0, path.lastIndexOf('\\'));
    }




    public static void main(String[] args) {
        String testFile = new String("C:\\Users\\yiyonghao\\Desktop\\FGVC\\IGARSS\\");
        System.out.println(getDirName(testFile));
    }
}
