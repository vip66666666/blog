package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddMenuDto;
import com.gyfz.domain.dto.EditMenuDto;
import com.gyfz.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-09-03 22:28:53
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult list(String menuName, String status);

    ResponseResult add(AddMenuDto addMenuDto);

    ResponseResult delete(Long id);

    ResponseResult getMenuById(Long id);

    ResponseResult update(EditMenuDto editMenuDto);

    ResponseResult treeSelect();

    ResponseResult getMenuByRoleId(Long roleId);
}
