package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.annotation.SysLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.PermissionDao;
import com.dao.UserDao;
import com.dao.user_hobbyDao;
import com.dto.UserDto;
import com.entity.*;
import com.service.UserService;
/*
import com.sun.xml.internal.bind.v2.model.core.ID;
*/
import com.utils.R;
import com.utils.UserUtil;
import net.minidev.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Ids;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private UserService userService;
	@Resource
	private UserDao userDao;
	@Resource
	private PermissionDao permissionDao;
	@Resource
	private user_hobbyDao user_hobbydao;

	@RequestMapping("/end")
	public R end(User user){
		String zt = user.getZt();
		String username = user.getUsername();
		int i = userService.updateZt(username,zt);
        if(i!=0){
	        if(zt.equals('1')){
		        return R.ok("开启成功");
	    }else{
		        return R.ok("关闭成功");
	    }
        }else{
	        return R.error("修改失败");
        }
	}


	@PutMapping(params = "headImgUrl")
	public void updateHeadImgUrl(String headImgUrl) {
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		if(headImgUrl != null && headImgUrl != ""){
		   user.setHeadImage(headImgUrl);
		}
		userService.updateUser(user);
		/*log.debug("{}修改了头像", user.getUsername());*/
	}

    @SysLog("新增用户")
	@RequestMapping("/add")
	public R saveUser(@RequestBody UserDto user) {
		User u = userService.getUser(user.getUsername());
		List<User> userList = userDao.getById(user.getId());
		String province = user.getPhidden();
		String city = user.getChidden();
		String dis = user.getDhidden();
		List<Integer> hobbyIds = user.getHobbyIds();
        List<Integer> permission = user.getPermissionIds();
		if(permission.size() != 0){
			userDao.saveUser_permission(user.getId(),user.getPermissionIds());
		}
        if(province != ""){
		  user.setAddress(province+"-"+city+"-"+dis);
        }else{
            user.setAddress("");
        }
		if (u != null) {
			return R.error(user.getUsername() + "已存在");
		}
		if(userList.size() != 0){
		    return R.error("ID已存在");
        }
		if(hobbyIds.size() != 0){

			userDao.saveUser_hobby(user.getId(),user.getHobbyIds());
		}
		if(hobbyIds.size() != 0){
			for(int i=hobbyIds.size()-1;i>=0;i--){
				if((hobbyIds.get(i)) ==1 || (hobbyIds.get(i)) ==2 || (hobbyIds.get(i)) ==3 || (hobbyIds.get(i)) ==4){
					hobbyIds.remove(i);
				}
			}
			List<String> hobbyList = userDao.getHobbyById(hobbyIds);
			String hobby = "";
			for(String name : hobbyList){
				hobby = name + "," + hobby;
			}
			user.setHobby(hobby);
		}
		if(userService.saveUser(user) != 0){

			return R.ok("保存成功");
		}
		return R.ok();
	}
    @RequestMapping("/checkPhone")
	@ResponseBody
	public String checkPhone(User user){
		String telephone = user.getTelephone();
		int i = userDao.checkPhoneUnique(telephone);
       if(i != 0){
       	   return "1";
	   }
       else{
       	  return "0";
	   }
	}

	@PutMapping
	@RequiresPermissions("sys:user:update")
	public int updateUser(@RequestBody User user) {
		return userService.updateUser(user);
	}

	@GetMapping("/current")
	public User currentUser() {
		return (User)SecurityUtils.getSubject().getPrincipal();
	}
	private String ret(Model model, String view) {
		return "/"+ view;
	}
	@PostMapping("/current")
	public User currentUser2(){
		return (User)SecurityUtils.getSubject().getPrincipal();
	}
	@SysLog("修改密码")
	@RequestMapping("/updatePassword")
	public R changePassword(String name, String oldPassword, String newPassword) {
        /*User u = userDao.getUser(name);*/
		User u = (User)SecurityUtils.getSubject().getPrincipal();
		if (u == null) {
			return R.error("用户不存在");
		}

		if (!u.getPassword().equals(userService.passwordEncoder(oldPassword,u.getSalt()))) {
			return R.error("密码错误");
		}

		userDao.changePassword(u.getId(), userService.passwordEncoder(newPassword,u.getSalt()));
		return R.ok();


	}
	@RequestMapping("/getUser")
	public User getUser(User user){
		User user1 = userService.getById(user.getId());
		return user1;
	}
	@SysLog("删除用户")
	@RequestMapping("/del")
	public R del(User user){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("id",user.getId());
		if(userDao.delete(queryWrapper) != 0){
			userDao.deleteHobbyById(user.getId());
			return R.ok("删除成功");
		}else{
			return R.error("删除失败");
		}

	}
    @SysLog("编辑用户")
	@RequestMapping("/edit")
	public R edit(@RequestBody UserDto user){
		String province = user.getPhidden();
		String city = user.getChidden();
		String dis = user.getDhidden();
		if(province != ""){
			user.setAddress(province+"-"+city+"-"+dis);
		}else{
			user.setAddress("");
		}
		List<Integer> hobbyIds = user.getHobbyIds();
		List<Integer> permissionIds = user.getPermissionIds();
		/*如果编辑之前有值则先删除再添加，如果编辑之前无值则直接添加（当然无值根本不可能进入编辑界面，代码未完成这样写不会报错）*/
		if(permissionIds.size() != 0){
			if((userDao.findByUid(user.getId())).size() != 0){
				if((userDao.deletePermissionById(user.getId()))!=0){
					userDao.saveUser_permission(user.getId(),permissionIds);
				}
			}else{
				userDao.saveUser_permission(user.getId(),permissionIds);
			}
		}
		if(hobbyIds.size() != 0 ){
			if((user_hobbydao.findByUid(user.getId())).size() != 0) {
				if ((userDao.deleteHobbyById(user.getId())) != 0) {
					userDao.saveUser_hobby(user.getId(), user.getHobbyIds());
				}
			}else{
				userDao.saveUser_hobby(user.getId(), user.getHobbyIds());
			}
		}
		if(hobbyIds.size() != 0){
			for(int i=hobbyIds.size()-1;i>=0;i--){
				if((hobbyIds.get(i)) ==1 || (hobbyIds.get(i)) ==2 || (hobbyIds.get(i)) ==3 || (hobbyIds.get(i)) ==4){
					hobbyIds.remove(i);
				}
			}
			List<String> hobbyList = userDao.getHobbyById(hobbyIds);
			String hobby = "";
			for(String name : hobbyList){
				hobby = name + "," + hobby;
			}
			hobby = hobby.substring(0,hobby.length()-1);
			user.setHobby(hobby);
		}

       if(userDao.update(user) == 0){
           return R.error("编辑失败");
       }else{
           return R.ok("编辑成功");
       }

	}
	@ResponseBody
	@RequestMapping("/getByPid")
	public String getByPid(String pid){
      int Pid = Integer.parseInt(pid);
      String json = JSONArray.toJSONString(userDao.findByPid(Pid));
	  return json;
	}
    @RequestMapping(value="/hobbyAll",method = RequestMethod.GET)
	@ResponseBody
	public JSONArray hobbyAll() {
		List<hobby> hobbyAll = userDao.hobbyAll();
		JSONArray array = new JSONArray();
		setHobbyTree(0, hobbyAll, array);
		return array;
	}

	@RequestMapping(value="/permissionAll",method = RequestMethod.GET)
	@ResponseBody
	public JSONArray permissionAll() {
		List<Permission> permissionAll = permissionDao.permissionAll();
		JSONArray array = new JSONArray();
		setPermissionTree(0, permissionAll, array);

		return array;
	}
	private void setPermissionTree(int pId, List<Permission> permissionsAll, JSONArray array) {
		for (Permission per : permissionsAll) {
			if (per.getPid() == pId) {
				String string = JSONObject.toJSONString(per);
				JSONObject parent = (JSONObject) JSONObject.parse(string);
				array.add(parent);
				if (permissionsAll.stream().filter(p -> p.getPid()==(per.getId())).findAny() != null) {
					JSONArray child = new JSONArray();
					parent.put("child", child);
					setPermissionTree(per.getId(), permissionsAll, child);
				}
			}
		}
	}
	@RequestMapping(value="/getPermissionIds",method = RequestMethod.GET)
	@ResponseBody
	public List<Integer> getPermissionIds(User_permission user_permission){
		List<Integer> permissionIds = permissionDao.getPermissionByUserIds(user_permission.getUserId());
		return permissionIds;
	}

	@RequestMapping(value="/getHobbyIds",method = RequestMethod.GET)
	@ResponseBody
	public List<Integer> getHobbyIds(user_hobby user_hobby1){
		List<Integer> hobbyids = userDao.getHobbyByUserId(user_hobby1.getUserId());
		return hobbyids;
	}
	private void setHobbyTree(int pId, List<hobby> permissionsAll, JSONArray array) {
		for (hobby per : permissionsAll) {
			if (per.getPid() == pId) {
				String string = JSONObject.toJSONString(per);
				JSONObject parent = (JSONObject) JSONObject.parse(string);
				array.add(parent);
				if (permissionsAll.stream().filter(p -> p.getPid()==(per.getId())).findAny() != null) {
					JSONArray child = new JSONArray();
					parent.put("child", child);
					setHobbyTree(per.getId(), permissionsAll, child);
				}
			}
		}
	}
}
