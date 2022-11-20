package com.learning.mltds.utils;


import com.learning.mltds.utils.geoserver.TiffDataset;
import org.apache.commons.codec.binary.Base64;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static org.gdal.gdalconst.gdalconstConstants.GDT_Byte;

public class ImageUtils {
    private static final Set<String> geoTiffFormat = new HashSet<>(Arrays.asList("tif", "tiff"));

    /**
     * 普通图像剪裁（jpg, png, tiff）等
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


    /**
     * GeoTiff 格式地理图像裁切
     * @param dataset       输入图像的Dataset对象
     * @param dstFilePath   输出图像文件路径
     * @param startX        起始行号
     * @param startY        起始列号
     * @param sizeX         裁切列数
     * @param sizeY         裁切行数
     */
    public static void tiffImageCut(Dataset dataset, String dstFilePath, int startX, int startY, int sizeX, int sizeY){
        if (dataset == null) {
            System.out.println("Dataset is null!");
            return;
        }
        int dataType = dataset.GetRasterBand(1).GetRasterDataType();     // 获取波段的数据类型
        System.out.println(dataType);
        int bandCount = dataset.GetRasterCount();                               // 获取图像的波段个数

        double[] geoTransform = new double[6];                                  //{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        dataset.GetGeoTransform(geoTransform);

        // 计算裁切后的图像的左上角坐标 geoTransform中的只是裁切前的图像左上角坐标 因此需要进行更新 只需更新左上角坐标即可
        geoTransform[0] = geoTransform[0] + sizeX*geoTransform[1] + sizeY*geoTransform[2];
        geoTransform[3] = geoTransform[3] + sizeX*geoTransform[4] + sizeY*geoTransform[5];


        // 通过name来获取驱动
        Driver driver = gdal.GetDriverByName("GTiff");
        if (driver == null) {   //判断获取的driver对象是否为null null表示未获取驱动
            System.out.println("Get Driver Failed");
            return;
        }
        // 创建输出tiff文件并设置空间参考和坐标信息，通过指定驱动创建输出文件的dataset——>dstDataset
        String tiffPath = dstFilePath.substring(0, dstFilePath.lastIndexOf('.')) + ".tif";
        Dataset dstDataset = driver.Create(tiffPath, sizeX, sizeY, bandCount, dataType, (String[]) null);
        if (dstDataset == null) {   //判断获取的dstDataset对象是否为null null表示未获取驱动
            System.out.println("Get dstDataset Failed");
            return;
        }
        // 因为只是从原来的图像上裁切一块下来 所以dstDataset的geoTransform一样(只是左上角的坐标变化了)
        // 所以dstDataset的投影信息还是一样的
        // SetProjection 为数据集设置投影定义
        // GetProjectionRef/GetProjection 获取数据集的投影定义
        dstDataset.SetGeoTransform(geoTransform);
        dstDataset.SetProjection(dataset.GetProjectionRef());

        // 指定读取波段序号的顺序
        int[] bandList = new int[bandCount];
        for (int i = 0; i < bandCount; i++) {
            bandList[i] = i + 1;
        }

        // 根据图像的数据类型来申请不同类型的缓存进行裁切
        if (dataType == GDT_Byte){
            // 如果是GDT_Byte(8bit图像)
            int count = sizeY * sizeX * bandCount;
            //申请所有数据所需要的缓存 如果图像太大需要分块处理 否则会出现申请内存时失败的情况
            byte[] dataBuff = new byte[count];
            //将裁切部分读入dataBuff中
            dataset.ReadRaster(startX, startY, sizeX, sizeY, sizeX, sizeY, dataType, dataBuff, bandList, 0,0,0);
            System.out.println("Read OK");
            //将读入的图像数据写到dstDataset数据集中就就可以完成图像的裁切了
            dstDataset.WriteRaster(0, 0, sizeX, sizeY, sizeX, sizeY, dataType, dataBuff, bandList, 0,0,0);
            System.out.println("Write to dataset OK");

            // 输出文件后缀名, "tif", "jpg", "png"等
            String dstFormat = dstFilePath.substring(dstFilePath.lastIndexOf('.') + 1);
            // 将读取的图像数据写入普通image
            if(!geoTiffFormat.contains(dstFormat.toLowerCase(Locale.ROOT)))
                dstDataset.GetDriver().CreateCopy(dstFilePath, dstDataset);
        } else {
            //其他类型的图像处理 和8bit类似 只是申请的缓存类型不同而已
            System.out.println("不支持该编码类型的图像裁剪");
        }
        //Dataset对象操作结束后如果不调用delete()方法 数据可能无法正确刷新到磁盘
        dataset.delete();
        dstDataset.delete();

        System.out.println("裁切完成");
    }

    /**
     * 	将任意图片文件转为base64，读字节是最快的方式
     * @param filePath	图片文件的全路径
     * @return base64编码的字符串
     */
    public String imageToBase64(String filePath) {
        byte [] data =null;
        try {
            InputStream in =new FileInputStream(filePath);
            data=new byte[in.available()];
            in.read(data);
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        return new String(Base64.encodeBase64(data));
    }



    public static void main(String[] args) {
        //注册驱动
        // 注册所有的驱动
        gdal.AllRegister();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        // 为了使属性表字段支持中文，请添加下面这句
        gdal.SetConfigOption("SHAPE_ENCODING","");

        String saveFilePath = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source\\cut_image_black.jpg";
        String inputFilePath = "C:\\Users\\yiyonghao\\SpringbootProject\\bs\\backend\\images\\source\\Google-1m_2_3.tif";
        Dataset dataset = new TiffDataset(inputFilePath).getDataset();
        tiffImageCut(dataset,
                saveFilePath,
                2000, 3000, 1500, 1500);
    }




}
