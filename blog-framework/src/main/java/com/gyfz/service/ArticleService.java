package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddArticleDto;
import com.gyfz.domain.dto.EditArticleDto;
import com.gyfz.domain.entity.Article;

import java.util.List;

////mybatisPlus service接口 IService<类名>
public interface ArticleService  extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult Add(AddArticleDto articleDto);

    ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult delete(List<String> ids);

    ResponseResult getArticleById(Long id);

    ResponseResult update(EditArticleDto editArticleDto);
}
