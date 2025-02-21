package com.lyusantu.easy.admin.module.pm.node.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 界面模板详情表 实体类
 *
 * @Author lyusantu
 * @Date 2025-02-19 17:09:17
 * @Copyright lyusantu
 */

@Data
@TableName("t_node_detail")
public class NodeDetailEntity {

    /**
     * 节点ID
     */
    @TableId(type = IdType.AUTO)
    private Long nodeDetailId;

    /**
     * 节点模版ID
     */
    private Long nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点下标
     */
    private Integer sign;

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
