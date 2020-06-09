package com.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.dto.UserDto;
import com.entity.User;

import java.util.List;

/**
 * (Login)表服务接口
 *
 * @author makejava
 * @since 2019-11-30 21:23:39
 */
public interface UserService extends IService<User> {

       User getUser(String username);
       int saveUser(User user);

       int updateUser(User user);

       List<User> selectAll(int page,int limit);
       /*盐值加密*/
       String passwordEncoder(String credentials, String salt);
       int updateZt(String username,String zt);


}