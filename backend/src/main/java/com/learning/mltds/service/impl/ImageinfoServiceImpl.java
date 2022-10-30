package com.learning.mltds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.mltds.entity.Imageinfo;
import com.learning.mltds.entity.Objectinfo;
import com.learning.mltds.mapper.ImageinfoMapper;
import com.learning.mltds.service.IImageinfoService;
import com.learning.mltds.service.IObjectinfoService;
import com.learning.mltds.utils.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    @Resource
    private IObjectinfoService objectinfoService;
    @Resource
    private IImageinfoService imageinfoService;

    public Map<String, Object> getDetectionImagesByTaskId(Integer taskId) {
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

    /**
     * 根据imageinfo ids 查询所有"image_id"在ids中的objectinfo
     * @param imageinfosIds 查询图像的数据库id集合
     * @return Map {图像1名: {"image_info": {}, "object_infos": {} }, ... }
     */
    public Map<String, Object> getDetectionMap(Collection<Integer> imageinfosIds) {
        Map<String, Object> result = new HashMap<>();

        for(Integer imageinfoId:imageinfosIds){
            // 根据id获取 imageinfo 对象并转化为 Map
            Imageinfo imageinfo = imageinfoService.getById(imageinfoId);
            // 根据 id 获取对应的检测结果
            QueryWrapper<Objectinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("image_id", imageinfoId);
            List<Objectinfo> objectinfos = objectinfoService.list(queryWrapper);
            imageinfo.setIsDetected(objectinfos.isEmpty() ? Boolean.FALSE : Boolean.TRUE);

            // imageinfo 对象并转化为 Map 对象
            Map<String, Object> imageinfoMap = MapUtils.entityToMap(imageinfo);
            Map<String, Object> imageRes = new HashMap<>();
            imageRes.put("image_info", imageinfoMap);
            imageRes.put("object_infos", objectinfos);
            result.put(imageinfo.getFilename(), imageRes);
        }

        return result;
    }



    public static void main(String[] args) {

    }
}
