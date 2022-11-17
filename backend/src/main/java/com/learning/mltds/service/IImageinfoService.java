package com.learning.mltds.service;

import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ImageinfoDTO;
import com.learning.mltds.entity.Imageinfo;
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
public interface IImageinfoService extends IService<Imageinfo> {
    List<DetectionResultDTO> getDetectionImagesByTaskId(Integer taskId);
    boolean deleteImageInfo(String imageName);
    boolean deleteImageInfo(Integer userId, String imageName);

    Integer saveImageInfoFromMap(ImageinfoDTO imageinfoDTO);
}
