package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddUserDto;
import com.gyfz.domain.dto.EditUserDto;
import com.gyfz.domain.dto.EditUserStatusDto;
import com.gyfz.domain.dto.UserListDto;
import com.gyfz.domain.entity.Role;
import com.gyfz.domain.entity.RoleMenu;
import com.gyfz.domain.entity.User;
import com.gyfz.domain.entity.UserRole;
import com.gyfz.domain.vo.AdminUserVo;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.domain.vo.UserInfoVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.mapper.UserMapper;
import com.gyfz.service.RoleService;
import com.gyfz.service.UserRoleService;
import com.gyfz.service.UserService;
import com.gyfz.utils.BeanCopyUtils;
import com.gyfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-08-30 00:15:26
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装vo
        UserInfoVo vo = BeanCopyUtils.CopyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        Long userId = SecurityUtils.getUserId();
        LambdaUpdateWrapper<User> userUpdateWrapper = new LambdaUpdateWrapper<>();
        userUpdateWrapper.eq(User::getId,userId);
        userUpdateWrapper.set(User::getAvatar,user.getAvatar());
        userUpdateWrapper.set(User::getEmail,user.getEmail());
        userUpdateWrapper.set(User::getNickName,user.getNickName());
        userUpdateWrapper.set(User::getSex,user.getSex());
        this.update(userUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new  SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new  SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new  SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new  SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在判断
        if (userNameExist(user.getUserName())){
            throw new  SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //密码进行加密
       user.setPassword(passwordEncoder.encode(user.getPassword()));
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(StringUtils.hasText(userListDto.getUserName()),User::getUserName,userListDto.getUserName());
        userLambdaQueryWrapper.eq(StringUtils.hasText(userListDto.getPhonenumber()),User::getPhonenumber,userListDto.getPhonenumber());
        userLambdaQueryWrapper.eq(StringUtils.hasText(userListDto.getStatus()),User::getStatus,userListDto.getStatus());
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,userLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult add(AddUserDto addUserDto) {
        if (!StringUtils.hasText(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //对数据进行是否存在判断
        if (userNameExist(addUserDto.getUserName())){
            throw new  SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        User user = BeanCopyUtils.CopyBean(addUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        List<UserRole> userRoles = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {
        User user = getById(id);
        List<Role> roles = (List<Role>) roleService.listAllRole().getData();
        List<Long> roleIds = userRoleService.getRoleIdByUserId(user.getId());
        AdminUserVo vo = new AdminUserVo(roles,roleIds,user);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult delete(List<Long> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(EditUserStatusDto editUserStatusDto) {
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper.eq(User::getId,editUserStatusDto.userId);
        userWrapper.set(User::getStatus,editUserStatusDto.status);
        update(userWrapper);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult update(EditUserDto editUserDto) {
        if (!StringUtils.hasText(editUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(editUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(editUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        User user = BeanCopyUtils.CopyBean(editUserDto, User.class);
        updateById(user);
        userRoleService.deleteByUserId(user.getId());
        List<UserRole> userRoleList = editUserDto.getRoleIds().stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.addUserRole(userRoleList);
        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUserName,userName);
        return count(userQueryWrapper) > 0;
    }
}
