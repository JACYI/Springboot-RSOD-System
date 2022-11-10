package com.learning.mltds.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.dto.SearchConditionDTO;
import com.learning.mltds.entity.Objectinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
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
    void saveObjectInfo(ObjectinfoDTO objectinfo);
//    void saveObjectInfoDTO(ObjectinfoDTO objectinfoDTO);

//    List<Objectinfo> searchObjectinfo(SearchConditionDTO condition, Integer limitFirst, Integer limitSecond);

    IPage<Objectinfo> getAllPage(IPage<Objectinfo> page, SearchConditionDTO condition, Boolean order);

    void deleteObjectinfoByFilename(String imageName);
}
