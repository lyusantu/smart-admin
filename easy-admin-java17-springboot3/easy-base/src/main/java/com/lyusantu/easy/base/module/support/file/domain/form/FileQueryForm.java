package com.lyusantu.easy.base.module.support.file.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.lyusantu.easy.base.common.domain.page.PageParam;
import com.lyusantu.easy.base.common.swagger.SchemaEnum;
import com.lyusantu.easy.base.common.validator.enumeration.CheckEnum;
import com.lyusantu.easy.base.module.support.file.constant.FileFolderTypeEnum;

import java.time.LocalDate;

/**
 * 文件信息查询
 */
@Data
public class FileQueryForm extends PageParam {

    @SchemaEnum(value = FileFolderTypeEnum.class, desc = "文件夹类型")
    @CheckEnum(value = FileFolderTypeEnum.class, message = "文件夹类型 错误")
    private Integer folderType;

    @Schema(description = "文件名词")
    private String fileName;

    @Schema(description = "文件Key")
    private String fileKey;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "创建人")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDate createTimeBegin;

    @Schema(description = "创建时间")
    private LocalDate createTimeEnd;

}
