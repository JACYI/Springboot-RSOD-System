package com.learning.mltds.utils;

import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.utils.geoserver.TiffDataset;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

import static org.gdal.gdalconst.gdalconstConstants.GDT_Byte;

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


//    public static void targetSliceCropping(TiffDataset dataset, List<Integer> bbox) {
//        targetSliceCropping(dataset, bbox, CommonConfig.areaSize);
//    }
//
//    public static void targetSliceCropping(TiffDataset dataset,
//                                           List<Integer>bbox,
//                                           Integer areaSize) {
//
//    }
//
//    // 判断是否超出边界
//    private boolean _judgeBorder(){
//        return false;
//    }
    /**
     * 图像裁切
     * @param pszSrcFile    输入文件路径
     * @param pszDstFile    输出文件路径
     * @param startX        起始行号
     * @param startY        起始列号
     * @param sizeX         裁切列数
     * @param sizeY         裁切行数
     * @param pszFormat     输出文件格式
     */
    static void ImageCut(String pszSrcFile, String pszDstFile, int startX, int startY, int sizeX, int sizeY, String pszFormat){
        //注册驱动
        // 注册所有的驱动
        gdal.AllRegister();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        // 为了使属性表字段支持中文，请添加下面这句
        gdal.SetConfigOption("SHAPE_ENCODING","");

        //使用只读方式打开图像
        Dataset dataset = gdal.Open(pszSrcFile, gdalconstConstants.GA_ReadOnly);
        if (dataset == null) {
            System.out.println("read fail!");
            return;
        }
        //获取波段的数据类型
        int dataType = dataset.GetRasterBand(1).GetRasterDataType();
        //获取图像的波段个数
        int bandCount = dataset.GetRasterCount();

        //根据裁切范围确定裁切后的图像宽高
        int dstWidth = sizeX;
        int dstHeight = sizeY;

        double geoTransform[] = new double[6];      //{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        dataset.GetGeoTransform(geoTransform);
        //        System.out.println(geoTransform);         //该图像的转换系数为-180.0|0.02222222222222|0.0|90.0|0.0|-0.02222222222222

        //计算裁切后的图像的左上角坐标 geoTransform中的只是裁切前的图像左上角坐标 因此需要进行更新 只需更新左上角坐标即可
        geoTransform[0] = geoTransform[0] + sizeX*geoTransform[1] + sizeY*geoTransform[2];
        geoTransform[3] = geoTransform[3] + sizeX*geoTransform[4] + sizeY*geoTransform[5];

        //创建输出文件并设置空间参考和坐标信息
        //通过name来获取驱动
//        Driver driver = gdal.GetDriverByName(pszFormat);
//        if (driver == null) {   //判断获取的driver对象是否为null null表示未获取驱动
//            System.out.println("Get Driver Failed");
//            return;
//        }
        //通过指定驱动创建输出文件的dataset——>dstDataset
//        Dataset dstDataset = driver.Create(pszDstFile, dstWidth, dstHeight, bandCount, dataType, (String[]) null);
//        if (driver == null) {   //判断获取的dstDataset对象是否为null null表示未获取驱动
//            System.out.println("Get dstDataset Failed");
//            return;
//        }
        //因为只是从原来的图像上裁切一块下来 所以dstDataset的geoTransform一样(只是左上角的坐标变化了)
//        dstDataset.SetGeoTransform(geoTransform);
//        //所以dstDataset的投影信息还是一样的
//        //SetProjection 为数据集设置投影定义
//        //GetProjectionRef/GetProjection 获取数据集的投影定义
//        dstDataset.SetProjection(dataset.GetProjectionRef());


        //指定读取波段序号的顺序
        int[] bandList = new int[bandCount];
        for (int i = 0; i < bandCount; i++) {
            bandList[i] = i + 1;
        }

        //根据图像的数据类型来申请不同类型的缓存进行裁切
        //如果是GDT_Byte(8bit图像)
//        if (dataType == GDT_Byte){
        if (true){
            //申请所有数据所需要的缓存 如果图像太大需要分块处理 否则会出现申请内存时失败的情况
            int count = dstHeight * dstWidth * bandCount;
            byte[] dataBuff = new byte[count];

            //将裁切部分读入dataBuff中
            int x = dataset.getRasterXSize();
            int y = dataset.getRasterYSize();
            dataset.ReadRaster(startX, startY, sizeX, sizeY, sizeX, sizeY, dataType, dataBuff, bandList, 0,0,0);
            System.out.println("Read OK");
            // 将读取的图像数据写入普通image
            ByteArrayInputStream bs = new ByteArrayInputStream(dataBuff);
            try {
                File dstFile = new File(pszDstFile);
                if(!dstFile.exists())
                    dstFile.createNewFile();
                BufferedImage bil = ImageIO.read(bs);
                ImageIO.write(bil, "image/jpeg", dstFile);
            } catch (IOException e){
                e.printStackTrace();
                System.out.println("读取字符流失败");
            }
            //将读入的图像数据写到dstDataset数据集中就就可以完成图像的裁切了
//            dstDataset.WriteRaster(0, 0, sizeX, sizeY, sizeX, sizeY, dataType, dataBuff, bandList, 0,0,0);
//            System.out.println("Write OK");
        }else {
            //其他类型的图像处理 和8bit类似 只是申请的缓存类型不同而已
            System.out.println("其他处理");
        }
        //Dataset对象操作结束后如果不调用delete()方法 数据可能无法正确刷新到磁盘
        dataset.delete();
//        dstDataset.delete();

        System.out.println("裁切完成");
    }

    public static void main(String[] args) {
//                UpdateRaster();
        //原始图像裁切
        //        ImageCut("E:\\GIS\\NE1_LR_LC_SR_W\\NE1_LR_LC_SR_W.tif","E:\\GIS\\NE1_LR_LC_SR_W\\test.tif",
        //                500, 300, 500, 500, "GTiff");
        ImageCut("C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source\\Google-1m_2_1.tif",
                "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source\\cut_image.jpg",
                500, 300, 1500, 1500, "jpg");
    }




}
