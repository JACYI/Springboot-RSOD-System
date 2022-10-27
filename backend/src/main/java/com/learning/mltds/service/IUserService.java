package com.learning.mltds.service;

import com.learning.mltds.controller.dto.UserDTO;
import com.learning.mltds.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
public interface IUserService extends IService<User> {

    boolean login(UserDTO userDTO);
}
