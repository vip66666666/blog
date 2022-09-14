package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddCategoryDto;
import com.gyfz.domain.dto.EditCategoryDto;
import com.gyfz.domain.entity.Category;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-08-20 17:23:58
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult list(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult add(AddCategoryDto addCategoryDto);

    ResponseResult delete(List<String> ids);

    ResponseResult getCategoryById(Long id);

    ResponseResult update(EditCategoryDto editCategoryDto);
}
