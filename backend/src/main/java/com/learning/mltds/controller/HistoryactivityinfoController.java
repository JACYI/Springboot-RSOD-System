package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.IHistoryactivityinfoService;
import com.learning.mltds.entity.Historyactivityinfo;

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
@RequestMapping("/historyactivityinfo")
public class HistoryactivityinfoController {
    @Resource
    private IHistoryactivityinfoService historyactivityinfoService;

    @PostMapping
    public Boolean save(@RequestBody Historyactivityinfo historyactivityinfo) {
        return historyactivityinfoService.saveOrUpdate(historyactivityinfo);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return historyactivityinfoService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return historyactivityinfoService.removeByIds(ids);
    }

    @GetMapping
    public List<Historyactivityinfo> findAll() {
        return historyactivityinfoService.list();
    }

    @GetMapping("/{id}")
    public Historyactivityinfo findOne(@PathVariable Integer id) {
        return historyactivityinfoService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Historyactivityinfo> findPage(@RequestParam Integer pageNum,
//                                              @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return historyactivityinfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

