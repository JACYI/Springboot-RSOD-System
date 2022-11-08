package com.learning.mltds.entity;

import com.baomidou.mybatisplus.annotation.*;

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
  @TableName("mltds_objectinfo")
public class Objectinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Double confidence;

    private String classname;

    private String typename;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private String shipNumber;  //弦号

    private LocalDateTime detectedTime;

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
    private LocalDateTime createTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Integer isDeleted;

    private Integer imageId;

    private Integer taskId;


}
