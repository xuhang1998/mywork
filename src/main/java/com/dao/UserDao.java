package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * (Login)表数据库访问层
 *
 * @author makejava
 * @since 2019-11-30 21:23:39
 */

public interface UserDao extends BaseMapper<User> {
       User getUser(@Param("name") String username);

       int changePassword(@Param("id") Integer id, @Param("password") String password);

       List<User> getById(@Param("id")Integer id);
       int update(@Param("user") User user);
       List<User> selectAll(@Param("page") Integer page,@Param("limit")Integer limit);
       int saveUser(@Param("user") User user);
       List<PCD> findByPid(@Param("pid") Integer pid);
       List<hobby> hobbyAll();


       List<String> getHobbyById(@Param("hobbyIds") List<Integer> hobbyIds);
       int saveUser_hobby(@Param("uid") Integer uid,@Param("hid") List<Integer> hid);
       int saveUser_permission(@Param("uid") Integer uid,@Param("pid") List<Integer> pid);
       List<Integer> getHobbyByUserId(@Param("uid") Integer uid);
       int deleteHobbyById(@Param("uid")Integer uid);
       int deletePermissionById(@Param("uid")Integer uid);
       List<User_permission> findByUid(@Param("uid") Integer uid);
       int updateZt(@Param("u") String username,@Param("zt") String zt);

       List<String> getAllName();
       int checkPhoneUnique(@Param("t") String telephone);
}