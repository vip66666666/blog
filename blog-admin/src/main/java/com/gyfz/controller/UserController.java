package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.*;
import com.gyfz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, UserListDto userListDto){
        return  userService.list(pageNum,pageSize,userListDto);
    }
    @PostMapping
    public ResponseResult add(@RequestBody AddUserDto addUserDto){
        return  userService.add(addUserDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable Long id){
        return  userService.getUserById(id);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult delete(@PathVariable List<Long> ids){
        return  userService.delete(ids);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody EditUserStatusDto editUserStatusDto){
        return userService.changeStatus(editUserStatusDto);
    }
    @PutMapping
    public ResponseResult update(@RequestBody EditUserDto editUserDto){
        return  userService.update(editUserDto);
    }
}
