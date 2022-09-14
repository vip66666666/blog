package com.gyfz.service.impl;

import com.gyfz.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限字段
     * @return
     */
    public boolean hasPermission(String permission){
        if (SecurityUtils.isAdmin()){
            return  true;
        }
        //用户当前登录用户所具有的权限列表 是否存在
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return  permissions.contains(permission);
    };
}
