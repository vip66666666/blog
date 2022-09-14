package com.gyfz.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.constants.SystemConstants;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddCategoryDto;
import com.gyfz.domain.dto.EditCategoryDto;
import com.gyfz.domain.entity.Article;
import com.gyfz.domain.entity.Category;
import com.gyfz.domain.vo.CategoryVo;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.mapper.CategoryMapper;
import com.gyfz.service.ArticleService;
import com.gyfz.service.CategoryService;
import com.gyfz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-08-20 17:23:58
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        List<Article> articleList = articleService.list(articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL));
        //获取文章的分类id 并且 去重
        Set<Long> articleIds = articleList.stream()
                .map(a -> a.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(articleIds);
        categories = categories.stream().filter(category -> category.getStatus().equals(SystemConstants.STATUS_NORMAL)).collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.CopyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos  = BeanCopyUtils.CopyBeanList(list, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page(pageNum, pageSize);
        page(page,queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(AddCategoryDto addCategoryDto) {
        if (!StringUtils.hasText(addCategoryDto.getName())){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOTNULL);
        }
        if (categoryExist(addCategoryDto.getName())){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_EXIST);
        }
        Category category = BeanCopyUtils.CopyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(List<String> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult update(EditCategoryDto editCategoryDto) {
        if (!StringUtils.hasText(editCategoryDto.getName())){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOTNULL);
        }
        Category category = BeanCopyUtils.CopyBean(editCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    public boolean categoryExist(String name){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Category::getName,name);
        return count(lambdaQueryWrapper) > 0;
    }
}
