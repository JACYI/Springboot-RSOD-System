package com.learning.mltds.config;

public class CommonConfig {
    public static final String exchange = "high_priority";
    // 切片图像路径
    public static final String imageBaseUrl = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\slice";
    // 检测原图路径
    public static final String sourceImagePath = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source";
    // 保存结果txt路径
    public static final String inferSaveTxtPath = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\data\\txt\\infer";
    // 大图的正方形区域切片边长
    public static final Integer areaSize = 1024;
    // 切片的保存路径
    public static final String SLICE_PATH = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\slice\\detection";


}
