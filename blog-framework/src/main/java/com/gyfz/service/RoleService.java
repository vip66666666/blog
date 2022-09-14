package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddRoleDto;
import com.gyfz.domain.dto.EditRoleDto;
import com.gyfz.domain.dto.EditRoleStatusDto;
import com.gyfz.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-09-03 22:35:09
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
    ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(EditRoleStatusDto editRoleStatusDto);

    ResponseResult delete(List<String> ids);

    ResponseResult listAllRole();

    ResponseResult add(AddRoleDto addRoleDto);

    ResponseResult getRoleById(Long id);

    ResponseResult update(EditRoleDto editRoleDto);
}
