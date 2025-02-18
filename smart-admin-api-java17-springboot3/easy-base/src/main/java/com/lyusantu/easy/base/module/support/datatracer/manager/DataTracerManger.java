package com.lyusantu.easy.base.module.support.datatracer.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyusantu.easy.base.module.support.datatracer.mapper.DataTracerMapper;
import com.lyusantu.easy.base.module.support.datatracer.domain.entity.DataTracerEntity;
import org.springframework.stereotype.Service;

/**
 * manager层
 */
@Service
public class DataTracerManger extends ServiceImpl<DataTracerMapper, DataTracerEntity> {
}
