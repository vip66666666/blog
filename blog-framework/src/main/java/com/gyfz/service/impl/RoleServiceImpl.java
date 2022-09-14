package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddRoleDto;
import com.gyfz.domain.dto.EditRoleDto;
import com.gyfz.domain.dto.EditRoleStatusDto;
import com.gyfz.domain.entity.Link;
import com.gyfz.domain.entity.Role;
import com.gyfz.domain.entity.RoleMenu;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.mapper.RoleMapper;
import com.gyfz.service.RoleMenuService;
import com.gyfz.service.RoleService;
import com.gyfz.utils.BeanCopyUtils;
import com.gyfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-09-03 22:35:09
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (SecurityUtils.isAdmin()){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        roleLambdaQueryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> Page = new Page<>(pageNum, pageSize);
        page(Page,roleLambdaQueryWrapper);
        PageVo pageVo = new PageVo(Page.getRecords(), Page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(EditRoleStatusDto editRoleStatusDto) {
        LambdaUpdateWrapper<Role> roleWrapper = new LambdaUpdateWrapper<>();
        roleWrapper.eq(Role::getId,editRoleStatusDto.roleId);
        roleWrapper.set(Role::getStatus,editRoleStatusDto.status);
        update(roleWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(List<String> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> list = list();
        return ResponseResult.okResult(list);
    }

    @Override
    @Transactional
    public ResponseResult add(AddRoleDto addRoleDto) {
        if (!StringUtils.hasText(addRoleDto.getRoleName())){
            throw  new SystemException(AppHttpCodeEnum.ROLE_NAME);
        }
        if (!StringUtils.hasText(addRoleDto.getRoleKey())){
            throw  new SystemException(AppHttpCodeEnum.ROLE_KEY);
        }
        Role role = BeanCopyUtils.CopyBean(addRoleDto, Role.class);
        save(role);
        List<RoleMenu> roleMenus = addRoleDto.getMenuIds().stream()
                .map(MenuId -> new RoleMenu(role.getId(), MenuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = getById(id);
        return ResponseResult.okResult(role);
    }

    @Override
    @Transactional
    public ResponseResult update(EditRoleDto editRoleDto) {
        if (!StringUtils.hasText(editRoleDto.getRoleName())){
            throw  new SystemException(AppHttpCodeEnum.ROLE_NAME);
        }
        if (!StringUtils.hasText(editRoleDto.getRoleKey())){
            throw  new SystemException(AppHttpCodeEnum.ROLE_KEY);
        }
        Role role = BeanCopyUtils.CopyBean(editRoleDto, Role.class);
        updateById(role);
        roleMenuService.deleteByRoleId(role.getId());
        List<RoleMenu> roleMenuList = editRoleDto.getMenuIds().stream().map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.addRoleMenuList(roleMenuList);
        return ResponseResult.okResult();
    }
}
