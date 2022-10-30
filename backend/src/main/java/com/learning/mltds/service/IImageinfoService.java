package com.learning.mltds.service;

import com.learning.mltds.entity.Imageinfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
public interface IImageinfoService extends IService<Imageinfo> {
    Map<String, Object> getDetectionImagesByTaskId(Integer taskId);
}
