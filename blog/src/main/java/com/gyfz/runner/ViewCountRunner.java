package com.gyfz.runner;

import com.gyfz.constants.SystemConstants;
import com.gyfz.domain.entity.Article;
import com.gyfz.mapper.ArticleMapper;
import com.gyfz.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String,Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(a->a.getId().toString(),a->a.getViewCount().intValue()));
        //存到redis
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT,viewCountMap);
    }
}
