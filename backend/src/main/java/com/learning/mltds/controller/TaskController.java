package com.learning.mltds.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.mltds.entity.Task;
import com.learning.mltds.entity.User;
import com.learning.mltds.service.ITaskService;
import com.learning.mltds.utils.ResUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private ResUtils resUtils;

    private static final int pageSize = 10;

//    @RequestMapping("/task/browse-tasks/")
//    @ResponseBody
//    public Map<String, Object> browseTasks(@RequestBody Map<String, Object> requestBody, HttpSession httpSession, @CookieValue("userId") Integer cookieUserId){
//
//        Integer userId= cookieUserId;
//
//        String taskCategory = (String) requestBody.get("taskCategory");
//        String taskStatus = (String) requestBody.get("taskStatus");
//        Integer isConfirmed = (Integer) requestBody.get("isConfirmed");
//        String taskIdentifier = (String) requestBody.get("taskIdentifier");
//        Integer page = (Integer) requestBody.get("page");
//        String order = (String) requestBody.get("order");
//
//        if(taskStatus != null && taskStatus.equals("ALL")){
//            taskStatus = null;
//        }
//
//
//        Map<String, Object> searchMap = new HashMap<>();
//        searchMap.put("category", taskCategory);
//        searchMap.put("status", taskStatus);
//        searchMap.put("isConfirmed", isConfirmed);
//        searchMap.put("identifier", taskIdentifier);
//        searchMap.put("isDeleted", 0);
//
//
//        Integer start = (page - 1) * eachTaskPageNums;
//        Map<String, Object> orderMap = new HashMap<>();
//        order = daoUtil.underlineToCamel(order);
//        if(order.contains("-")){
//            orderMap.put("sign", "-");
//            orderMap.put("val", order.substring(1));
//        }
//        else{
//
//            orderMap.put("sign", "+");
//            orderMap.put("val", order);
//        }
//
//        // 分页查询满足条件的任务
//        List<Task> taskInfos  = taskService.searchTaskPage(start, eachTaskPageNums, searchMap, userId, orderMap);
////        for(Task taskInfo: taskInfos){
////            System.out.println(taskInfo);
////        }
//        // 计算查询到的任务数量
//        Integer counts = taskService.countTaskNums(searchMap);
//        Map<String, Object> resultMap = daoUtil.makeResponse(new HashMap<String, Object>(){{put("task_infos", taskInfos); put("pages", Math.ceil(counts.doubleValue() / eachTaskPageNums.doubleValue()));}});
//        return resultMap;
//
//    }

//    @PostMapping
//    public Boolean save(@RequestBody Task task) {
//        return taskService.saveOrUpdate(task);
//    }
//
//    @DeleteMapping("/{id}")
//    public Boolean delete(@PathVariable Integer id) {
//        return taskService.removeById(id);
//    }
//
//    @DeleteMapping("/del/batch")
//    public boolean deleteBatch(@RequestBody List<Integer> ids) {
//        return taskService.removeByIds(ids);
//    }
//
//    @GetMapping
//    public List<Task> findAll() {
//        return taskService.list();
//    }
//
//    @GetMapping("/{id}")
//    public Task findOne(@PathVariable Integer id) {
//        return taskService.getById(id);
//    }

    @PostMapping("/browse-tasks/")
    public Map<String, Object> findPage(@RequestBody Map<String, Object> requestBody,
            HttpSession httpSession/**, @CookieValue("userId") Integer cookieUserId*/) {

//        Integer userId= cookieUserId;
        String taskCategory = (String) requestBody.get("taskCategory");
        String taskStatus = (String) requestBody.get("taskStatus");
        Integer isConfirmed = (Integer) requestBody.get("isConfirmed");
        String taskIdentifier = (String) requestBody.get("taskIdentifier");
        Integer pageNum = (Integer) requestBody.get("page");
        String order = (String) requestBody.getOrDefault("order", "-id");
        if(taskStatus != null && taskStatus.equals("ALL")){
            taskStatus = null;
        }


        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq(taskCategory != null,"category", taskCategory);
        queryWrapper.eq(taskStatus!= null, "status", taskStatus);
//        queryWrapper.eq("task_user_id", cookieUserId);
        queryWrapper.eq(isConfirmed!= null,"is_confirmed", isConfirmed);
        queryWrapper.eq(taskIdentifier!=null, "identifier", taskIdentifier);

        IPage<Task> queryResult = taskService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return resUtils.makeResponse(new HashMap<String, Object>(){{put("task_infos", queryResult.getRecords()); put("pages", queryResult.getPages());}});

    }

    @PostMapping("stop/")
    public Map<String, Object> stopTask(@RequestBody Map<String, Object> requestBody) {
        Integer taskId = (Integer) requestBody.get("task_id");
        Task task = taskService.getById(taskId);
//        Integer userId = requestBody.session.get(user_id)

        Map<String, Object> response;

        if(task.getCode().equals(""))
            response = resUtils.makeResponse("Error", "无法中止当前任务");

        // 判断执行任务和中止任务是否为同一人
//        else if(task.getTaskUserId() == userId)

        // 能够中止任务
        UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", "FAILURE");
        updateWrapper.set("info", "任务中止");
        updateWrapper.set("progress", 100);

        taskService.update(updateWrapper);
        return resUtils.makeResponse("任务已经中止");
    }


}

