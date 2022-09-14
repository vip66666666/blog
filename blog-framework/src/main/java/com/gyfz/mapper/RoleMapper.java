package com.gyfz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gyfz.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-03 22:35:07
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}
