package com.lyusantu.easy.admin.module.pm.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyusantu.easy.admin.module.pm.node.domain.entity.NodeDetailEntity;
import com.lyusantu.easy.admin.module.pm.node.manager.NodeDetailMapper;
import com.lyusantu.easy.admin.module.pm.project.constant.ProjectStageStateEnum;
import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectExpensesEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectNodeEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.entity.ProjectStageEntity;
import com.lyusantu.easy.admin.module.pm.project.domain.form.*;
import com.lyusantu.easy.admin.module.pm.project.domain.form.expenses.ProjectExpensesAddForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.expenses.ProjectExpensesUpdateForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.node.ProjectNodeAddForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.node.ProjectNodeUpdateForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.stage.ProjectStageAddForm;
import com.lyusantu.easy.admin.module.pm.project.domain.form.stage.ProjectStageUpdateForm;
import com.lyusantu.easy.admin.module.pm.project.domain.vo.*;
import com.lyusantu.easy.admin.module.pm.project.mapper.ProjectExpensesMapper;
import com.lyusantu.easy.admin.module.pm.project.mapper.ProjectMapper;
import com.lyusantu.easy.admin.module.pm.project.mapper.ProjectNodeMapper;
import com.lyusantu.easy.admin.module.pm.project.mapper.ProjectStageMapper;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.util.RequestUtil;
import com.lyusantu.easy.base.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目信息表 Service
 *
 * @Author lyusantu
 * @Date 2025-02-20 10:29:51
 * @Copyright lyusantu
 */

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectMapper projectMapper;

    private final NodeDetailMapper nodeDetailMapper;

    private final ProjectNodeMapper projectNodeMapper;

    private final ProjectStageMapper projectStageMapper;

    private final ProjectExpensesMapper projectExpensesMapper;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ProjectVO> queryPage(ProjectQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<ProjectVO> list = projectMapper.queryPage(page, queryForm);
        PageResult<ProjectVO> pageResult = PageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    boolean isNodeTemplateNameAvailable(Long projectId, String projectName) {
        return projectMapper.selectCount(new LambdaQueryWrapper<ProjectEntity>()
                .eq(ProjectEntity::getProjectName, projectName)
                .ne(null != projectId, ProjectEntity::getProjectId, projectId)) > 0;
    }

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(ProjectAddForm addForm) {

        if (isNodeTemplateNameAvailable(null, addForm.getProjectName())) {
            return ResponseDTO.userErrorParam("项目名称已经存在");
        }
        if (addForm.getStartTime().isAfter(addForm.getEndTime())) {
            return ResponseDTO.userErrorParam("项目开始时间必须在项目结束时间之前");
        }

        ProjectEntity projectEntity = BeanUtil.copy(addForm, ProjectEntity.class);
        projectEntity.setState(addForm.getEndTime().isBefore(LocalDate.now()) ? 5 : 0);
        projectEntity.setCreateUserId(RequestUtil.getRequestUserId());
        // 项目节点模板
        if (null != addForm.getNodeTemplateId()) {
            List<NodeDetailEntity> list = nodeDetailMapper.selectList(new LambdaQueryWrapper<NodeDetailEntity>().eq(NodeDetailEntity::getNodeId, addForm.getNodeTemplateId()));
            if (!list.isEmpty()) {
                list.forEach(nd -> projectNodeMapper.insert(new ProjectNodeEntity(nd.getNodeName(), nd.getSign())));
            }
        }
        projectMapper.insert(projectEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(ProjectUpdateForm updateForm) {
        if (isNodeTemplateNameAvailable(updateForm.getProjectId(), updateForm.getProjectName())) {
            return ResponseDTO.userErrorParam("项目名称已经存在");
        }
        if (updateForm.getStartTime().isAfter(updateForm.getEndTime())) {
            return ResponseDTO.userErrorParam("项目开始时间必须在项目结束时间之前");
        }
        ProjectEntity projectEntity = BeanUtil.copy(updateForm, ProjectEntity.class);
        projectMapper.updateById(projectEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        projectMapper.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long projectId) {
        if (null == projectId) {
            return ResponseDTO.ok();
        }

        projectMapper.deleteById(projectId);
        return ResponseDTO.ok();
    }

    public ResponseDTO<ProjectDetailVO> detail(Long projectId) {
        ProjectEntity project = projectMapper.selectById(projectId);
        if (null == project) {
            return ResponseDTO.userErrorParam("项目不存在");
        }
        ProjectDetailVO detail = BeanUtil.copy(project, ProjectDetailVO.class);
        List<ProjectNodeEntity> projectNodeEntityList = projectNodeMapper.selectList(new LambdaQueryWrapper<ProjectNodeEntity>().eq(ProjectNodeEntity::getProjectId, projectId).orderByAsc(ProjectNodeEntity::getProjectNodeSign));
        List<ProjectNodeVO> projectNodeList = BeanUtil.copyList(projectNodeEntityList, ProjectNodeVO.class);
        projectNodeList.forEach(pn -> {
            // 查询项目阶段进行项目是否完成的设置
            pn.setComplete(true);
            List<ProjectStageEntity> list = projectStageMapper.selectList(new LambdaQueryWrapper<ProjectStageEntity>()
                    .eq(ProjectStageEntity::getProjectId, projectId)
                    .eq(ProjectStageEntity::getProjectNodeId, pn.getProjectNodeId())
                    .orderByAsc(ProjectStageEntity::getStageId));
            pn.setListProjectStage(BeanUtil.copyList(list, ProjectStageVO.class));
        });
        detail.setListProjectNode(projectNodeList);
        return ResponseDTO.ok(detail);
    }

    public ResponseDTO<String> addProjectNode(ProjectNodeAddForm form) {
        if (projectNodeMapper.selectCount(new LambdaQueryWrapper<ProjectNodeEntity>()
                .eq(ProjectNodeEntity::getProjectNodeName, form.getNodeName())
                .eq(ProjectNodeEntity::getProjectId, form.getProjectId())) > 0) {
            return ResponseDTO.userErrorParam("节点名称已存在");
        }
        ProjectNodeEntity lastProctNode = projectNodeMapper.selectOne(new LambdaQueryWrapper<ProjectNodeEntity>()
                .eq(ProjectNodeEntity::getProjectId, form.getProjectId())
                .orderByDesc(ProjectNodeEntity::getProjectNodeSign).last("limit 1"));

        ProjectNodeEntity projectNode = new ProjectNodeEntity();
        projectNode.setProjectId(form.getProjectId());
        projectNode.setProjectNodeName(form.getNodeName());
        projectNode.setProjectNodeSign(lastProctNode == null ? 1 : lastProctNode.getProjectNodeSign() + 1);
        projectNodeMapper.insert(projectNode);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateProjectNode(ProjectNodeUpdateForm form) {
        if (projectNodeMapper.selectCount(new LambdaQueryWrapper<ProjectNodeEntity>()
                .eq(ProjectNodeEntity::getProjectNodeName, form.getNodeName())
                .eq(ProjectNodeEntity::getProjectId, form.getProjectId())
                .ne(ProjectNodeEntity::getProjectNodeId, form.getProjectNodeId())) > 0) {
            return ResponseDTO.userErrorParam("节点名称已存在");
        }

        ProjectNodeEntity projectNode = projectNodeMapper.selectById(form.getProjectNodeId());
        projectNode.setProjectNodeName(form.getNodeName());
        projectNodeMapper.updateById(projectNode);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> addProjectStage(ProjectStageAddForm form) {
        ProjectStageEntity projectStage = BeanUtil.copy(form, ProjectStageEntity.class);
        projectStage.setState(ProjectStageStateEnum.NOT_STARTED.getValue());
        projectStageMapper.insert(projectStage);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateProjectStage(ProjectStageUpdateForm form) {
        ProjectStageEntity projectStage = projectStageMapper.selectById(form.getStageId());
        if (null == projectStage) {
            return ResponseDTO.userErrorParam("阶段不存在或已被删除");
        }
        BeanUtil.copyProperties(form, projectStage);
        projectStageMapper.updateById(projectStage);
        return ResponseDTO.ok();
    }

    public ResponseDTO<List<ProjectNodeListVO>> listProjectNode(Long projectId) {
        List<ProjectNodeEntity> list = projectNodeMapper.selectList(new LambdaQueryWrapper<ProjectNodeEntity>().eq(ProjectNodeEntity::getProjectId, projectId).orderByAsc(ProjectNodeEntity::getProjectNodeSign));
        return ResponseDTO.ok(BeanUtil.copyList(list, ProjectNodeListVO.class));
    }

    public ResponseDTO<ProjectStageVO> getStage(Long stageId) {
        ProjectStageEntity projectStage = projectStageMapper.selectById(stageId);
        ProjectStageVO vo = BeanUtil.copy(projectStage, ProjectStageVO.class);
        return ResponseDTO.ok(vo);
    }

    public ResponseDTO<List<ProjectExpensesVO>> listProjectExpenses(Long projectId) {
        List<ProjectExpensesEntity> listProjectExpenses = projectExpensesMapper.selectList(new LambdaQueryWrapper<ProjectExpensesEntity>().eq(ProjectExpensesEntity::getProjectId, projectId).orderByAsc(ProjectExpensesEntity::getExpensesId));
        if (listProjectExpenses.isEmpty()) {
            return ResponseDTO.ok();
        }
        List<ProjectExpensesVO> list = BeanUtil.copyList(listProjectExpenses, ProjectExpensesVO.class);
        AtomicInteger serialNumber = new AtomicInteger(1);
        list.forEach(projectExpensesVO -> {
            projectExpensesVO.setSerialNumber(serialNumber.getAndIncrement());
            projectExpensesVO.setInvoicesCount(0);
            if (StringUtil.isNotBlank(projectExpensesVO.getInvoices())) {
                JSONArray array = JSONArray.parse(projectExpensesVO.getInvoices());
                projectExpensesVO.setInvoicesCount(array.size());
            }
        });
        return ResponseDTO.ok(list);
    }

    public ResponseDTO<String> addProjectExpenses(ProjectExpensesAddForm form) {
        ProjectExpensesEntity projectExpenses = BeanUtil.copy(form, ProjectExpensesEntity.class);
        projectExpenses.setInvoices(form.getInvoices() == null ? null : form.getInvoices().toJSONString());
        projectExpensesMapper.insert(projectExpenses);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateProjectExpenses(ProjectExpensesUpdateForm form) {
        ProjectExpensesEntity projectExpenses = projectExpensesMapper.selectById(form.getExpensesId());
        if (null == projectExpenses) {
            return ResponseDTO.userErrorParam("支出信息不存在或已被删除");
        }
        BeanUtil.copyProperties(form, projectExpenses);
        projectExpenses.setInvoices(form.getInvoices() == null ? null : form.getInvoices().toJSONString());
        projectExpensesMapper.updateById(projectExpenses);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> deleteProjectExpenses(Long expensesId) {
        projectExpensesMapper.deleteById(expensesId);
        return ResponseDTO.ok();
    }

    public ResponseDTO<List<MyTaskVO>> myTask() {
        return ResponseDTO.ok(projectMapper.myTask(RequestUtil.getRequestUserId()));
    }

}
