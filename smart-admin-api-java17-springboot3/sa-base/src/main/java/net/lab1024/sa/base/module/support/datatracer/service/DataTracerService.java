package net.lab1024.sa.base.module.support.datatracer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.page.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.BeanUtil;
import net.lab1024.sa.base.common.util.IpUtil;
import net.lab1024.sa.base.common.util.PageUtil;
import net.lab1024.sa.base.common.util.RequestUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerConst;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.mapper.DataTracerMapper;
import net.lab1024.sa.base.module.support.datatracer.domain.entity.DataTracerEntity;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerForm;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerQueryForm;
import net.lab1024.sa.base.module.support.datatracer.domain.vo.DataTracerVO;
import net.lab1024.sa.base.module.support.datatracer.manager.DataTracerManger;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据变动记录 Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataTracerService {

    private final DataTracerMapper dataTracerMapper;

    private final DataTracerManger dataTracerManger;

    private final DataTracerChangeContentService dataTracerChangeContentService;

    /**
     * 获取变更内容
     *
     * @param object
     * @return
     */
    public String getChangeContent(Object object) {
        return dataTracerChangeContentService.getChangeContent(object);
    }

    /**
     * 获取变更内容
     */
    public String getChangeContent(Object oldObject, Object newObject) {
        return dataTracerChangeContentService.getChangeContent(oldObject, newObject);
    }


    /**
     * 获取变更内容
     */
    public <T> String getChangeContent(List<T> oldObjectList, List<T> newObjectList) {
        return dataTracerChangeContentService.getChangeContent(oldObjectList, newObjectList);
    }


    /**
     * 保存【修改】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void update(Long dataId, DataTracerTypeEnum type, Object oldObject, Object newObject) {
        DataTracerForm form = DataTracerForm.builder()
                .dataId(dataId)
                .type(type)
                .content(dataTracerChangeContentService.getChangeContent(oldObject, newObject))
                .build();
        this.addTrace(form);
    }


    /**
     * 保存【新增】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void insert(Long dataId, DataTracerTypeEnum type) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(DataTracerConst.INSERT).build();
        this.addTrace(form);
    }

    /**
     * 保存【删除】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void delete(Long dataId, DataTracerTypeEnum type) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(DataTracerConst.DELETE).build();
        this.addTrace(form);
    }

    /**
     * 保存【删除】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void delete(Long dataId, DataTracerTypeEnum type, Object object) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(DataTracerConst.DELETE).build();
        this.addTrace(form);
    }

    /**
     * 保存【批量删除】数据变动记录
     *
     * @param dataIdList
     * @param type
     */
    public void batchDelete(List<Long> dataIdList, DataTracerTypeEnum type) {
        if (CollectionUtils.isEmpty(dataIdList)) {
            return;
        }

        this.addTraceList(dataIdList.stream().map(e -> DataTracerForm.builder()
                        .dataId(e)
                        .type(type)
                        .content(DataTracerConst.DELETE)
                        .build())
                .collect(Collectors.toList())
        );
    }

    /**
     * 保存数据变动记录
     *
     * @param dataId
     * @param type
     * @param content
     */
    public void addTrace(Long dataId, DataTracerTypeEnum type, String content) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(content).build();
        this.addTrace(form);
    }

    /**
     * 保存数据变动记录
     */
    public void addTrace(DataTracerForm tracerForm) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        this.addTrace(tracerForm, requestUser);
    }


    /**
     * 保存数据变动记录
     */
    public void addTrace(DataTracerForm tracerForm, RequestUser requestUser) {
        DataTracerEntity tracerEntity = BeanUtil.copy(tracerForm, DataTracerEntity.class);
        tracerEntity.setType(tracerForm.getType().getValue());
        if (requestUser != null) {
            tracerEntity.setIp(requestUser.getIp());
            tracerEntity.setIpRegion(IpUtil.getRegion(requestUser.getIp()));
            tracerEntity.setUserAgent(requestUser.getUserAgent());
            tracerEntity.setUserId(requestUser.getUserId());
            tracerEntity.setUserType(requestUser.getUserType().getValue());
            tracerEntity.setUserName(requestUser.getUserName());
        }
        dataTracerManger.save(tracerEntity);
    }

    /**
     * 批量保存数据变动记录
     */
    public void addTraceList(List<DataTracerForm> tracerFormList) {
        RequestUser requestUser = RequestUtil.getRequestUser();
        this.addTraceList(tracerFormList, requestUser);
    }

    /**
     * 批量保存数据变动记录
     */
    public void addTraceList(List<DataTracerForm> tracerFormList, RequestUser requestUser) {
        if (CollectionUtils.isEmpty(tracerFormList)) {
            return;
        }

        List<DataTracerEntity> tracerEntityList = tracerFormList.stream().map(e -> {
            DataTracerEntity tracerEntity = BeanUtil.copy(e, DataTracerEntity.class);
            tracerEntity.setType(e.getType().getValue());
            tracerEntity.setIp(requestUser.getIp());
            tracerEntity.setIpRegion(IpUtil.getRegion(requestUser.getIp()));
            tracerEntity.setUserAgent(requestUser.getUserAgent());
            tracerEntity.setUserId(requestUser.getUserId());
            tracerEntity.setUserType(requestUser.getUserType().getValue());
            tracerEntity.setUserName(requestUser.getUserName());
            return tracerEntity;
        }).collect(Collectors.toList());
        dataTracerManger.saveBatch(tracerEntityList);
    }


    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DataTracerVO>> query(DataTracerQueryForm queryForm) {
        Page page = PageUtil.convert2PageQuery(queryForm);
        List<DataTracerVO> list = dataTracerMapper.query(page, queryForm);
        PageResult<DataTracerVO> pageResult = PageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

}
