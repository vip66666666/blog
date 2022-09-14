package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.entity.UserRole;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表服务接口
 *
 * @author makejava
 * @since 2022-09-08 23:53:55
 */
public interface UserRoleService extends IService<UserRole> {

    List<Long> getRoleIdByUserId(Long id);

    void deleteByUserId(Long id);

    void addUserRole(List<UserRole> userRoleList);
}
