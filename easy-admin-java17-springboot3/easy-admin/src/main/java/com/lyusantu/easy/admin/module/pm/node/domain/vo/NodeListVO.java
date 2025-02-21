package com.lyusantu.easy.admin.module.pm.node.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 节点列表 列表VO
 *
 * @Author lyusantu
 * @Date 2025-02-19 14:33:24
 * @Copyright lyusantu
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NodeListVO {

    @Schema(description = "节点名称")
    private String nodeName;

    @Schema(description = "节点签名")
    private Integer sign;

}
