package com.learning.mltds.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.learning.mltds.entity.Imageinfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ImageinfoVO {
    private static final long serialVersionUID = 1L;

    private String filename;

    private Double latitude;
    private Double longitude;
    private Integer imageWidth;
    private Integer imageHeight;
    private Double spatialResolution;
    private Integer imageBands;
    private List<Double> geoCenter;

    private String imageType;
    private String satType;
    private String sensorType;

    private String path;
    private LocalDateTime observationTime;
    private LocalDateTime productTime;
    private String detectedTime;

    private Double projectedCoordinateX;
    private Double projectedCoordinateY;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Boolean isDetected;
    private Boolean isProcessed;
    private Integer isDeleted;
    private Integer taskId;

    public Imageinfo convert2DO() {
        Imageinfo imageinfo = new Imageinfo();

        imageinfo.setFilename(filename);
        imageinfo.setPath(path);

        imageinfo.setImageWidth(imageWidth);
        imageinfo.setImageHeight(imageHeight);
        imageinfo.setImageBands(imageBands);

        imageinfo.setSatType("default");
        imageinfo.setImageType("default");
        imageinfo.setSensorType("default");

        imageinfo.setLongitude(longitude);
        imageinfo.setLatitude(latitude);

        imageinfo.setSpatialResolution(spatialResolution);
        imageinfo.setProjectedCoordinateX(projectedCoordinateX);
        imageinfo.setProjectedCoordinateY(projectedCoordinateY);

        return imageinfo;
    }
}
