package com.learning.mltds.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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
    Map<String, Object> makeTaskResult(Task task);
    Map<String, Object> makeTaskResult(Task task, List<DetectionResultDTO> taskResult);
}
