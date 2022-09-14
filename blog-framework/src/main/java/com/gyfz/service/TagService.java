package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddTagDto;
import com.gyfz.domain.dto.EditTagDto;
import com.gyfz.domain.dto.TagListDto;
import com.gyfz.domain.entity.Tag;
import com.gyfz.domain.vo.PageVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-09-03 18:38:41
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult Add(AddTagDto tagDto);

    ResponseResult removeTagByIds(List<String> id);

    ResponseResult getTagById(Long id);

    ResponseResult editTag(EditTagDto editTagDto);

    ResponseResult listAllTag();
}
