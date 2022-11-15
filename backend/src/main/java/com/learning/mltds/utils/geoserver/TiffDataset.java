package com.learning.mltds.utils.geoserver;

import com.learning.mltds.entity.Imageinfo;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

import java.time.LocalDateTime;
import java.util.*;

public class TiffDataset {
    private String imagePath;
    private Dataset dataset;
    // 影像长宽信息
    private Integer imageWidth;
    private Integer imageHeight;
    // 获取影像波段数
    private Integer bands;
    // 获取源影像的坐标参考
    private double[] geoTransform;

    public TiffDataset(String _imagePath) {
        // gdal工具初始化
        gdal.AllRegister();
        // 支持中文路径
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");

        this.imagePath = _imagePath;
        this.dataset = gdal.Open(this.imagePath);

        if(dataset == null){
            System.err
                    .println("GDALOpen failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            return;
        }

        this.imageWidth = dataset.getRasterXSize();
        this.imageHeight = dataset.getRasterYSize();
        this.bands = dataset.getRasterCount();

        geoTransform = new double[6];
        dataset.GetGeoTransform(geoTransform);
    }

    public Imageinfo getImageinfo(){
        Imageinfo imageinfo = new Imageinfo();
        String filename = imagePath.substring(imagePath.lastIndexOf('\\') + 1, imagePath.lastIndexOf('.'));
        imageinfo.setFilename(filename);
        imageinfo.setPath(imagePath);
        imageinfo.setIsDetected(Boolean.FALSE); // 上传的图像检测置为false
//        imageinfo.setTaskId(-1);

        imageinfo.setImageWidth(imageWidth);
        imageinfo.setImageHeight(imageHeight);
        imageinfo.setImageBands(bands);
        imageinfo.setDetectedTime(LocalDateTime.now().toString());

        imageinfo.setSatType("default");
        imageinfo.setImageType("default");
        imageinfo.setSensorType("default");

        List<Double> extent = getTiffExtent();
        List<Double> center = getTiffGeoCenter();

        imageinfo.setLongitude(extent.get(0));
        imageinfo.setLatitude(extent.get(1));
        // TODO GeoCenter 待补充
        imageinfo.setSpatialResolution(geoTransform[1]);
        imageinfo.setProjectedCoordinateX(geoTransform[0]);
        imageinfo.setProjectedCoordinateY(geoTransform[3]);

        return imageinfo;
    }


    /**
     * 获取图像左上角和右下角的经纬度坐标
     */
    public List<Double> getTiffExtent() {
        List<Double> extent = new ArrayList<>(Arrays.asList(
                geoTransform[0],
                geoTransform[3],
                geoTransform[0] + geoTransform[1] * imageWidth,
                geoTransform[3] - geoTransform[1] * imageHeight
        ));
        return transform2WGS84(extent);
    }

    /**
     * 获取图像中心的经纬度坐标
     */
    public List<Double> getTiffGeoCenter() {
        List<Double> extent = new ArrayList<>(Arrays.asList(
                geoTransform[0] + geoTransform[1] * imageWidth/2,
                geoTransform[3] - geoTransform[1] * imageHeight/2
        ));
        return transform2WGS84(extent);
    }

    // 将左上角和右下角的两个投影坐标转化为WGS84标准坐标
    private List<Double> transform2WGS84(List<Double> extent) {
        SpatialReference prosrs;
        String proj;

        proj = dataset.GetProjectionRef();
        // PROJCS 是投影坐标系, GEOGCS 是地理坐标系
        if (proj.startsWith("PROJCS")) {
            prosrs = new SpatialReference(proj);
            // WKID表示Well Known ID，
            String WKID = prosrs.GetAttrValue("AUTHORITY", 1);

            // 4326 是 GCS_WGS_1984 地理坐标系的WKID
            if (!WKID.equals("4326")) {
                // 将投影坐标系坐标转为地理坐标系坐标
//                extent = transform2WGS84(WKID, extent);
                SpatialReference s = new SpatialReference("");
                s.SetFromUserInput("EPSG:" + WKID); // source coordinates
                SpatialReference t = new SpatialReference("");
                t.SetFromUserInput("EPSG:4326"); // destination coordinates, WGS-84

                // corner坐标是4个，中心坐标是2个
                double[] bottomRight = new double[2];
                double[] topLeft = new double[2];
                try {
                    CoordinateTransformation ct = CoordinateTransformation.CreateCoordinateTransformation(s, t);
                    topLeft = ct.TransformPoint(extent.get(0), extent.get(1));
                    if(extent.size() == 4)
                        bottomRight = ct.TransformPoint(extent.get(2), extent.get(3));
                } catch(RuntimeException e) {
                    e.printStackTrace();
                }
                if (extent.size() == 2)
                    extent = new ArrayList<>(Arrays.asList(topLeft[0], topLeft[1]));
                else if(extent.size() == 4)
                    extent = new ArrayList<>(Arrays.asList(
                            topLeft[0], topLeft[1], bottomRight[0], bottomRight[1]));
                else
                    extent = null;
            } else {
                double[] res1 = geo2LonLat(dataset, extent.get(0), extent.get(1));
                double[] res2 = new double[2];
                // corner坐标是4个，中心坐标是2个
                if(extent.size() == 4)
                    res2 = geo2LonLat(dataset, extent.get(2), extent.get(3));

                if(extent.size() == 2)
                    extent = new ArrayList<>(Arrays.asList(res1[0], res1[1]));
                else if(extent.size() == 4)
                    extent = new ArrayList<>(Arrays.asList(res1[0], res1[1], res2[0], res2[1]));
                else
                    extent = null;
            }
            prosrs.delete();
        }
        return extent;
    }

    private double[] geo2LonLat(Dataset dataset, double x, double y) {
        SpatialReference prosrs = new SpatialReference();
        prosrs.ImportFromWkt(dataset.GetProjectionRef());
        SpatialReference geosrs = prosrs.CloneGeogCS();

        CoordinateTransformation ct = CoordinateTransformation.CreateCoordinateTransformation(prosrs, geosrs);
        return ct.TransformPoint(x, y);
    }

}
