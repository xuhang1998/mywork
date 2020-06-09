package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.annotation.SysLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;

import com.constants.UserConstants;
import com.dao.UserDao;
import com.entity.User;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.service.UserService;

import com.utils.PageResult;
import com.utils.R;
import com.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * (Login)表控制层
 *
 * @author makejava
 * @since 2019-11-30 21:23:39
 */

@Controller
@RequestMapping
public class LoginController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private RedisTemplate<String,List<User>> redisTemplate;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @Autowired
    private Producer producer;
    @ResponseBody
    @RequestMapping("/code")
    public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);

    }

    @ResponseBody
    @SysLog("用户登录")
    @RequestMapping("/yzlogin")
    public R yzlogin(String username,String password,String validateCode) {
        String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if(!validateCode.equalsIgnoreCase(kaptcha)){
            return R.error("验证码不正确");
        }
        try{
            User user = userService.getUser(username);
            if(user == null){
                return R.error("用户名不存在");
            }
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken =
                    new UsernamePasswordToken(username,userService.passwordEncoder(password,user.getSalt()));
            subject.login(usernamePasswordToken);
            // 设置shiro的session过期时间
            SecurityUtils.getSubject().getSession().setTimeout(serverProperties.getServlet().getSession().getTimeout().toMillis());

        }catch(UnknownAccountException e){

            return R.error("用户名不存在");
        }catch(IncorrectCredentialsException e){

            return R.error("密码错误");
        }
           return R.ok();
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }
    @RequestMapping("/updateImage")
    public String updateImage(){
        return "updateImage";
    }
    @RequestMapping("/hello")
    public String hello(Model model) {


        String user = "99999";
        model.addAttribute("name",user);
        return "hello";
    }

    @GetMapping("/dashboard")
    public String pd(){
        return "dashboard";
    }
    @RequestMapping("/updateMyself")
    public String uM(){
        return "pages/user/updateMyself";
    }
    @RequestMapping("/changePassword")
    public String up(){
        return "pages/user/changePassword";
    }
    @RequestMapping("/personal")
    public String personal(){
        return "personal";
    }
    @RequestMapping("/userList")
    public String userList(){
        return "pages/user/userList";
    }
    @RequestMapping("/user")
    public String user(){
        return "user";
    }
    @RequestMapping("listData")
    @ResponseBody
    public PageResult<User> listData(Integer page,Integer limit,User user){
        ValueOperations<String,List<User>> operations = redisTemplate.opsForValue();
        PageResult<User> result = new PageResult<>();
        List<User> list;
        list = operations.get("listDataKey");
        if(list != null){
            result.setData(list);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            int i = userDao.selectCount(queryWrapper);
            result.setCount(i);
        }else{
           Integer id = user.getId();
           if(id == null){
              list = userService.selectAll((page-1)*limit,limit);
           }else{
              list = userDao.getById(id);
           }
            result.setData(list);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            int i = userDao.selectCount(queryWrapper);
            String key = "listDataKey";
            operations.set(key, list,100, TimeUnit.SECONDS);
            result.setCount(i);
        }

        return result;
    }
    @SysLog("退出")
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }
    @RequestMapping("/add")
    public String add(){
        return "pages/user/addUser";
    }
    @RequestMapping("/edit")
    public String edit(){
        return "pages/user/updateUser";
    }


}