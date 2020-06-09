package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Permission;
import com.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionDao extends BaseMapper<Permission> {
    List<Permission> permissionAll();
    List<Integer> getPermissionByUserIds(@Param("uid") Integer uid);
    List<Permission> getPermission(@Param("uid") Integer uid);
}
