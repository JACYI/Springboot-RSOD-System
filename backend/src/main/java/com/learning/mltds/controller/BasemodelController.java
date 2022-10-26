package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.IBasemodelService;
import com.learning.mltds.entity.Basemodel;

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
@RequestMapping("/basemodel")
public class BasemodelController {
    @Resource
    private IBasemodelService basemodelService;

    @PostMapping
    public Boolean save(@RequestBody Basemodel basemodel) {
        return basemodelService.saveOrUpdate(basemodel);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return basemodelService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return basemodelService.removeByIds(ids);
    }

    @GetMapping
    public List<Basemodel> findAll() {
        return basemodelService.list();
    }

    @GetMapping("/{id}")
    public Basemodel findOne(@PathVariable Integer id) {
        return basemodelService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Basemodel> findPage(@RequestParam Integer pageNum,
//                                    @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return basemodelService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

