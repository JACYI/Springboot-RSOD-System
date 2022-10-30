package com.learning.mltds.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

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
  @TableName("mltds_user")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;

    private String password;

    @TableField(insertStrategy = FieldStrategy.DEFAULT)
    private Integer authority;


    private LocalDateTime lastLoginTime;

    @TableField(insertStrategy = FieldStrategy.DEFAULT)
    private Integer isDeleted;

    @TableField(insertStrategy = FieldStrategy.DEFAULT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;


}
