package com.gyfz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.domain.entity.ArticleTag;
import com.gyfz.mapper.ArticleTagMapper;
import com.gyfz.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-09-05 21:12:10
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
