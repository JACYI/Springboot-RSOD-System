package com.learning.mltds.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;

import com.learning.mltds.config.CommonConfig;
import com.learning.mltds.utils.geoserver.GeoUtils;
import com.learning.mltds.utils.geoserver.TiffDataset;
import com.learning.mltds.vo.ObjectinfoVO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
  @TableName("mltds_objectinfo")
@Builder
public class Objectinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Double confidence;

    private String classname;

    private String typename;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private String shipNumber;  //弦号

    private String detectedTime;

    private Integer imageCenterX;

    private Integer imageCenterY;

    private Double geoCenterLongitude;

    private Double geoCenterLatitude;

    private Integer bboxP1X;

    private Integer bboxP1Y;

    private Integer bboxP2X;

    private Integer bboxP2Y;

    private Integer bboxP3X;

    private Integer bboxP3Y;

    private Integer bboxP4X;

    private Integer bboxP4Y;

    private Double geoBboxP1X;

    private Double geoBboxP1Y;

    private Double geoBboxP2X;

    private Double geoBboxP2Y;

    private Double geoBboxP3X;

    private Double geoBboxP3Y;

    private Double geoBboxP4X;

    private Double geoBboxP4Y;

    private String targetSlicePath;

    private String fixTargetSlicePath;

    private String areaSlicePath;

    @TableField(insertStrategy = FieldStrategy.NEVER)
    private String createTime;
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.DEFAULT)
    private String updateTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Integer isDeleted;

    private Integer imageId;

    private Integer taskId;

    public ObjectinfoVO convert2VO(String imagePath) {
        // TODO windows路径转化为linux路径
        imagePath = Paths.get(CommonConfig.sourceImagePath,
                imagePath.split("task-image/")[1]).toString();
        // 根据 imageId 创建Tiffdataset，获取图像的地理信息
        TiffDataset dataset = new TiffDataset(imagePath);
        double[] geoTransform = dataset.getGeoTransform();

        int nXSize = dataset.getImageWidth() + 1;
        int nYSize = dataset.getImageHeight() + 1;

        Integer[] bbox = new Integer[]{bboxP1X, bboxP1Y,
                bboxP2X, bboxP2Y,
                bboxP3X, bboxP3Y,
                bboxP4X, bboxP4Y};
        // 用于计算中心经纬度
        List<Double> geoBbox = new ArrayList<>(Arrays.asList(
                bboxP1X.doubleValue(), bboxP1Y.doubleValue(),
                bboxP2X.doubleValue(), bboxP2Y.doubleValue(),
                bboxP3X.doubleValue(), bboxP3Y.doubleValue(),
                bboxP4X.doubleValue(), bboxP4Y.doubleValue()
        ));

        double geoCenterX = 0.;
        double geoCenterY = 0.;

        // 对目标框从坐标点换算到经纬度上
        for(int i=0; i<geoBbox.size(); i+=2) {
            Double bboxX = geoBbox.get(i); Double bboxY = geoBbox.get(i + 1);
            // 横纵坐标调整
            double x = geoTransform[0] + bboxX * geoTransform[1] + bboxY * geoTransform[2];
            double y = geoTransform[3] + bboxX * geoTransform[4] + bboxY * geoTransform[5];
            // 如果是投影坐标系的话
            if(x > 5000. || y > 5000.){
                double[] temp = GeoUtils.geo2LonLat(dataset.getDataset(), x, y);
                x = temp[0];
                y = temp[1];
            }
            geoCenterX += x; geoCenterY += y;
            geoBbox.set(i, x); geoBbox.set(i + 1, y);
        }
        // 计算目标中心点的经纬度
        geoCenterX /= 4.;
        geoCenterY /= 4.;
        // 计算矩形框的长宽
        Map<String, Double> lengthAndWidth = GeoUtils.getGeoRectSize(geoBbox);
        Double length = lengthAndWidth.get("length");
        Double width = lengthAndWidth.get("width");

        return ObjectinfoVO.builder()
                .id(id)
                .score(confidence)
                .confidence(confidence)
                .classname(classname)
                .typename(typename)
                .shipNumber(shipNumber)
                .detectedTime(detectedTime)
                .createTime(createTime)
                .isLabeled(false)

                .imageCenter(new ArrayList<>(Arrays.asList(nXSize/2, nYSize/2)))
                .geoCenter(new ArrayList<>(Arrays.asList(geoCenterX, geoCenterY)))
                .length(length)
                .width(width)
                .bbox(new ArrayList<>(Arrays.asList(bbox)))
                .geoBbox(geoBbox)
                .targetSlicePath(targetSlicePath)
                .areaSlicePath(areaSlicePath)
                .fixTargetSlicePath(fixTargetSlicePath)

                .isDeleted(isDeleted)
                .imageId(imageId)
                .taskId(taskId)
                .build();
    }

    public <T> String coord2String(T coordX, T coordY) {
        return '(' + coordX.toString() + ',' + coordY.toString() + ')';
    }

//    // 通过经纬度坐标获取矩形框的长度和宽度
//    private Map<String, Double> getGeoLength(List<Double> bbox) {
//        double[] a = new double[]{bbox.get(0), bbox.get(1)};
//        double[] b = new double[]{bbox.get(2), bbox.get(3)};
//        double[] c = new double[]{bbox.get(4), bbox.get(5)};
//
//        double a1 = distance(a, b);
//        double b1 = distance(b, c);
////        List<List<Double>> res1 = new ArrayList<>();
////        List<List<Double>> res2 = new ArrayList<>();
////        res1.add(new ArrayList<>(Arrays.asList(a[0], a[1])));
////        res1.add(new ArrayList<>(Arrays.asList(b[0], b[1])));
////        res2.add(new ArrayList<>(Arrays.asList(b[0], b[1])));
////        res2.add(new ArrayList<>(Arrays.asList(c[0], c[1])));
//
////        HashMap<String, List<List<Double>>> lengthAndWidth = new HashMap<>();
//        HashMap<String, Double> lengthAndWidth = new HashMap<>();
//        if(a1 > b1){
//            lengthAndWidth.put("length", a1);
//            lengthAndWidth.put("width", b1);
//        } else {
//            lengthAndWidth.put("length", b1);
//            lengthAndWidth.put("width", a1);
//        }
//        return lengthAndWidth;
//    }
//    // 用来计算两点之间实际距离
//    private long distance(double[] point1, double[] point2) {
//        double lat1 = point1[1] * Math.PI / 180.0;
//        double lon1 = point1[0] * Math.PI / 180.0;
//        double lat2 = point2[1] * Math.PI / 180.0;
//        double lon2 = point2[0] * Math.PI / 180.0;
//
//        double vlon = Math.abs(lon2 - lon1);
//        double vlat = Math.abs(lat2 - lat1);
//
//        double h = Haversin(vlat) + Math.cos(lat1) * Math.cos(lat2) * Haversin(vlon);
//        double length = 2 * 6371008.8 * Math.sin(Math.sqrt(h));
//
//        return Math.round(length);
//    }

//    private double Haversin(double theta){
//        double v = Math.sin(theta / 2);
//        return v * v;
//    }
}
