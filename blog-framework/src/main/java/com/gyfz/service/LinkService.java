package com.gyfz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddLinkDto;
import com.gyfz.domain.dto.EditLinkDto;
import com.gyfz.domain.entity.Link;

import java.util.List;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-08-23 23:51:01
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult list(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult add(AddLinkDto addLinkDto);

    ResponseResult delete(List<String> ids);

    ResponseResult getLinkById(Long id);

    ResponseResult update(EditLinkDto editLinkDto);

    ResponseResult changeLinkStatus(Long id, String status);
}
