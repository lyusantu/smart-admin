package com.lyusantu.easy.admin.module.business.category.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.constant.AdminSwaggerTagConst;
import com.lyusantu.easy.admin.module.business.category.domain.form.CategoryAddForm;
import com.lyusantu.easy.admin.module.business.category.domain.form.CategoryTreeQueryForm;
import com.lyusantu.easy.admin.module.business.category.domain.form.CategoryUpdateForm;
import com.lyusantu.easy.admin.module.business.category.domain.vo.CategoryTreeVO;
import com.lyusantu.easy.admin.module.business.category.domain.vo.CategoryVO;
import com.lyusantu.easy.admin.module.business.category.service.CategoryService;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类目
 */
@RequiredArgsConstructor
@RestController
@Tag(name = AdminSwaggerTagConst.Business.MANAGER_CATEGORY)
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "添加类目")
    @PostMapping("/category/add")
    @SaCheckPermission("category:add")
    public ResponseDTO<String> add(@RequestBody @Valid CategoryAddForm addForm) {
        return categoryService.add(addForm);
    }

    @Operation(summary = "更新类目")
    @PostMapping("/category/update")
    @SaCheckPermission("category:update")
    public ResponseDTO<String> update(@RequestBody @Valid CategoryUpdateForm updateForm) {
        return categoryService.update(updateForm);
    }

    @Operation(summary = "查询类目详情")
    @GetMapping("/category/{categoryId}")
    public ResponseDTO<CategoryVO> queryDetail(@PathVariable Long categoryId) {
        return categoryService.queryDetail(categoryId);
    }

    @Operation(summary = "查询类目层级树")
    @PostMapping("/category/tree")
    @SaCheckPermission("category:tree")
    public ResponseDTO<List<CategoryTreeVO>> queryTree(@RequestBody @Valid CategoryTreeQueryForm queryForm) {
        return categoryService.queryTree(queryForm);
    }

    @Operation(summary = "删除类目")
    @GetMapping("/category/delete/{categoryId}")
    @SaCheckPermission("category:delete")
    public ResponseDTO<String> delete(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
