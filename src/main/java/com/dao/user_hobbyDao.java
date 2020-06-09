package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.User;
import com.entity.user_hobby;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface user_hobbyDao extends BaseMapper<User> {
    List<user_hobby> findByUid(@Param("uid") Integer uid);
}
