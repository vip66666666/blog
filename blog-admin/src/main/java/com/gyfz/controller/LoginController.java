package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.entity.LoginUser;
import com.gyfz.domain.entity.Menu;
import com.gyfz.domain.entity.User;
import com.gyfz.domain.vo.AdminUserInfoVo;
import com.gyfz.domain.vo.RouterVo;
import com.gyfz.domain.vo.UserInfoVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.service.LoginService;
import com.gyfz.service.MenuService;
import com.gyfz.service.RoleService;
import com.gyfz.utils.BeanCopyUtils;
import com.gyfz.utils.SecurityUtils;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult Login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){

        return  loginService.logout();
    }
    @GetMapping("getInfo")
    public  ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList  = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.CopyBean(user,UserInfoVo.class);
        //封装数量返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("getRouters")
    public ResponseResult<RouterVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是true的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //数据返回
        return  ResponseResult.okResult(new RouterVo(menus));
    }

}
