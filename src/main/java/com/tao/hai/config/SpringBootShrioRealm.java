package com.tao.hai.config;

import com.tao.hai.bean.Permission;
import com.tao.hai.bean.Role;
import com.tao.hai.bean.User;
import com.tao.hai.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringBootShrioRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    public String getName() {
        return "SpringBootShrioRealm";
    }
    //权限信息，包括角色以及权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //如果身份认证的时候没有传入User对象，这里只能取到userName
        //也就是SimpleAuthenticationInfo构造的时候第一个参数传递需要User对象
        User user  = (User)principals.getPrimaryPrincipal();
        for(Role role:user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            for(Permission p:role.getPermissionList()){
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }
    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String userName = upToken.getUsername();
        // Null username is invalid
        if (userName == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        String version = userService.getDbVersion();
        System.out.println(version);
        User user = userService.getUser(userName);
        if (user == null) {
            throw new UnknownAccountException("No account found for admin [" + userName + "]");
        }
        user.setPermissionList(userService.findUserRolePermissionByUserName(user.getUserName()));
        user.setRoleList(userService.findUserRoleByUserName(user.getUserName()));
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
                user.getPassword(), //密码
                getName()  //realm name
        );
        if(user.getCredentialsSalt()!=null){
            authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getCredentialsSalt()));
        }
        return authenticationInfo;
    }
}
