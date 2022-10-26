package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.IImageinfoService;
import com.learning.mltds.entity.Imageinfo;

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
@RequestMapping("/imageinfo")
public class ImageinfoController {
    @Resource
    private IImageinfoService imageinfoService;

    @PostMapping
    public Boolean save(@RequestBody Imageinfo imageinfo) {
        return imageinfoService.saveOrUpdate(imageinfo);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return imageinfoService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return imageinfoService.removeByIds(ids);
    }

    @GetMapping
    public List<Imageinfo> findAll() {
        return imageinfoService.list();
    }

    @GetMapping("/{id}")
    public Imageinfo findOne(@PathVariable Integer id) {
        return imageinfoService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Imageinfo> findPage(@RequestParam Integer pageNum,
//                                    @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return imageinfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

