package com.lyusantu.easy.admin.module.system.department.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门实体类
 */
@Data
@TableName(value = "t_department")
public class DepartmentEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long departmentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 负责人员工 id
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long managerId;

    /**
     * 部门父级id
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;



}
