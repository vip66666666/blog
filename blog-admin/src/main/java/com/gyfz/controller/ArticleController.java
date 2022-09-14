package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddArticleDto;
import com.gyfz.domain.dto.EditArticleDto;
import com.gyfz.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.Add(articleDto);
    }
    @GetMapping("/list")
    private ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.list(pageNum,pageSize,title,summary);
    }
    @DeleteMapping("/{ids}")
    public  ResponseResult delete(@PathVariable List<String> ids){
        return articleService.delete(ids);
    }
    @GetMapping("/{id}")
    public  ResponseResult getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }
    @PutMapping
    public ResponseResult update(@RequestBody EditArticleDto editArticleDto){
        return articleService.update(editArticleDto);
    }
}
