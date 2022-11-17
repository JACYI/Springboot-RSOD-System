package com.learning.mltds.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.entity.Task;
import com.learning.mltds.mapper.TaskMapper;
import com.learning.mltds.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    // 将 task 实体对象转为 Map
    public Map<String, Object> makeTaskResult(Task task) {
        Map<String, Object> res = new HashMap<>();
        res.put("task_id", task.getId());
        res.put("name", task.getName());
        res.put("category", task.getCategory());
        res.put("info", task.getId());
        res.put("status", task.getStatus());
        res.put("progress", task.getProgress());
        res.put("code", task.getCode());

        return res;
    }
    public Map<String, Object> makeTaskResult(Task task, List<DetectionResultDTO> taskResult) {
        // 转换为前端需要的格式
        Map<String, Object> jsonResultMap = new HashMap<>();
        for(DetectionResultDTO detectionResultDTO : taskResult) {
            jsonResultMap.put(detectionResultDTO.getImageName(), detectionResultDTO);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("task_id", task.getId());
        res.put("name", task.getName());
        res.put("category", task.getCategory());
        res.put("info", task.getId());
        res.put("status", task.getStatus());
        res.put("progress", task.getProgress());
        res.put("code", task.getCode());
        res.put("task_result", jsonResultMap);
        return res;
    }


}
