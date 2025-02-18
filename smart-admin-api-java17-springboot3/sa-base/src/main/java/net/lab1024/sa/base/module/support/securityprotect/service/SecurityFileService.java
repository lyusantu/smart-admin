package net.lab1024.sa.base.module.support.securityprotect.service;

import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 三级等保 文件上传 相关
 */

@Service
public class SecurityFileService {

    @Resource
    private Level3ProtectConfigService level3ProtectConfigService;


    /**
     * 检测文件安全类型
     */
    public ResponseDTO<String> checkFile(MultipartFile file) {

        // 检验文件大小
        if (level3ProtectConfigService.getMaxUploadFileSizeMb() > 0) {
            long maxSize = level3ProtectConfigService.getMaxUploadFileSizeMb() * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return ResponseDTO.userErrorParam("上传文件最大为:" + level3ProtectConfigService.getMaxUploadFileSizeMb() + " mb");
            }
        }

        // 文件类型安全检测
        if (!level3ProtectConfigService.isFileDetectFlag()) {
            return ResponseDTO.ok();
        }

        // 检测文件类型
        // .....

        return ResponseDTO.ok();
    }

}
;
