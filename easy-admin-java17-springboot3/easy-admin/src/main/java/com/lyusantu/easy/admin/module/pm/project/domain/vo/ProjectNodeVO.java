package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 项目节点 VO
 **/
@Data
public class ProjectNodeVO {

    private Long projectNodeId;

    private String projectNodeName;

    private Integer projectNodeSign;

    private Boolean complete;

    private List<ProjectStageVO> listProjectStage;

}
