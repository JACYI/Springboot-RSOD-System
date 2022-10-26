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
  @TableName("mltds_sampleinfo")
public class Sampleinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String path;

    private Integer detectionImageNumber;

    private Integer detectionObjectNumber;

    private Integer classificationImageNumber;

    private Integer classificationObjectNumber;

    private Boolean isBalance;

    private Boolean isTrained;

    private LocalDateTime trainTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;

    private Integer imageId;

    private Integer taskId;


}
