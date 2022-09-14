package com.gyfz.domain.vo;

import com.gyfz.domain.entity.Role;
import com.gyfz.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserVo {
    private List<Role> roles;
    private List<Long> roleIds;
    private User user;
}
