package com.learning.mltds.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.dto.SearchConditionDTO;
import com.learning.mltds.entity.Objectinfo;
import com.learning.mltds.mapper.ObjectinfoMapper;
import com.learning.mltds.service.IObjectinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
@Service
public class ObjectinfoServiceImpl extends ServiceImpl<ObjectinfoMapper, Objectinfo> implements IObjectinfoService {
    @Resource
    private ObjectinfoMapper objectinfoMapper;
    @Override
    public Boolean saveObjectInfoDTOS(List<ObjectinfoDTO> objectinfoDTOS) {
        for(ObjectinfoDTO objectinfoDTO:objectinfoDTOS){
            try {
                objectinfoMapper.saveObjectInfoDTO(objectinfoDTO);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    // 根据页码和条件搜索objectinfo
    @Override
    public IPage<Objectinfo> pageSearch(IPage<Objectinfo> page, SearchConditionDTO condition, Boolean order) {
        return objectinfoMapper.getAllPage(page, condition, order);
//        for(Objectinfo objectinfo : results) {
//
//        }
//        List<ObjectinfoVO>
//        return listPage;
    }

    @Override
    public void turnObjectInfoSepcialValue(Map<String, Object> objectInfo) {
        if(objectInfo.containsKey("bbox")){
            List<Integer> bbox = (List<Integer>) objectInfo.get("bbox");
            objectInfo.put("bboxP1X", bbox.get(0));
            objectInfo.put("bboxP1Y", bbox.get(1));
            objectInfo.put("bboxP2X", bbox.get(2));
            objectInfo.put("bboxP2Y", bbox.get(3));
            objectInfo.put("bboxP3X", bbox.get(4));
            objectInfo.put("bboxP3Y", bbox.get(5));
            objectInfo.put("bboxP4X", bbox.get(6));
            objectInfo.put("bboxP4Y", bbox.get(7));
            objectInfo.remove("bbox");
        }
        if(objectInfo.containsKey("geoBbox")){
            List<Double> geoBbox = (List<Double>) objectInfo.get("geoBbox");
            objectInfo.put("geoBboxP1X", geoBbox.get(0));
            objectInfo.put("geoBboxP1Y", geoBbox.get(1));
            objectInfo.put("geoBboxP2X", geoBbox.get(2));
            objectInfo.put("geoBboxP2Y", geoBbox.get(3));
            objectInfo.put("geoBboxP3X", geoBbox.get(4));
            objectInfo.put("geoBboxP3Y", geoBbox.get(5));
            objectInfo.put("geoBboxP4X", geoBbox.get(6));
            objectInfo.put("geoBboxP4Y", geoBbox.get(7));
            objectInfo.remove("geoBbox");
        }
        if(objectInfo.containsKey("imageCenter")){
            List<Integer> imageCenter = (List<Integer>) objectInfo.get("imageCenter");
            objectInfo.put("imageCenterX", imageCenter.get(0));
            objectInfo.put("imageCenterY", imageCenter.get(1));
            objectInfo.remove("imageCenter");
        }
        if(objectInfo.containsKey("geoCenter")){
            List<Double> geoCenter = (List<Double>) objectInfo.get("geoCenter");
            objectInfo.put("geoCenterLongitude", geoCenter.get(0));
            objectInfo.put("geoCenterLatitude", geoCenter.get(1));
            objectInfo.remove("geoCenter");
        }
    }

}
