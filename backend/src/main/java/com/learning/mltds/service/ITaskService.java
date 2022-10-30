package com.learning.mltds.service;

import com.learning.mltds.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
public interface ITaskService extends IService<Task> {
    public Map<String, Object> makeTaskResult(Task task);
    public Map<String, Object> makeTaskResult(Task task, Map<String, Object> taskResult);
}
