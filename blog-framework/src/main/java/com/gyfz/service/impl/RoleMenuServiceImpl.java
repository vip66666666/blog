package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.domain.entity.RoleMenu;
import com.gyfz.mapper.RoleMenuMapper;
import com.gyfz.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("roleMenuService")
public class RoleMenuServiceImpl  extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    @Override
    public void deleteByRoleId(Long id) {
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        remove(roleMenuLambdaQueryWrapper);
    }

    @Override
    public void addRoleMenuList(List<RoleMenu> roleMenuList) {
        saveBatch(roleMenuList);
    }

    @Override
    public List<Long> getAllMenuIdByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,roleId);
        List<RoleMenu> list = list(roleMenuLambdaQueryWrapper);
        List<Long> menuIds = list.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        return menuIds;
    }
}
