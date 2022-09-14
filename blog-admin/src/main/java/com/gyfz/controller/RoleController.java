package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddRoleDto;
import com.gyfz.domain.dto.EditRoleDto;
import com.gyfz.domain.dto.EditRoleStatusDto;
import com.gyfz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String roleName,String status){
       return roleService.list(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody EditRoleStatusDto editRoleStatusDto){
        return roleService.changeStatus(editRoleStatusDto);
    }
    @DeleteMapping("/{ids}")
    public  ResponseResult delete(@PathVariable List<String> ids){
        return roleService.delete(ids);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
    @PostMapping
    public ResponseResult add(@RequestBody AddRoleDto addRoleDto){
        return roleService.add(addRoleDto);
    }
    @GetMapping("/{id}")
    public  ResponseResult getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id);
    }
    @PutMapping
    public ResponseResult update(@RequestBody EditRoleDto editRoleDto){
        return roleService.update(editRoleDto);
    }
}
