package com.lyusantu.easy.base.common.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import com.lyusantu.easy.base.module.support.dict.domain.vo.DictValueVO;
import com.lyusantu.easy.base.module.support.dict.service.DictCacheService;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 字典序列化
 */
public class DictValueVoSerializer extends JsonSerializer<String> {

    @Resource
    private DictCacheService dictCacheService;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isEmpty(value)) {
            jsonGenerator.writeObject(Lists.newArrayList());
            return;
        }

        String[] valueCodeArray = value.split(",");
        List<String> valueCodeList = Arrays.asList(valueCodeArray);
        List<DictValueVO> dictValueVOList = Lists.newArrayList();
        valueCodeList.forEach(e->{
            if(StringUtils.isNotBlank(e)){
                DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(e);
                if(dictValueVO != null){
                    dictValueVOList.add(dictValueVO);
                }
            }
        });
        jsonGenerator.writeObject(dictValueVOList);

    }
}
