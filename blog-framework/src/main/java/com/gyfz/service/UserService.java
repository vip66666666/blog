package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddUserDto;
import com.gyfz.domain.dto.EditUserDto;
import com.gyfz.domain.dto.EditUserStatusDto;
import com.gyfz.domain.dto.UserListDto;
import com.gyfz.domain.entity.User;

import java.util.List;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-08-30 00:15:26
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult list(Integer pageNum, Integer pageSize, UserListDto userListDto);

    ResponseResult add(AddUserDto addUserDto);

    ResponseResult getUserById(Long id);

    ResponseResult delete(List<Long> ids);

    ResponseResult changeStatus(EditUserStatusDto editUserStatusDto);

    ResponseResult update(EditUserDto editUserDto);
}
