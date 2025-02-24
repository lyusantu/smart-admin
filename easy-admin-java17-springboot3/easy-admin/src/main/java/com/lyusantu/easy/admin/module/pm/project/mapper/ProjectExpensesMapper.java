package com.lyusantu.easy.admin.module.pm.project.mapper;

import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectExpensesEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.form.expenses.ProjectExpensesQueryForm;
import com.lyusantu.easy.admin.module.pm.project.domain.vo.ProjectExpensesVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 项目支付明细表 Mapper
 *
 * @Author lyusantu
 * @Date 2025-02-24 10:14:42
 * @Copyright lyusantu
 */

@Mapper
@Component
public interface ProjectExpensesMapper extends BaseMapper<ProjectExpensesEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ProjectExpensesVO> queryPage(Page page, @Param("queryForm") ProjectExpensesQueryForm queryForm);

}
