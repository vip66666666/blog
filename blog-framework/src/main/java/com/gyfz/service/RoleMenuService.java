package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2022-09-09 00:25:28
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteByRoleId(Long id);

    void addRoleMenuList(List<RoleMenu> roleMenuList);

    List<Long> getAllMenuIdByRoleId(Long roleId);
}
