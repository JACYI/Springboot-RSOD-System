package com.learning.mltds.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.learning.mltds.entity.Objectinfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ObjectinfoVO {
    private Integer id;
    private Double confidence;
    private String classname;
    private String typename;
    private String shipNumber;
    private String detectedTime;
    private String createTime;

    private String imageCenter;
    private String geoCenter;
    private String bboxP1;
    private String bboxP2;
    private String bboxP3;
    private String bboxP4;
    private String geoBboxP1;
    private String geoBboxP2;
    private String geoBboxP3;
    private String geoBboxP4;

    private String fixTargetSlicePath;
    private String targetSlicePath;
    private String areaSlicePath;

    private Integer isDeleted;
    private Integer imageId;
    private Integer taskId;

//    private String candidateShipNumbers;

    public Objectinfo convert2DO() {
        return Objectinfo.builder()
                .id(id)
                .confidence(confidence)
                .classname(classname)
                .typename(typename)
                .shipNumber(shipNumber)
                .detectedTime(detectedTime)
                .imageCenterX(string2IntegerCoordX(imageCenter))
                .imageCenterY(string2IntegerCoordY(imageCenter))
                .geoCenterLongitude(string2DoubleCoordX(geoCenter))
                .geoCenterLatitude(string2DoubleCoordY(geoCenter))

                .bboxP1X(string2IntegerCoordX(bboxP1))
                .bboxP1Y(string2IntegerCoordY(bboxP1))
                .bboxP2X(string2IntegerCoordX(bboxP2))
                .bboxP2Y(string2IntegerCoordY(bboxP2))
                .bboxP3X(string2IntegerCoordX(bboxP3))
                .bboxP3Y(string2IntegerCoordY(bboxP3))
                .bboxP4X(string2IntegerCoordX(bboxP4))
                .bboxP4Y(string2IntegerCoordY(bboxP4))

                .geoBboxP1X(string2DoubleCoordX(geoBboxP1))
                .geoBboxP1Y(string2DoubleCoordX(geoBboxP1))
                .geoBboxP2X(string2DoubleCoordX(geoBboxP2))
                .geoBboxP2Y(string2DoubleCoordX(geoBboxP2))
                .geoBboxP3X(string2DoubleCoordX(geoBboxP3))
                .geoBboxP3Y(string2DoubleCoordX(geoBboxP3))
                .geoBboxP4X(string2DoubleCoordX(geoBboxP4))
                .geoBboxP4Y(string2DoubleCoordX(geoBboxP4))

                .targetSlicePath(targetSlicePath)
                .fixTargetSlicePath(fixTargetSlicePath)
                .areaSlicePath(areaSlicePath)

                .imageId(imageId)
                .taskId(taskId)
                .build();
    }

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
