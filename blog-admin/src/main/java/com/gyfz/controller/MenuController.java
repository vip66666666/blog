package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddMenuDto;
import com.gyfz.domain.dto.EditMenuDto;
import com.gyfz.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult list(String menuName, String status){
        return menuService.list(menuName,status);
    }
    @PostMapping
    public ResponseResult add(@RequestBody AddMenuDto addMenuDto){
        return menuService.add(addMenuDto);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult delete(@PathVariable Long id){
        return  menuService.delete(id);
    }
    @GetMapping("{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }
    @PutMapping
    public ResponseResult update(@RequestBody EditMenuDto editMenuDto){
        return menuService.update(editMenuDto);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        return  menuService.treeSelect();
    }
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public ResponseResult getMenuByRoleId(@PathVariable Long roleId){
        return menuService.getMenuByRoleId(roleId);
    }
}
