package com.learning.mltds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ImageinfoDTO;
import com.learning.mltds.entity.Imageinfo;
import com.learning.mltds.entity.Objectinfo;
import com.learning.mltds.mapper.ImageinfoMapper;
import com.learning.mltds.service.IImageinfoService;
import com.learning.mltds.service.IObjectinfoService;
import com.learning.mltds.utils.MapUtils;
import com.learning.mltds.vo.ObjectinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
@Service
public class ImageinfoServiceImpl extends ServiceImpl<ImageinfoMapper, Imageinfo> implements IImageinfoService {
    @Autowired
    private IObjectinfoService objectinfoService;
    @Autowired
    private IImageinfoService imageinfoService;

    @Override
    public List<DetectionResultDTO> getDetectionImagesByTaskId(Integer taskId) {
        // 找到具有该id的任务中包含的所有图像信息，查询出多个Objectinfo对象
        QueryWrapper<Objectinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        List<Objectinfo> objectinfos = objectinfoService.list(queryWrapper);

        Set<Integer> imageinfoIds = new HashSet<>();
//         如果查询数据库没有结果，返回空集合
        if (objectinfos == null || objectinfos.isEmpty())
            return getDetectionMap(imageinfoIds);

        for(Objectinfo objectinfo:objectinfos)
            imageinfoIds.add(objectinfo.getImageId());

        return getDetectionMap(imageinfoIds);
    }

    @Override
    public boolean deleteImageInfo(String imageName) {
        return this.deleteImageInfo(null, imageName);
    }

    @Override
    public boolean deleteImageInfo(Integer userId, String imageName) {
        QueryWrapper<Imageinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId!=null, "user_id", userId);
        queryWrapper.eq(imageName!=null, "filename", imageName);
        return imageinfoService.remove(queryWrapper);
    }

    @Override
    public Integer saveImageInfoFromMap(ImageinfoDTO imageinfoDTO) {
        Imageinfo imageinfo = new Imageinfo();
        imageinfo.setFilename(imageinfoDTO.getFilename());
        imageinfo.setSatType(imageinfoDTO.getSatType());
        imageinfo.setSensorType(imageinfoDTO.getSensorType());
        imageinfo.setImageWidth(imageinfoDTO.getImageWidth());
        imageinfo.setImageHeight(imageinfoDTO.getImageHeight());
        imageinfo.setPath(imageinfoDTO.getPath());
        imageinfo.setIsDetected(imageinfoDTO.getIsDetected());
        imageinfo.setTaskId(imageinfoDTO.getTaskId());
        imageinfo.setDetectedTime(imageinfoDTO.getDetectedTime());

        return imageinfoService.save(imageinfo) ? imageinfo.getId() : -1;
    }
//    public Integer saveImageInfoFromMap(Map<String, Object> imageinfoMap) {
//        Imageinfo imageinfo = new Imageinfo();
//        imageinfo.setFilename(imageinfoMap.get("filename"));
//        imageinfo.setSatType( (String) imageinfoMap.get("sat_type"));
//        imageinfo.setSensorType( (String) imageinfoMap.get("sensor_type"));
//        imageinfo.setImageWidth( (Integer) imageinfoMap.get("image_width"));
//        imageinfo.setImageHeight( (Integer) imageinfoMap.get("im_height"));
//        imageinfo.setPath( (String) imageinfoMap.get("path"));
//        imageinfo.setIsDetected( (Boolean) imageinfoMap.get("is_detected"));
//        imageinfo.setTaskId( (Integer) imageinfoMap.get("task_id"));
//        imageinfo.setDetectedTime(LocalDateTime.now());
//
//        return imageinfoService.save(imageinfo) ? imageinfo.getId() : -1;
//    }

    /**
     * 根据 imageinfo ids 查询所有 "image_id"在ids中的objectinfo
     * @param imageinfosIds 查询图像的数据库id集合
     * @return Map {图像1名: {"image_info": {}, "object_infos": {} }, ... }
     */
    public List<DetectionResultDTO> getDetectionMap(Collection<Integer> imageinfosIds) {
        List<DetectionResultDTO> result = new ArrayList<>();

        for(Integer imageinfoId:imageinfosIds){
            // 根据id获取 imageinfo 对象并转化为 Map
            Imageinfo imageinfo = imageinfoService.getById(imageinfoId);
            // 根据 id 获取对应的检测结果
            QueryWrapper<Objectinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("image_id", imageinfoId);
            List<Objectinfo> objectinfos = objectinfoService.list(queryWrapper);

            // objectinfo -> objectinfoVO
            List<ObjectinfoVO> objectinfoVOS = new ArrayList<>();
            for(Objectinfo obj : objectinfos){
                // 找出objectinfo 对应的imagePath
                Imageinfo one = imageinfoService.getById(obj.getImageId());
                String imagePath = one.getPath();

                objectinfoVOS.add(obj.convert2VO(imagePath));
            }
            // 驼峰转下划线

            imageinfo.setIsDetected(objectinfos.isEmpty() ? Boolean.FALSE : Boolean.TRUE);
            DetectionResultDTO detectionDTO = new DetectionResultDTO();
            detectionDTO.setImageName(imageinfo.getFilename());
            detectionDTO.setImageInfo(imageinfo.convert2DTO());
            detectionDTO.setObjectInfos(objectinfoVOS);
            result.add(detectionDTO);

//            // imageinfo 对象并转化为 Map 对象
//            Map<String, Object> imageinfoMap = MapUtils.entityToMap(imageinfo);
//            // objectinfo 对象转为 Map 对象
//            List<Map<String, Object>> objectinfoMaps = MapUtils.entitysToMap(objectinfos);
//            // response data: { <imageName>: {"image_info": {}, "object_info": {} }, ... }
//            Map<String, Object> imageRes = new HashMap<>();
//            imageRes.put("image_info", imageinfoMap);
//            imageRes.put("object_infos", objectinfoMaps);
//            result.put(imageinfo.getFilename(), imageRes);
        }

        return result;
    }



    public static void main(String[] args) {

    }
}
