package com.lyusantu.easy.base.common.json.serializer.enumeration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.lyusantu.easy.base.common.constant.StringConst;
import com.lyusantu.easy.base.common.enumeration.BaseEnum;
import com.lyusantu.easy.base.common.util.EnumUtil;
import com.lyusantu.easy.base.common.util.StringUtil;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 枚举 序列化
 */
public class EnumSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private Class<? extends BaseEnum> enumClazz;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(value);
        String fieldName = gen.getOutputContext().getCurrentName() + "Desc";
        Object desc;
        // 多个枚举类 逗号分割
        if (value instanceof String && String.valueOf(value).contains(StringConst.SEPARATOR)) {
            desc = StringUtil.splitConvertToIntList(String.valueOf(value), StringConst.SEPARATOR)
                                  .stream().map(e -> EnumUtil.getEnumDescByValue(e, enumClazz)).collect(Collectors.toList());

        } else {
            BaseEnum anEnum = EnumUtil.getEnumByValue(value, enumClazz);
            desc = null != anEnum ? anEnum.getDesc() : null;
        }
        gen.writeObjectField(fieldName, desc);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        EnumSerialize annotation = property.getAnnotation(EnumSerialize.class);
        if (null == annotation) {
            return prov.findValueSerializer(property.getType(), property);
        }
        enumClazz = annotation.value();
        return this;
    }
}
