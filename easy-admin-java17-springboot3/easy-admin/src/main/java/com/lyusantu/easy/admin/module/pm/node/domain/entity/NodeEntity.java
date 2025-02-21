package com.lyusantu.easy.admin.module.pm.node.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 节点模板表 实体类
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */

@Data
@TableName("t_node")
public class NodeEntity {

    /**
     * 节点ID
     */
    @TableId(type = IdType.AUTO)
    private Long nodeId;

    /**
     * 节点模板名称
     */
    private String nodeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    private Integer deletedFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
