package com.learning.mltds.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.dto.SearchConditionDTO;
import com.learning.mltds.entity.Objectinfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
public interface IObjectinfoService extends IService<Objectinfo> {

    Boolean saveObjectInfos(List<ObjectinfoDTO> objectinfoDTOS);

    void turnObjectInfoSepcialValue(Map<String, Object> objectInfo);

    IPage<Objectinfo> pageSearch(IPage<Objectinfo> page, SearchConditionDTO conditionDTO, Boolean order);
}
