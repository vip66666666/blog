package com.gyfz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditCategoryDto {
    private Long id;
    //分类名
    private String name;
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;
}
