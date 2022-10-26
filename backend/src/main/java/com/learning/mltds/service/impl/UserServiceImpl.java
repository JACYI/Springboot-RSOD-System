package com.learning.mltds.service.impl;

import com.learning.mltds.entity.User;
import com.learning.mltds.mapper.UserMapper;
import com.learning.mltds.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
