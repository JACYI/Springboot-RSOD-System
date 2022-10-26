package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.learning.mltds.service.ITaskService;
import com.learning.mltds.entity.Task;

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
@RequestMapping("/task")
public class TaskController {
    @Resource
    private ITaskService taskService;

    @PostMapping
    public Boolean save(@RequestBody Task task) {
        return taskService.saveOrUpdate(task);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return taskService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return taskService.removeByIds(ids);
    }

    @GetMapping
    public List<Task> findAll() {
        return taskService.list();
    }

    @GetMapping("/{id}")
    public Task findOne(@PathVariable Integer id) {
        return taskService.getById(id);
    }

//    @GetMapping("/page")
//    public Page<Task> findPage(@RequestParam Integer pageNum,
//                               @RequestParam Integer pageSize) {
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return taskService.page(new Page<>(pageNum, pageSize), queryWrapper);
//    }



}

