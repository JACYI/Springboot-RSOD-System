package com.learning.mltds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.mltds.controller.dto.UserDTO;
import com.learning.mltds.entity.User;
import com.learning.mltds.mapper.UserMapper;
import com.learning.mltds.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    public boolean login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User user = getOne(queryWrapper);
        if (user != null){
            System.out.println("login success");
            user.setLastLoginTime(LocalDateTime.now());
            System.out.println(user);
            updateById(user);
        }
        return user != null;
    }
}
