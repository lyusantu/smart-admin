package com.lyusantu.easy.base.module.support.file.service;

import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.module.support.file.domain.vo.FileDownloadVO;
import com.lyusantu.easy.base.module.support.file.domain.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 接口
 */
public interface IFileStorageService {

    /**
     * 文件上传
     *
     * @param file
     * @param path
     * @return
     */
    ResponseDTO<FileUploadVO> upload(MultipartFile file, String path);

    /**
     * 获取文件url
     *
     * @param fileKey
     * @return
     */
    ResponseDTO<String> getFileUrl(String fileKey);


    /**
     * 流式下载（名称为原文件）
     *
     * @param key
     * @return
     */
    ResponseDTO<FileDownloadVO> download(String key);

    /**
     * 单个删除文件
     * 根据文件key删除
     *
     * @param fileKey
     * @return
     */
    ResponseDTO<String> delete(String fileKey);


    /**
     * 获取文件类型
     *
     * @param fileExt
     * @return
     */
    default String getContentType(String fileExt) {
        // 文件的后缀名
        if ("bmp".equalsIgnoreCase(fileExt)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExt)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt)) {
            return "image/jpeg";
        }
        if ("png".equalsIgnoreCase(fileExt)) {
            return "image/png";
        }
        if ("html".equalsIgnoreCase(fileExt)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExt)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExt)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
            return "application/msword";
        }
        if ("pdf".equalsIgnoreCase(fileExt)) {
            return "application/pdf";
        }
        if ("xml".equalsIgnoreCase(fileExt)) {
            return "text/xml";
        }
        return "";
    }

}
