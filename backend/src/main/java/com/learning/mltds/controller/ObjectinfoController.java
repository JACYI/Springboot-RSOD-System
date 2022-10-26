package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.IObjectinfoService;
import com.learning.mltds.entity.Objectinfo;

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
@RequestMapping("/objectinfo")
public class ObjectinfoController {
    @Resource
    private IObjectinfoService objectinfoService;

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

