package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ObjectinfoDTO;
import com.learning.mltds.dto.SearchConditionDTO;
import com.learning.mltds.mapper.ObjectinfoMapper;
import com.learning.mltds.utils.ReqUtils;
import com.learning.mltds.utils.ResUtils;
import com.learning.mltds.vo.ObjectinfoVO;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.learning.mltds.service.IObjectinfoService;
import com.learning.mltds.entity.Objectinfo;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
*     /detection-management
 * </p>
 *  历史检测识别结果管理后端处理
 * @author yiyonghoa
 * @since 2022-10-26
 */
@RestController
@RequestMapping("/object-info")
public class ObjectinfoController {
    private static final Integer pageSize = 10;
    @Resource
    private IObjectinfoService objectinfoService;
    @Resource
    private ObjectinfoMapper objectinfoMapper;

    @PostMapping("search/")
    public Map<String, Object> search(@RequestBody Map<String, Object> requestBody) {
        Integer pageNum = (Integer) requestBody.get("page");
        // 是否按倒序，true表示倒序,false表示正序
        Boolean order = (requestBody.getOrDefault("order", "-id")).equals("-id");
        SearchConditionDTO condition = ReqUtils.searchConditionConvert(requestBody.get("form"));

        // 获取查询结果，使用IPage拦截器获取结果，
        IPage<Objectinfo> searchResult = objectinfoService.pageSearch(
                new Page<>(pageNum, pageSize), condition, order);

        // DAO转化为VO返回response
        List<Objectinfo> objectinfoResults = searchResult.getRecords();
        List<ObjectinfoVO> objectinfoVOS = new ArrayList<>();
        for(Objectinfo objectinfo : objectinfoResults)
            objectinfoVOS.add(objectinfo.convert2VO());

        return ResUtils.makeResponse(objectinfoVOS, searchResult.getPages());
    }

    @PostMapping("update/")
    public Map<String, Object> update(@RequestBody Map<String, Object> requestBody) {
        List<ObjectinfoDTO> objectinfoDTOS = ReqUtils.updateResultsConvert(requestBody);
        try {
            if(!objectinfoService.saveObjectInfoDTOS(objectinfoDTOS))
                throw new Exception("保存结果失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResUtils.makeResponse("Error", "保存结果错误");
        }
        return ResUtils.makeResponse();
    }




    @PostMapping
    public Boolean save(@RequestBody Objectinfo objectinfo) {
        return objectinfoService.saveOrUpdate(objectinfo);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return objectinfoService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return objectinfoService.removeByIds(ids);
    }

    @GetMapping
    public List<Objectinfo> findAll() {
        return objectinfoService.list();
    }

    @GetMapping("/{id}")
    public Objectinfo findOne(@PathVariable Integer id) {
        return objectinfoService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Objectinfo> findPage(@RequestParam Integer pageNum,
//                                     @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return objectinfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

