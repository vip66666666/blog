package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.domain.entity.UserRole;
import com.gyfz.mapper.UserRoleMapper;
import com.gyfz.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-09-08 23:53:55
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<Long> getRoleIdByUserId(Long id) {
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> list = list(userRoleLambdaQueryWrapper);
        List<Long> roleIds = list.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
        return roleIds;
    }

    @Override
    public void deleteByUserId(Long id) {
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper =new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,id);
        remove(userRoleLambdaQueryWrapper);
    }

    @Override
    public void addUserRole(List<UserRole> userRoleList) {
        saveBatch(userRoleList);
    }
}
