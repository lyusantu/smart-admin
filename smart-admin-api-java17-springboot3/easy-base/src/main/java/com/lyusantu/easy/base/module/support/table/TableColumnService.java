package com.lyusantu.easy.base.module.support.table;

import com.alibaba.fastjson.JSONArray;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.module.support.table.domain.TableColumnEntity;
import com.lyusantu.easy.base.module.support.table.domain.TableColumnUpdateForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * 表格自定义列（前端用户自定义表格列，并保存到数据库里）
 */
@RequiredArgsConstructor
@Service
public class TableColumnService {

    private final TableColumnMapper tableColumnMapper;

    /**
     * 获取 - 表格列
     *
     * @return
     */
    public String getTableColumns(RequestUser requestUser, Integer tableId) {
        TableColumnEntity tableColumnEntity = tableColumnMapper.selectByUserIdAndTableId(requestUser.getUserId(), requestUser.getUserType().getValue(), tableId);
        return tableColumnEntity == null ? null : tableColumnEntity.getColumns();
    }

    /**
     * 更新表格列
     *
     * @return
     */
    public ResponseDTO<String> updateTableColumns(RequestUser requestUser, TableColumnUpdateForm updateForm) {
        if (CollectionUtils.isEmpty(updateForm.getColumnList())) {
            return ResponseDTO.ok();
        }
        Integer tableId = updateForm.getTableId();
        TableColumnEntity tableColumnEntity = tableColumnMapper.selectByUserIdAndTableId(requestUser.getUserId(), requestUser.getUserType().getValue(), tableId);
        if (tableColumnEntity == null) {
            tableColumnEntity = new TableColumnEntity();
            tableColumnEntity.setTableId(tableId);
            tableColumnEntity.setUserId(requestUser.getUserId());
            tableColumnEntity.setUserType(requestUser.getUserType().getValue());

            tableColumnEntity.setColumns(JSONArray.toJSONString(updateForm.getColumnList()));
            tableColumnMapper.insert(tableColumnEntity);
        } else {
            tableColumnEntity.setColumns(JSONArray.toJSONString(updateForm.getColumnList()));
            tableColumnMapper.updateById(tableColumnEntity);
        }
        return ResponseDTO.ok();
    }

    /**
     * 删除表格列
     *
     * @return
     */
    public ResponseDTO<String> deleteTableColumn(RequestUser requestUser, Integer tableId) {
        tableColumnMapper.deleteTableColumn(requestUser.getUserId(), requestUser.getUserType().getValue(), tableId);
        return ResponseDTO.ok();
    }
}
