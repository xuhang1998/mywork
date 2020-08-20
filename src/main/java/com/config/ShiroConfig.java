package com.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.service.impl.RedisSession;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("security") DefaultWebSecurityManager defaultWebSecurityManager){
      ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
      shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
      Map<String,String> filterMap = new LinkedHashMap<>();
      /*
      * anon ：无需认证（登录）可访问
      * authc : 要认证（登录）才可访问
      * */
      filterMap.put("/wechat/index","anon");
      filterMap.put("/code","anon");
      filterMap.put("/admin","authc");
      filterMap.put("/updateMyself","authc");
      filterMap.put("/updateImage","authc");
      filterMap.put("/changePassword","authc");
      filterMap.put("/user","authc");
      filterMap.put("/log/list","authc");
      filterMap.put("/quarz/index","authc");
     /* filterMap.put("/mywork/*","authc");*///将所有页面都加验证
         filterMap.put("/yzlogin","anon");
      shiroFilterFactoryBean.setLoginUrl("/login");//未登录则跳转到这个页面
      shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
      return shiroFilterFactoryBean;
}
    @Bean
    public RedisSession getRedisSessionDao(){
        return new RedisSession();
    }
    @Bean(name="sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //sessionManager.setCacheManager(cacheManager());
        sessionManager.setGlobalSessionTimeout(43200000); //12小时
        sessionManager.setDeleteInvalidSessions(true);
        //关键在这里
        sessionManager.setSessionDAO(getRedisSessionDao());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(getSessionIdCookie());

        return sessionManager;
    }
    /**
     * 记住我
     */
    public CookieRememberMeManager rememberMeManager()
    {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("fCq+/xW488hMTCD+cmJ3aQ=="));
        return cookieRememberMeManager;
    }
    /**
     * cookie 属性设置
     */
    public SimpleCookie rememberMeCookie()
    {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }
   @Bean("security")
    public DefaultWebSecurityManager getSecurityManager(@Qualifier("UserRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
       // 记住我
       defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
       defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
        return defaultWebSecurityManager;
    }
    @Bean(name="sessionIdCookie")
    public SimpleCookie getSessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("jessionId");

        return simpleCookie;
    }
    @Bean("UserRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     *
     * @return
     */
    @Bean
    @DependsOn({ "lifecycleBeanPostProcessor" })
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
