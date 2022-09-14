package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddTagDto;
import com.gyfz.domain.dto.EditTagDto;
import com.gyfz.domain.dto.TagListDto;
import com.gyfz.domain.entity.Tag;
import com.gyfz.domain.entity.User;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.domain.vo.TagVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.mapper.TagMapper;
import com.gyfz.service.TagService;
import com.gyfz.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-09-03 18:38:42
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        //封装Vo
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult Add(AddTagDto tagDto) {
        if (!StringUtils.hasText(tagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NOTNULL);
        }
        if (tagExist(tagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_EXIST);
        }
        Tag tag = BeanCopyUtils.CopyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Long id) {
        Tag tag = getById(id);
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult editTag(EditTagDto editTagDto) {
        if (!StringUtils.hasText(editTagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NOTNULL);
        }
        Tag tag = BeanCopyUtils.CopyBean(editTagDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.CopyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }

    @Override
    public ResponseResult removeTagByIds(List<String> id) {
        removeByIds(id);
        return ResponseResult.okResult();
    }

    private boolean tagExist(String tagName) {
        LambdaQueryWrapper<Tag> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(Tag::getName,tagName);
        return count(QueryWrapper) > 0;
    }
}
