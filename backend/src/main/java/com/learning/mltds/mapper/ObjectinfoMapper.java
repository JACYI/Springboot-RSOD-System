package com.learning.mltds.mapper;

import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.entity.Objectinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
@Mapper
public interface ObjectinfoMapper extends BaseMapper<Objectinfo> {
    void saveObjectInfo(Map<String, Object> map);
    void saveObjectInfoDTO(ObjectinfoDTO objectinfoDTO);
}
