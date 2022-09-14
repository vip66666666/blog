package com.gyfz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyfz.constants.SystemConstants;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddLinkDto;
import com.gyfz.domain.dto.EditLinkDto;
import com.gyfz.domain.entity.Link;
import com.gyfz.domain.vo.LinkVo;
import com.gyfz.domain.vo.PageVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.mapper.LinkMapper;
import com.gyfz.service.LinkService;
import com.gyfz.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-08-23 23:51:02
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> linkWrapper = new LambdaQueryWrapper<Link>().eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(linkWrapper);

        List<LinkVo> linkVos = BeanCopyUtils.CopyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        linkLambdaQueryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        Page<Link> linkPage = new Page<>(pageNum, pageSize);
        page(linkPage,linkLambdaQueryWrapper);
        PageVo pageVo = new PageVo(linkPage.getRecords(), linkPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(AddLinkDto addLinkDto) {
        if (!StringUtils.hasText(addLinkDto.getName())){
            throw new SystemException(AppHttpCodeEnum.LINK_NAME_NOTNULL);
        }
        if (linkExist(addLinkDto.getName())){
            throw new SystemException(AppHttpCodeEnum.LINK_NAME_EXIST);
        }
        Link link = BeanCopyUtils.CopyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delete(List<String> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        return ResponseResult.okResult(link);
    }

    @Override
    public ResponseResult update(EditLinkDto editLinkDto) {
        if (!StringUtils.hasText(editLinkDto.getName())){
            throw new SystemException(AppHttpCodeEnum.LINK_NAME_NOTNULL);
        }
        Link link = BeanCopyUtils.CopyBean(editLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(Long id, String status) {
        LambdaUpdateWrapper<Link> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Link::getId,id);
        lambdaUpdateWrapper.set(Link::getStatus,status);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    private boolean linkExist(String name){
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getName,name);
        return count(linkLambdaQueryWrapper) > 0;
    }
}
