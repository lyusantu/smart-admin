package net.lab1024.sa.base.module.support.codegenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.codegenerator.domain.entity.CodeGeneratorConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 代码生成配置 Mapper
 */
@Mapper
@Component
public interface CodeGeneratorConfigMapper extends BaseMapper<CodeGeneratorConfigEntity> {

}
