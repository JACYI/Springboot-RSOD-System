package com.learning.mltds.mapper;

import com.learning.mltds.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}
