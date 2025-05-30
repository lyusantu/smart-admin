package com.lyusantu.easy.base.module.support.file.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;
import com.lyusantu.easy.base.module.support.file.constant.FileFolderTypeEnum;

import java.time.LocalDateTime;

/**
 * 文件信息
 */
@Data
public class FileVO {

    @Schema(description = "主键")
    private Long fileId;

    @Schema(description = "存储文件夹类型")
    @SchemaEnum(FileFolderTypeEnum.class)
    private Integer folderType;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件大小")
    private Integer fileSize;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件路径")
    private String fileKey;

    @Schema(description = "上传人")
    private Long creatorId;

    @Schema(description = "上传人")
    private String creatorName;

    @SchemaEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer creatorUserType;

    @Schema(description = "文件展示url")
    private String fileUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
