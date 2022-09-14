package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.constants.SystemConstants;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddArticleDto;
import com.gyfz.domain.dto.EditArticleDto;
import com.gyfz.domain.entity.Article;
import com.gyfz.domain.entity.ArticleTag;
import com.gyfz.domain.entity.Category;
import com.gyfz.domain.vo.ArticleDetailVo;
import com.gyfz.domain.vo.ArticleListVo;
import com.gyfz.domain.vo.HotArticleVo;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.mapper.ArticleMapper;
import com.gyfz.service.ArticleService;
import com.gyfz.service.ArticleTagService;
import com.gyfz.service.CategoryService;
import com.gyfz.service.TagService;
import com.gyfz.utils.BeanCopyUtils;
import com.gyfz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
////mybatisPlus service实现类 继承ServiceImpl<mapper接口,实体类> 继承 service接口
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount);
        Page<Article> articlePage = new Page(1,10);
        List<Article> articles = page(articlePage, queryWrapper).getRecords();
        //从redis中获取文章viewCount
        articles = articles.stream()
                .map(article -> {
                    Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, article.getId().toString());
                    article.setViewCount(viewCount.longValue());
                    return article;
                })
                .collect(Collectors.toList());
        //bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article:articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> articleVos = BeanCopyUtils.CopyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId 查询时要和传入相同
        articleWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0,Article::getCategoryId,categoryId);
        //状态正式发布 对isTop排序
        articleWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = page(new Page<>(pageNum, pageSize), articleWrapper);
//        for (Article article :articles) {
//           article.setCategoryName( categoryService.getById(article.getCategoryId()).getName());
//        }
        page.getRecords().stream()
                .map(article->{
                    article.setCategoryName( categoryService.getById(article.getCategoryId()).getName());
                    Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, article.getId().toString());
                    article.setViewCount(viewCount.longValue());
                    return article;
                })
                .collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.CopyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        //从redis中获取文章viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo articleDetailVo = BeanCopyUtils.CopyBean(article, ArticleDetailVo.class);
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if (category !=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {

        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult Add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.CopyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(StringUtils.hasText(title),Article::getTitle,title);
        articleLambdaQueryWrapper.eq(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage,articleLambdaQueryWrapper);
        PageVo pageVo = new PageVo(articlePage.getRecords(), articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult delete(List<String> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> articleTagQueryWrapper = new LambdaQueryWrapper<>();
        articleTagQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> tags = articleTagService.list(articleTagQueryWrapper);
        List<Long> tagIds = tags.stream().map(tag -> tag.getTagId()).collect(Collectors.toList());
        article.setTags(tagIds);
        return ResponseResult.okResult(article);
    }

    @Override
    @Transactional
    public ResponseResult update(EditArticleDto editArticleDto) {
        Article article = BeanCopyUtils.CopyBean(editArticleDto, Article.class);
        updateById(article);
        LambdaQueryWrapper<ArticleTag> articleTagWrapper = new LambdaQueryWrapper<>();
        articleTagWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagWrapper);
        List<ArticleTag> articleTags = editArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
}
