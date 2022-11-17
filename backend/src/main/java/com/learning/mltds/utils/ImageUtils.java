package com.learning.mltds.utils;

import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.utils.geoserver.TiffDataset;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ImageUtils {
    /**
     * 图片剪裁
     *
     * @param x          距离左上角的x轴距离
     * @param y          距离左上角的y轴距离
     * @param width      宽度
     * @param height     高度
     * @param sourcePath 图片源
     * @param descpath   目标位置
     */
    public static void imageCut(int x, int y, int width, int height,
                          String sourcePath, String descpath) {
        FileInputStream fis = null;
        ImageInputStream iis = null;
        try {

            String fileSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);

            String dstFileSuffix = descpath.substring(descpath.lastIndexOf(".") + 1);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);
            ImageReader reader = it.next();


            fis = new FileInputStream(sourcePath);
            iis = ImageIO.createImageInputStream(fis);

            reader.setInput(iis, true);

            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);

            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, dstFileSuffix, new File(descpath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fis = null;
            }
            if (iis != null) {
                try {
                    iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iis = null;
            }
        }
    }


    public static void targetSliceCropping(TiffDataset dataset, List<Integer> bbox) {
        targetSliceCropping(dataset, bbox, CommonConfig.areaSize);
    }

    public static void targetSliceCropping(TiffDataset dataset,
                                           List<Integer>bbox,
                                           Integer areaSize) {

    }

    // 判断是否超出边界
    private boolean _judgeBorder(){
        return false;
    }



}
