package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.mltds.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.IGlobalobjectinfoService;
import com.learning.mltds.entity.Globalobjectinfo;

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
@RequestMapping("/globalobjectinfo")
public class GlobalobjectinfoController {
    @Resource
    private IGlobalobjectinfoService globalobjectinfoService;

    @PostMapping
    public Boolean save(@RequestBody Globalobjectinfo globalobjectinfo) {
        return globalobjectinfoService.saveOrUpdate(globalobjectinfo);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return globalobjectinfoService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return globalobjectinfoService.removeByIds(ids);
    }

    @GetMapping
    public List<Globalobjectinfo> findAll() {
        return globalobjectinfoService.list();
    }

    @GetMapping("/{id}")
    public Globalobjectinfo findOne(@PathVariable Integer id) {
        return globalobjectinfoService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Globalobjectinfo> findPage(@RequestParam Integer pageNum,
//                                           @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return globalobjectinfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

