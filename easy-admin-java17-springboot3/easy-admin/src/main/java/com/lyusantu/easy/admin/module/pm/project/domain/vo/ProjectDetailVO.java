package com.lyusantu.easy.admin.module.pm.project.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 项目详情VO
 **/
@Data
public class ProjectDetailVO extends ProjectVO {

    private List<ProjectNodeVO> listProjectNode;

}
