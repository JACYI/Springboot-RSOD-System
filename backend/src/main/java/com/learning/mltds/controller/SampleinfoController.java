package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.mltds.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.ISampleinfoService;
import com.learning.mltds.entity.Sampleinfo;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
@RestController
@RequestMapping("/sampleinfo")
public class SampleinfoController {
    @Resource
    private ISampleinfoService sampleinfoService;

    @PostMapping
    public Boolean save(@RequestBody Sampleinfo sampleinfo) {
        return sampleinfoService.saveOrUpdate(sampleinfo);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return sampleinfoService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return sampleinfoService.removeByIds(ids);
    }

    @GetMapping
    public List<Sampleinfo> findAll() {
        return sampleinfoService.list();
    }

    @GetMapping("/{id}")
    public Sampleinfo findOne(@PathVariable Integer id) {
        return sampleinfoService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Sampleinfo> findPage(@RequestParam Integer pageNum,
//                                     @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return sampleinfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

