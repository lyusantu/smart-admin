package com.lyusantu.easy.admin.module.pm.node.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeListVO;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeTemplateListVO;
import com.lyusantu.easy.admin.module.pm.node.manager.NodeDetailMapper;
import com.lyusantu.easy.admin.module.pm.node.mapper.NodeMapper;
import com.lyusantu.easy.admin.module.pm.node.domain.entity.NodeDetailEntity;
import com.lyusantu.easy.admin.module.pm.node.domain.entity.NodeEntity;
import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeAddForm;
import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeQueryForm;
import com.lyusantu.easy.admin.module.pm.node.domain.form.NodeUpdateForm;
import com.lyusantu.easy.admin.module.pm.node.domain.vo.NodeVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lyusantu.easy.base.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 节点模板表 Service
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@RequiredArgsConstructor
@Service
public class NodeService {

    private final NodeMapper nodeMapper;

    private final NodeDetailMapper nodeDetailMapper;

    /**
     * 分页查询
     */
    public PageResult<NodeVO> queryPage(NodeQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<NodeVO> list = nodeMapper.queryPage(page, queryForm);
        return PageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(NodeAddForm addForm) {
        // 验证节点模版名称和节点名称是否重复
        if (isNodeTemplateNameAvailable(null, addForm.getNodeName())) {
            return ResponseDTO.userErrorParam("节点模版名称已存在");
        }
        String nodeNameAvailable = isNodeNameAvailable(addForm.getList());
        if (nodeNameAvailable != null) {
            return ResponseDTO.userErrorParam(nodeNameAvailable);
        }

        NodeEntity nodeEntity = BeanUtil.copy(addForm, NodeEntity.class);
        nodeMapper.insert(nodeEntity);
        addForm.getList().forEach(n -> {
            NodeDetailEntity entity = BeanUtil.copy(n, NodeDetailEntity.class);
            entity.setNodeId(nodeEntity.getNodeId());
            nodeDetailMapper.insert(entity);
        });
        return ResponseDTO.ok();
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(NodeUpdateForm updateForm) {
        // 验证节点模版名称和节点名称是否重复
        if (isNodeTemplateNameAvailable(updateForm.getNodeId(), updateForm.getNodeName())) {
            return ResponseDTO.userErrorParam("节点模版名称已存在");
        }
        String nodeNameAvailable = isNodeNameAvailable(updateForm.getList());
        if (nodeNameAvailable != null) {
            return ResponseDTO.userErrorParam(nodeNameAvailable);
        }

        NodeEntity nodeEntity = BeanUtil.copy(updateForm, NodeEntity.class);
        nodeMapper.updateById(nodeEntity);

        nodeDetailMapper.delete(new LambdaQueryWrapper<NodeDetailEntity>().eq(NodeDetailEntity::getNodeId, nodeEntity.getNodeId()));
        updateForm.getList().forEach(n -> {
            NodeDetailEntity entity = BeanUtil.copy(n, NodeDetailEntity.class);
            entity.setNodeId(nodeEntity.getNodeId());
            nodeDetailMapper.insert(entity);
        });
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        nodeMapper.batchDeleted(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long nodeId) {
        if (null == nodeId) {
            return ResponseDTO.ok();
        }

        nodeMapper.deleteById(nodeId);
        return ResponseDTO.ok();
    }

    public List<NodeListVO> listNode(Long nodeId) {
        return BeanUtil.copyList(nodeDetailMapper.selectList(new LambdaQueryWrapper<NodeDetailEntity>().eq(NodeDetailEntity::getNodeId, nodeId)), NodeListVO.class);
    }

    boolean isNodeTemplateNameAvailable(Long nodeTemplateId, String nodeTemplateName) {
        return nodeMapper.selectCount(new LambdaQueryWrapper<NodeEntity>()
                .eq(NodeEntity::getNodeName, nodeTemplateName)
                .ne(null != nodeTemplateId, NodeEntity::getNodeId, nodeTemplateId)) > 0;
    }

    String isNodeNameAvailable(List<NodeListVO> list) {
        Set<String> nodeNameSet = new HashSet<>();
        Set<Integer> signSet = new HashSet<>();
        for (NodeListVO n : list) {
            if (StringUtil.isBlank(n.getNodeName())) {
                return "节点名称不能为空";
            }
            if (n.getSign() == null) {
                return "节点签名不能为空";
            }
            if (!nodeNameSet.add(n.getNodeName())) {
                return String.format("节点名称[%s]重复，请修改后重新提交", n.getNodeName());
            }
            if (!signSet.add(n.getSign())) {
                return "节点签名重复，请联系开发者修复此问题";
            }
        }
        return null;
    }

    /**
     * 分页查询
     */
    public List<NodeTemplateListVO> listNodeTemplate() {
        return nodeMapper.listNodeTemplate();
    }

}
