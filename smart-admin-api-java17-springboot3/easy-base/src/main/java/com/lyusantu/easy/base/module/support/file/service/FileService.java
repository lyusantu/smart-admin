package com.lyusantu.easy.base.module.support.file.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lyusantu.easy.base.module.support.file.constant.FileFolderTypeEnum;
import com.lyusantu.easy.base.module.support.file.domain.entity.FileEntity;
import com.lyusantu.easy.base.module.support.file.domain.form.FileQueryForm;
import com.lyusantu.easy.base.module.support.file.domain.vo.FileDownloadVO;
import com.lyusantu.easy.base.module.support.file.domain.vo.FileUploadVO;
import com.lyusantu.easy.base.module.support.file.domain.vo.FileVO;
import com.lyusantu.easy.base.module.support.file.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.constant.StringConst;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.RequestUser;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.EnumUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.common.util.StringUtil;
import com.lyusantu.easy.base.module.support.redis.RedisService;
import com.lyusantu.easy.base.module.support.securityprotect.service.SecurityFileService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文件服务
 */
@RequiredArgsConstructor
@Service
public class FileService {

    /**
     * 文件名最大长度
     */
    private static final int FILE_NAME_MAX_LENGTH = 100;

    private final IFileStorageService fileStorageService;

    private final FileMapper fileMapper;

    private final RedisService redisService;

    private final SecurityFileService securityFileService;

    /**
     * 文件上传服务
     *
     * @param file
     * @param folderType 文件夹类型
     * @return
     */
    public ResponseDTO<FileUploadVO> fileUpload(MultipartFile file, Integer folderType, RequestUser requestUser) {
        FileFolderTypeEnum folderTypeEnum = EnumUtil.getEnumByValue(folderType, FileFolderTypeEnum.class);
        if (null == folderTypeEnum) {
            return ResponseDTO.userErrorParam("文件夹错误");
        }

        if (null == file || file.getSize() == 0) {
            return ResponseDTO.userErrorParam("上传文件不能为空");
        }

        // 校验文件名称
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            return ResponseDTO.userErrorParam("上传文件名称不能为空");
        }

        if (originalFilename.length() > FILE_NAME_MAX_LENGTH) {
            return ResponseDTO.userErrorParam("文件名称最大长度为：" + FILE_NAME_MAX_LENGTH);
        }

        // 校验文件大小以及安全性
        ResponseDTO<String> validateFile = securityFileService.checkFile(file);
        if (!validateFile.getOk()) {
            return ResponseDTO.error(validateFile);
        }

        // 进行上传
        ResponseDTO<FileUploadVO> response = fileStorageService.upload(file, folderTypeEnum.getFolder());
        if (!response.getOk()) {
            return response;
        }

        // 上传成功 保存记录数据库
        FileUploadVO uploadVO = response.getData();
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFolderType(folderTypeEnum.getValue());
        fileEntity.setFileName(originalFilename);
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileKey(uploadVO.getFileKey());
        fileEntity.setFileType(uploadVO.getFileType());
        fileEntity.setCreatorId(requestUser == null ? null : requestUser.getUserId());
        fileEntity.setCreatorName(requestUser == null ? null : requestUser.getUserName());
        fileEntity.setCreatorUserType(requestUser == null ? null : requestUser.getUserType().getValue());
        fileMapper.insert(fileEntity);

        // 将fileId 返回给前端
        uploadVO.setFileId(fileEntity.getFileId());

        return response;
    }

    /**
     * 批量获取文件信息
     *
     * @param fileKeyList
     * @return
     */
    public List<FileVO> getFileList(List<String> fileKeyList) {
        if (CollectionUtils.isEmpty(fileKeyList)) {
            return Lists.newArrayList();
        }

        // 查询数据库，并获取 file url
        HashSet<String> fileKeySet = new HashSet<>(fileKeyList);
        Map<String, FileVO> fileMap = fileMapper.selectByFileKeyList(fileKeySet)
                .stream().collect(Collectors.toMap(FileVO::getFileKey, Function.identity()));

        for (FileVO fileVO : fileMap.values()) {
            ResponseDTO<String> fileUrlResponse = fileStorageService.getFileUrl(fileVO.getFileKey());
            if (fileUrlResponse.getOk()) {
                fileVO.setFileUrl(fileUrlResponse.getData());
            }
        }

        // 返回结果
        List<FileVO> result = Lists.newArrayListWithCapacity(fileKeyList.size());
        for (String fileKey : fileKeyList) {
            FileVO fileVO = fileMap.get(fileKey);
            if (fileVO != null) {
                result.add(fileVO);
            }
        }

        return result;
    }


    /**
     * 根据文件绝对路径 获取文件URL
     * 支持单个 key 逗号分隔的形式
     *
     * @param fileKeys
     * @return
     */
    public ResponseDTO<String> getFileUrl(String fileKeys) {
        if (StringUtils.isBlank(fileKeys)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }

        List<String> fileKeyArray = StrUtil.split(fileKeys, StringConst.SEPARATOR);
        List<String> fileUrlList = Lists.newArrayListWithCapacity(fileKeyArray.size());
        for (String fileKey : fileKeyArray) {
            ResponseDTO<String> fileUrlResponse = fileStorageService.getFileUrl(fileKey);
            if (fileUrlResponse.getOk()) {
                fileUrlList.add(fileUrlResponse.getData());
            }
        }
        return ResponseDTO.ok(StringUtil.join(StringConst.SEPARATOR, fileUrlList));
    }


    /**
     * 根据文件服务类型 和 FileKey 下载文件
     */
    public ResponseDTO<FileDownloadVO> getDownloadFile(String fileKey, String userAgent) {
        FileVO fileVO = fileMapper.getByFileKey(fileKey);
        if (fileVO == null) {
            return ResponseDTO.userErrorParam("文件不存在");
        }

        // 根据文件服务类 获取对应文件服务 查询 url
        ResponseDTO<FileDownloadVO> download = fileStorageService.download(fileKey);
        if (download.getOk()) {
            download.getData().getMetadata().setFileName(fileVO.getFileName());
        }
        return download;
    }

    /**
     * 分页查询
     */
    public PageResult<FileVO> queryPage(FileQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<FileVO> list = fileMapper.queryPage(page, queryForm);
        return PageUtil.convert2PageResult(page, list);
    }


}
