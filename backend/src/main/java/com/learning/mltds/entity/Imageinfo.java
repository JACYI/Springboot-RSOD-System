package com.learning.mltds.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Getter
@Setter
  @TableName("mltds_imageinfo")
public class Imageinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String filename;

    private Double latitude;

    private Double longitude;

    private Integer imageWidth;

    private Integer imageHeight;

    private Double spatialResolution;

    private Integer imageBands;

    private String imageType;

    private String satType;

    private String sensorType;

    private String path;

    private LocalDateTime observationTime;

    private LocalDateTime productTime;

    private LocalDateTime detectedTime;

    private Double projectedCoordinateX;

    private Double projectedCoordinateY;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isDetected;

    private Boolean isProcessed;

    private Integer isDeleted;

    private Integer taskId;


}
