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
  @TableName("mltds_globalobjectinfo")
public class Globalobjectinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String classname;

    private String typename;

    private String nation;

    private String shipName;

    private String shipNumber;

    private String equipment;

    private String power;

    private String detectionMethod;

    private String organization;

    private String otherInfo;

    private String examplePath;

    private Double speed;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;


}
