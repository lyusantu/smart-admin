package com.lyusantu.easy.base.module.support.codegenerator.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.codegenerator.constant.CodeGeneratorConstant;
import com.lyusantu.easy.base.module.support.codegenerator.domain.entity.CodeGeneratorConfigEntity;
import com.lyusantu.easy.base.module.support.codegenerator.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.common.util.StringUtil;
import com.lyusantu.easy.base.module.support.codegenerator.mapper.CodeGeneratorConfigMapper;
import com.lyusantu.easy.base.module.support.codegenerator.mapper.CodeGeneratorMapper;
import com.lyusantu.easy.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import com.lyusantu.easy.base.module.support.codegenerator.domain.form.CodeGeneratorPreviewForm;
import com.lyusantu.easy.base.module.support.codegenerator.domain.form.TableQueryForm;
import com.lyusantu.easy.base.module.support.codegenerator.domain.model.*;
import com.lyusantu.easy.base.module.support.codegenerator.domain.vo.TableColumnVO;
import com.lyusantu.easy.base.module.support.codegenerator.domain.vo.TableConfigVO;
import com.lyusantu.easy.base.module.support.codegenerator.domain.vo.TableVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

/**
 * 代码生成器 Service
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CodeGeneratorService {

    private final CodeGeneratorMapper codeGeneratorMapper;

    private final CodeGeneratorConfigMapper codeGeneratorConfigMapper;

    private final CodeGeneratorTemplateService codeGeneratorTemplateService;


    /**
     * 列信息
     *
     * @param tableName 表名
     * @return 列信息
     */
    public List<TableColumnVO> getTableColumns(String tableName) {
        return codeGeneratorMapper.selectTableColumn(tableName);
    }


    /**
     * 查询数据库表数据
     */
    public PageResult<TableVO> queryTableList(TableQueryForm tableQueryForm) {
        Page<?> page = PageUtil.convert2PageQuery(tableQueryForm);
        return PageUtil.convert2PageResult(page, codeGeneratorMapper.queryTableList(page, tableQueryForm));
    }

    /**
     * 获取 表的 配置信息
     *
     * @param table
     * @return
     */
    public TableConfigVO getTableConfig(String table) {

        TableConfigVO config = new TableConfigVO();

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigMapper.selectById(table);
        if (codeGeneratorConfigEntity == null) {
            return config;
        }

        if (StringUtil.isNotEmpty(codeGeneratorConfigEntity.getBasic())) {
            CodeBasic basic = JSON.parseObject(codeGeneratorConfigEntity.getBasic(), CodeBasic.class);
            config.setBasic(basic);
        }

        if (StringUtil.isNotEmpty(codeGeneratorConfigEntity.getFields())) {
            List<CodeField> fields = JSONArray.parseArray(codeGeneratorConfigEntity.getFields(), CodeField.class);
            config.setFields(fields);
        }

        if (StringUtil.isNotEmpty(codeGeneratorConfigEntity.getInsertAndUpdate())) {
            CodeInsertAndUpdate insertAndUpdate = JSON.parseObject(codeGeneratorConfigEntity.getInsertAndUpdate(), CodeInsertAndUpdate.class);
            config.setInsertAndUpdate(insertAndUpdate);
        }

        if (StringUtil.isNotEmpty(codeGeneratorConfigEntity.getDeleteInfo())) {
            CodeDelete deleteInfo = JSON.parseObject(codeGeneratorConfigEntity.getDeleteInfo(), CodeDelete.class);
            config.setDeleteInfo(deleteInfo);
        }

        if (StringUtil.isNotEmpty(codeGeneratorConfigEntity.getQueryFields())) {
            List<CodeQueryField> queryFields = JSONArray.parseArray(codeGeneratorConfigEntity.getQueryFields(), CodeQueryField.class);
            config.setQueryFields(queryFields);
        }

        if (StringUtil.isNotEmpty(codeGeneratorConfigEntity.getTableFields())) {
            List<CodeTableField> tableFields = JSONArray.parseArray(codeGeneratorConfigEntity.getTableFields(), CodeTableField.class);
            config.setTableFields(tableFields);
        }

        return config;
    }

    /**
     * 更新配置
     *
     * @param form
     * @return
     */
    public synchronized ResponseDTO<String> updateConfig(CodeGeneratorConfigForm form) {
        long existCount = codeGeneratorMapper.countByTableName(form.getTableName());
        if (existCount == 0) {
            return ResponseDTO.userErrorParam("表不存在，请联系后端查看下数据库");
        }

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigMapper.selectById(form.getTableName());
        boolean updateFlag = true;
        if (codeGeneratorConfigEntity == null) {
            codeGeneratorConfigEntity = new CodeGeneratorConfigEntity();
            updateFlag = false;
        }

        // 校验假删，必须有 deleted_flag 字段
        List<TableColumnVO> tableColumns = getTableColumns(form.getTableName());
        if (null != form.getDeleteInfo() && form.getDeleteInfo().getIsSupportDelete() && !form.getDeleteInfo().getIsPhysicallyDeleted()) {
            Optional<TableColumnVO> any = tableColumns.stream().filter(e -> e.getColumnName().equals(CodeGeneratorConstant.DELETED_FLAG)).findAny();
            if (!any.isPresent()) {
                return ResponseDTO.userErrorParam("表结构中没有假删字段：" + CodeGeneratorConstant.DELETED_FLAG + ",请仔细排查");
            }
        }

        // 校验表必须有主键
        if (!tableColumns.stream().filter(e -> "PRI".equalsIgnoreCase(e.getColumnKey())).findAny().isPresent()) {
            return ResponseDTO.userErrorParam("表必须有主键，请联系后端查看下数据库表结构");
        }

        codeGeneratorConfigEntity.setTableName(form.getTableName());
        codeGeneratorConfigEntity.setBasic(JSON.toJSONString(form.getBasic()));
        codeGeneratorConfigEntity.setFields(JSONArray.toJSONString(form.getFields()));
        codeGeneratorConfigEntity.setInsertAndUpdate(JSON.toJSONString(form.getInsertAndUpdate()));
        codeGeneratorConfigEntity.setDeleteInfo(JSON.toJSONString(form.getDeleteInfo()));
        codeGeneratorConfigEntity.setQueryFields(JSONArray.toJSONString(form.getQueryFields()));
        codeGeneratorConfigEntity.setTableFields(JSONArray.toJSONString(form.getTableFields()));

        if (updateFlag) {
            codeGeneratorConfigMapper.updateById(codeGeneratorConfigEntity);
        } else {
            codeGeneratorConfigMapper.insert(codeGeneratorConfigEntity);
        }
        return ResponseDTO.ok();
    }

    /**
     * 预览
     *
     * @param form
     * @return
     */
    public ResponseDTO<String> preview(CodeGeneratorPreviewForm form) {
        long existCount = codeGeneratorMapper.countByTableName(form.getTableName());
        if (existCount == 0) {
            return ResponseDTO.userErrorParam("表不存在，请联系后端查看下数据库");
        }

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigMapper.selectById(form.getTableName());
        if (codeGeneratorConfigEntity == null) {
            return ResponseDTO.userErrorParam("配置信息不存在，请先进行配置");
        }

        List<TableColumnVO> columns = getTableColumns(form.getTableName());
        if (CollectionUtils.isEmpty(columns)) {
            return ResponseDTO.userErrorParam("表没有列信息无法生成");
        }

        String result = codeGeneratorTemplateService.generate(form.getTableName(), form.getTemplateFile(), codeGeneratorConfigEntity);
        return ResponseDTO.ok(result);

    }

    /**
     * 下载代码
     *
     * @param tableName
     * @return
     */
    public ResponseDTO<byte[]> download(String tableName) {
        if (StringUtil.isBlank(tableName)) {
            return ResponseDTO.userErrorParam("表名不能为空");
        }

        long existCount = codeGeneratorMapper.countByTableName(tableName);
        if (existCount == 0) {
            return ResponseDTO.userErrorParam("表不存在，请联系后端查看下数据库");
        }

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigMapper.selectById(tableName);
        if (codeGeneratorConfigEntity == null) {
            return ResponseDTO.userErrorParam("配置信息不存在，请先进行配置");
        }

        List<TableColumnVO> columns = getTableColumns(tableName);
        if (CollectionUtils.isEmpty(columns)) {
            return ResponseDTO.userErrorParam("表没有列信息无法生成");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        codeGeneratorTemplateService.zipGeneratedFiles(out, tableName, codeGeneratorConfigEntity);
        return ResponseDTO.ok(out.toByteArray());
    }
}
