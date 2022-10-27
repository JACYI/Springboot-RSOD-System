package com.learning.mltds.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
