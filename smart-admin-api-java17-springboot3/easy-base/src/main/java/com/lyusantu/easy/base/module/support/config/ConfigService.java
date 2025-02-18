package com.lyusantu.easy.base.module.support.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.config.domain.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.code.UserErrorCode;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.constant.ReloadConst;
import com.lyusantu.easy.base.module.support.reload.core.annoation.EasyReload;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置业务类
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ConfigService {

    /**
     * 一个简单的系统配置缓存
     */
    private final ConcurrentHashMap<String, ConfigEntity> CONFIG_CACHE = new ConcurrentHashMap<>();

    private final ConfigMapper configMapper;

    @EasyReload(ReloadConst.CONFIG_RELOAD)
    public void configReload(String param) {
        this.loadConfigCache();
    }

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void loadConfigCache() {
        CONFIG_CACHE.clear();
        List<ConfigEntity> entityList = configMapper.selectList(null);
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(entity -> this.CONFIG_CACHE.put(entity.getConfigKey().toLowerCase(), entity));
        log.info("系统配置缓存初始化完毕，缓存数量：{}", CONFIG_CACHE.size());
    }

    /**
     * 刷新系统设置缓存
     */
    private void refreshConfigCache(Long configId) {
        // 重新查询 加入缓存
        ConfigEntity configEntity = configMapper.selectById(configId);
        if (null == configEntity) {
            return;
        }
        this.CONFIG_CACHE.put(configEntity.getConfigKey().toLowerCase(), configEntity);
    }

    /**
     * 分页查询系统配置
     */
    public ResponseDTO<PageResult<ConfigVO>> queryConfigPage(ConfigQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<ConfigEntity> entityList = configMapper.queryByPage(page, queryForm);
        PageResult<ConfigVO> pageResult = PageUtil.convert2PageResult(page, entityList, ConfigVO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询配置缓存
     */
    public ConfigVO getConfig(ConfigKeyEnum configKey) {
        return this.getConfig(configKey.getValue());
    }

    /**
     * 查询配置缓存
     */
    public ConfigVO getConfig(String configKey) {
        if (StrUtil.isBlank(configKey)) {
            return null;
        }
        ConfigEntity entity = this.CONFIG_CACHE.get(configKey.toLowerCase());
        return BeanUtil.copy(entity, ConfigVO.class);
    }

    /**
     * 查询配置缓存参数
     */
    public String getConfigValue(ConfigKeyEnum configKey) {
        ConfigVO config = this.getConfig(configKey);
        return config == null ? null : config.getConfigValue();
    }

    /**
     * 根据参数key查询 并转换为对象
     */
    public <T> T getConfigValue2Obj(ConfigKeyEnum configKey, Class<T> clazz) {
        String configValue = this.getConfigValue(configKey);
        return JSON.parseObject(configValue, clazz);
    }

    /**
     * 添加系统配置
     */
    public ResponseDTO<String> add(ConfigAddForm configAddForm) {
        ConfigEntity entity = configMapper.selectByKey(configAddForm.getConfigKey());
        if (null != entity) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        entity = BeanUtil.copy(configAddForm, ConfigEntity.class);
        configMapper.insert(entity);

        // 刷新缓存
        this.refreshConfigCache(entity.getConfigId());
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     */
    public ResponseDTO<String> updateConfig(ConfigUpdateForm updateDTO) {
        Long configId = updateDTO.getConfigId();
        ConfigEntity entity = configMapper.selectById(configId);
        if (null == entity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ConfigEntity alreadyEntity = configMapper.selectByKey(updateDTO.getConfigKey());
        if (null != alreadyEntity && !Objects.equals(configId, alreadyEntity.getConfigId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "config key 已存在");
        }

        // 更新数据
        entity = BeanUtil.copy(updateDTO, ConfigEntity.class);
        configMapper.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     */
    public ResponseDTO<String> updateValueByKey(ConfigKeyEnum key, String value) {
        ConfigVO config = this.getConfig(key);
        if (null == config) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 更新数据
        Long configId = config.getConfigId();
        ConfigEntity entity = new ConfigEntity();
        entity.setConfigId(configId);
        entity.setConfigValue(value);
        configMapper.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }
}
