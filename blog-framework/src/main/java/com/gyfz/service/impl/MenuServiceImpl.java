package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.constants.SystemConstants;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddMenuDto;
import com.gyfz.domain.dto.EditMenuDto;
import com.gyfz.domain.entity.Menu;
import com.gyfz.domain.vo.RoleMenuTreeSelectVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.mapper.MenuMapper;
import com.gyfz.service.MenuService;
import com.gyfz.service.RoleMenuService;
import com.gyfz.utils.BeanCopyUtils;
import com.gyfz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-09-03 22:28:54
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是系统管理员，返回所有权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
            menuQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            menuQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(menuQueryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;

        }
        //否则返回去所具有权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper baseMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //返回所有符合要求的Menu
            menus= baseMapper.selectAllRouterMenu();
        }else {
            //否则返回当前用户所具有的Menu
            menus = baseMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public ResponseResult list(String menuName, String status) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        menuLambdaQueryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        menuLambdaQueryWrapper.orderByAsc(Menu::getParentId);
        menuLambdaQueryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = list(menuLambdaQueryWrapper);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult add(AddMenuDto addMenuDto) {
        Menu menu = BeanCopyUtils.CopyBean(addMenuDto, Menu.class);
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(Long id) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId,id);
        if (count(menuLambdaQueryWrapper) > 0){
            throw  new SystemException(AppHttpCodeEnum.DELETE_EXIST_SUBMENU);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult update(EditMenuDto editMenuDto) {
        Menu menu = BeanCopyUtils.CopyBean(editMenuDto, Menu.class);
        if (editMenuDto.getId().equals(editMenuDto.getParentId())){
            throw new SystemException(AppHttpCodeEnum.EDIT_NOT_PARENT_MENU);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeSelect() {
        List<Menu> menus = baseMapper.selectAllRouterMenu();
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return ResponseResult.okResult(menuTree);
    }

    @Override
    public ResponseResult getMenuByRoleId(Long roleId) {
        List<Long> menuIds = roleMenuService.getAllMenuIdByRoleId(roleId);
        List<Menu> menus = (List<Menu>) treeSelect().getData();
        RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(menus,menuIds);
        return ResponseResult.okResult(roleMenuTreeSelectVo);
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
       return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
    }
}
