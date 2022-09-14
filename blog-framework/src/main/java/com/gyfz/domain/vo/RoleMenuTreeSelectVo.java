package com.gyfz.domain.vo;

import com.gyfz.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleMenuTreeSelectVo {
    private List<Menu> menus;
    private List<Long> checkedKeys;
}
