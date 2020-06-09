package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.constants.UserConstants;
import com.dao.UserDao;
import com.dto.UserDto;
import com.entity.User;
import com.service.UserService;
import com.utils.R;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 * (Login)表服务实现类
 *
 * @author makejava
 * @since 2019-11-30 21:23:39
 */
@Service("UserService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserDao userDao;



    @Override
    public User getUser(String username) {
        User user = userDao.getUser(username);
        return user;
    }

    @Override
    public int saveUser(User user) {
        user.setSalt(new SecureRandomNumberGenerator().nextBytes().toString());
        user.setPassword(passwordEncoder(user.getPassword(),user.getSalt()));
        return userDao.saveUser(user);
    }

    @Override
    public int updateUser(User user) {

        return userDao.update(user);
    }

    /*@Override
    public R changePassword(String username, String oldPassword, String newPassword) {
        User u = userDao.getUser(username);
        if (u == null) {
            return R.error("用户不存在");
        }

        if (!u.getPassword().equals(oldPassword)) {
            return R.error("密码错误");
        }

        userDao.changePassword(u.getId(), newPassword);
        return R.ok();

    }*/
    /*@Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userDao.getUser(username);
        user.setPassword(newPassword);
        return update(user, new QueryWrapper<User>().eq("username", username).eq("password", oldPassword));
    }*/

    @Override
    public List<User> selectAll(int page,int limit) {
        return userDao.selectAll(page,limit);
    }

    @Override
    public String passwordEncoder(String credentials, String salt) {
        Object object = new SimpleHash("md5", credentials, salt, UserConstants.HASH_ITERATIONS);
        return object.toString();
    }
    public int updateZt(String username,String zt){
        return userDao.updateZt(username,zt);
    }


}