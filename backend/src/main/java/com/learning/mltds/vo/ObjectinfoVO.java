package com.learning.mltds.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.entity.Objectinfo;
import com.learning.mltds.utils.MapUtils;
import com.learning.mltds.utils.ReqUtils;
import com.learning.mltds.utils.geoserver.TiffDataset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ObjectinfoVO {
    private Integer id;
    private Double confidence;
    private Double score;
    private String classname;
    private String typename;
    private String shipNumber;
    private String detectedTime;
    private String createTime;
    // 标识该目标是否是手动标注的
    private Boolean isLabeled;

    private List<Integer> imageCenter;
    private List<Double> geoCenter;
    private Double length;
    private Double width;
    private List<Integer> bbox;
//    private String bboxP1;
//    private String bboxP2;
//    private String bboxP3;
//    private String bboxP4;
    private List<Double> geoBbox;
//    private String geoBboxP1;
//    private String geoBboxP2;
//    private String geoBboxP3;
//    private String geoBboxP4;

    private String fixTargetSlicePath;
    private String targetSlicePath;
    private String areaSlicePath;

    private Integer isDeleted;
    private Integer imageId;
    private Integer taskId;

//    private String candidateShipNumbers;
//    public ObjectinfoVO(Map<String, Object> objectInfo) {
//
//    }
    // 目标信息完整的情况
    public Objectinfo convert2DO() {
        return Objectinfo.builder()
                .id(id)
                .confidence(confidence)
                .classname(classname)
                .typename(typename)
                .shipNumber(shipNumber)
                .detectedTime(detectedTime)
//                .imageCenterX(string2IntegerCoordX(imageCenter))
//                .imageCenterY(string2IntegerCoordY(imageCenter))
//                .geoCenterLongitude(string2DoubleCoordX(geoCenter))
//                .geoCenterLatitude(string2DoubleCoordY(geoCenter))
                .imageCenterX(imageCenter.get(0))
                .imageCenterY(imageCenter.get(1))
                .geoCenterLongitude(geoCenter.get(0))
                .geoCenterLatitude(geoCenter.get(1))

//                .bboxP1X(string2IntegerCoordX(bboxP1))
//                .bboxP1Y(string2IntegerCoordY(bboxP1))
//                .bboxP2X(string2IntegerCoordX(bboxP2))
//                .bboxP2Y(string2IntegerCoordY(bboxP2))
//                .bboxP3X(string2IntegerCoordX(bboxP3))
//                .bboxP3Y(string2IntegerCoordY(bboxP3))
//                .bboxP4X(string2IntegerCoordX(bboxP4))
//                .bboxP4Y(string2IntegerCoordY(bboxP4))
                .bboxP1X((bbox.get(0)))
                .bboxP1Y((bbox.get(1)))
                .bboxP2X((bbox.get(2)))
                .bboxP2Y((bbox.get(3)))
                .bboxP3X((bbox.get(4)))
                .bboxP3Y((bbox.get(5)))
                .bboxP4X((bbox.get(6)))
                .bboxP4Y((bbox.get(7)))

//                .geoBboxP1X(string2DoubleCoordX(geoBboxP1))
//                .geoBboxP1Y(string2DoubleCoordX(geoBboxP1))
//                .geoBboxP2X(string2DoubleCoordX(geoBboxP2))
//                .geoBboxP2Y(string2DoubleCoordX(geoBboxP2))
//                .geoBboxP3X(string2DoubleCoordX(geoBboxP3))
//                .geoBboxP3Y(string2DoubleCoordX(geoBboxP3))
//                .geoBboxP4X(string2DoubleCoordX(geoBboxP4))
//                .geoBboxP4Y(string2DoubleCoordX(geoBboxP4))
                .geoBboxP1X(geoBbox.get(0))
                .geoBboxP1Y(geoBbox.get(1))
                .geoBboxP2X(geoBbox.get(2))
                .geoBboxP2Y(geoBbox.get(3))
                .geoBboxP3X(geoBbox.get(4))
                .geoBboxP3Y(geoBbox.get(5))
                .geoBboxP4X(geoBbox.get(6))
                .geoBboxP4Y(geoBbox.get(7))

                .targetSlicePath(targetSlicePath)
                .fixTargetSlicePath(fixTargetSlicePath)
                .areaSlicePath(areaSlicePath)

                .imageId(imageId)
                .taskId(taskId)
                .build();
    }

    // 目标仅包含geoBbox（标注的情况）, 需要使用dataset计算其他数据
//    public Objectinfo convert2DO(TiffDataset dataset) {
//        // 经纬度转图像坐标
//        List<Integer> bbox = geoUtils.lonLat2Bbox(imagePath, (List<Double>)objectInfo.get("geo_bbox"));
//        objectInfo.put("bbox", bbox);
//        Integer mean_x = 0;
//        Integer mean_y = 0;
//        Integer min_x = Integer.MAX_VALUE;
//        Integer max_x = Integer.MIN_VALUE;
//        Integer min_y = Integer.MAX_VALUE;
//        Integer max_y = Integer.MIN_VALUE;
//        for(int i=0; i<bbox.size(); i+=2){
//            mean_x += bbox.get(i);
//            mean_y += bbox.get(i + 1);
//            min_x = Math.min(min_x, bbox.get(i));
//            max_x = Math.max(max_x, bbox.get(i));
//            min_y = Math.min(min_y, bbox.get(i +1));
//            max_y = Math.max(max_y,bbox.get(i + 1));
//        }
//        mean_x = mean_x / (bbox.size()  / 2);
//        mean_y = mean_y / (bbox.size()  / 2);
//        List<Integer> imageCenterList = new ArrayList<>();
//        imageCenterList.add(mean_x);
//        imageCenterList.add(mean_y);
//        objectInfo.put("imageCenter", imageCenterList);
//    }

    public Integer string2IntegerCoordX(String coordString) {
        int mid = coordString.indexOf(',');
        return Integer.parseInt(coordString.substring(1, mid));
    }

    public Integer string2IntegerCoordY(String coordString) {
        int mid = coordString.indexOf(',');
        return Integer.parseInt(coordString.substring(mid+1, coordString.length()-1));
    }

    public Double string2DoubleCoordX(String coordString) {
        int mid = coordString.indexOf(',');
        return Double.parseDouble(coordString.substring(1, mid));
    }
    public Double string2DoubleCoordY(String coordString) {
        int mid = coordString.indexOf(',');
        return Double.parseDouble(coordString.substring(1, mid));
    }

//    public static void main(String[] args) {
////        string2DoubleCoordX("(-76.42847836017609,-76.42847836017609)");
//    }
}
