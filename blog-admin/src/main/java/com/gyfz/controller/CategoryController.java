package com.gyfz.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.dto.AddCategoryDto;
import com.gyfz.domain.dto.EditCategoryDto;
import com.gyfz.domain.vo.CategoryVo;
import com.gyfz.domain.vo.ExcelCategoryVo;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.service.CategoryService;
import com.gyfz.utils.BeanCopyUtils;
import com.gyfz.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
       return categoryService.listAllCategory();
    }
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String name,String status){
        return categoryService.list(pageNum,pageSize,name,status);
    }
    @PostMapping()
    public ResponseResult Add(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.add(addCategoryDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult Delete(@PathVariable List<String> ids){
       return categoryService.delete(ids);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping()
    public ResponseResult update(@RequestBody EditCategoryDto editCategoryDto){
        return categoryService.update(editCategoryDto);
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public  void export(HttpServletResponse response){
        //设置下载文件请求头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<CategoryVo> categoryVos = (List<CategoryVo>) categoryService.listAllCategory().getData();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.CopyBeanList(categoryVos, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
            //把数据写入到Excel中
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
