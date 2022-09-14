package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddTagDto;
import com.gyfz.domain.dto.EditTagDto;
import com.gyfz.domain.dto.TagListDto;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){

        return tagService.pageTagList( pageNum,  pageSize,  tagListDto);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto tagDto){
        return tagService.Add(tagDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult removeTagById(@PathVariable List<String> ids){
        return tagService.removeTagByIds(ids);
    }
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }
    @PutMapping()
    public ResponseResult editTag(@RequestBody EditTagDto editTagDto){
        return tagService.editTag(editTagDto);
    }
}
