package com.lyusantu.easy.admin.module.pm.project.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 项目节点表 实体类
 *
 * @Author lyusantu
 * @Date 2025-02-20 14:58:16
 * @Copyright lyusantu
 */
@NoArgsConstructor
@Data
@TableName("t_project_node")
public class ProjectNodeEntity {

    /**
     * 项目节点ID
     */
    @TableId(type = IdType.AUTO)
    private Long projectNodeId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 项目节点名称
     */
    private String projectNodeName;

    /**
     * 项目节点顺序
     */
    private Integer projectNodeSign;

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

    public ProjectNodeEntity(String projectNodeName, Integer projectNodeSign) {
        this.projectNodeName = projectNodeName;
        this.projectNodeSign = projectNodeSign;
    }

}
