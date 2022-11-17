package com.learning.mltds.utils.geoserver;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图像扩展名转化工具
 */
public class ImageExtConvert {
    private static final String DEFAULT_GEOGCS = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.0174532925199433],AUTHORITY[\"EPSG\",\"4326\"]]";
    private static final List<Double> DEFAULT_GEOTRANSFORM = new ArrayList<>(Arrays.asList(
            0., 8.983152841195215e-05,
            0., 0.,
            0., -8.983152841195215e-05)
    );

    // TODO 普通图像转化为TIFF地理图像
    public static String nomarl2Tiff(String imagePath) {
        return null;
    }


    private static void writeTiff(String tiffPath
                                  ) {

    }
}
