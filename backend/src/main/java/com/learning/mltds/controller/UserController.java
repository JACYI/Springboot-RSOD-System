package com.learning.mltds.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.mltds.controller.dto.UserDTO;
import com.learning.mltds.utils.JwtUtil;
import com.learning.mltds.utils.ResUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.learning.mltds.service.IUserService;
import com.learning.mltds.entity.User;

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
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private ResUtils resUtils;
//    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());


    @PostMapping
    public Boolean save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService.removeByIds(ids);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping("/page")
    public Page<User> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return userService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    // 用户登录
    @PostMapping("/login/")
    public Map<String, Object> login(@RequestBody Map<String, Object>requestBody, HttpSession httpSession) {

        String username = (String)requestBody.get("username");
        String password = (String)requestBody.get("password");

//        logger.info("user " + username + "请求登录");

        if(StrUtil.isBlank(username) || StrUtil.isBlank(password))
            return resUtils.makeResponse("Username or password error", "用户名或密码错误");;

        List<User> users = userService.listByMap(new HashMap<String, Object>(){{put("name", username);}});

        Map<String, Object> resultMap;

        if(users.size() == 1){
            User user = users.get(0);
//            Boolean isSuccess = jwtUtil.validatePassword(user.getPassword(), password);
            Boolean isSuccess = password.equals(user.getPassword());
            if (isSuccess) {
                Map<String, Object> result = new HashMap<>();
                String token = jwtUtil.sign(user.getName(), user.getId().toString());
                result.put("token", token);
                result.put("username", user.getName());
                resultMap = resUtils.makeResponse(result);
                System.out.println("用户"+user.getName()+"已登录");
            } else {
                resultMap = resUtils.makeResponse("Password error!", "密码错误");
            }
        }else
            resultMap = resUtils.makeResponse("Username error", "找不到用户");

        httpSession.setAttribute("loginUser", username);

        return resultMap;
    }
    // 用户退出登录
    @PostMapping("/logout/")
    public boolean logout() {
        System.out.println("用户已登出");
        return true;
    }
    // 用户注册
    @PostMapping("/register/")
    public boolean registerUser(@RequestBody User user) {
        String username = user.getName();
        String password = user.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password))
            return false;
        return this.save(user);
    }


}

