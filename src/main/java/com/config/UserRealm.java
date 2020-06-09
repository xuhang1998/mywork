package com.config;
import com.constants.UserConstants;
import com.dao.PermissionDao;
import com.entity.User;
import com.service.UserService;
import com.utils.SpringUtil;
import com.utils.UserUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.entity.Permission;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    //验证
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
        User user = userService.getUser(usernamePasswordToken.getUsername());
        if(user == null){
            return null;//shiro底层会抛出UnknownAccountException
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),user.getUsername());
/*
        return new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(UserConstants.salt),user.getUsername());
*/
        return info;
    }
    @Override
    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        List<Permission> permissionList = SpringUtil.getBean(PermissionDao.class).getPermission(user.getId());
        UserUtil.setPermissionSession(permissionList);
        Set<String> permissions = permissionList.stream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
                .map(Permission::getPermission).collect(Collectors.toSet());
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    /**
     * 重写缓存key，否则集群下session共享时，会重复执行doGetAuthorizationInfo权限配置
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) principals;
        Object object = principalCollection.getPrimaryPrincipal();

        if (object instanceof User) {
            User user = (User) object;

            return "authorization:cache:key:users:" + user.getId();
        }

        return super.getAuthorizationCacheKey(principals);
    }

}
