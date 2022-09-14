package com.gyfz.controller;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddCategoryDto;
import com.gyfz.domain.dto.AddLinkDto;
import com.gyfz.domain.dto.EditLinkDto;
import com.gyfz.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String name,String status){
       return linkService.list(pageNum,pageSize,name,status);
    }
    @PostMapping()
    public ResponseResult Add(@RequestBody AddLinkDto addLinkDto){
        return  linkService.add(addLinkDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult delete(@PathVariable List<String> ids){
        return  linkService.delete(ids);
    }
    @GetMapping("/{id}")
    public  ResponseResult getLinkById(@PathVariable Long id){
        return  linkService.getLinkById(id);
    }
    @PutMapping()
    public ResponseResult update(@RequestBody EditLinkDto editLinkDto){
        return  linkService.update(editLinkDto);
    }
    @PutMapping("/changeLinkStatus")
    public  ResponseResult changeLinkStatus(@RequestBody EditLinkDto editLinkDto){
        return linkService.changeLinkStatus(editLinkDto.getId(),editLinkDto.getStatus());
    }
}
