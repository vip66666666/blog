package com.gyfz.utils;

import com.gyfz.domain.entity.Article;
import com.gyfz.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}
    public static <V>V CopyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static <O,V> List<V> CopyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> CopyBean(o, clazz))
                .collect(Collectors.toList());
    };
    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1l);
        article.setTitle("123123");
        article.setViewCount(11l);
        HotArticleVo hotArticleVo = CopyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
