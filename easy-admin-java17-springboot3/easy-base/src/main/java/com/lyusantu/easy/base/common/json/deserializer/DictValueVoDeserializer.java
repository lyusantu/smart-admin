package com.lyusantu.easy.base.common.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典反序列化
 */
@Slf4j
public class DictValueVoDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<String> list = new ArrayList<>();
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode listOrObjectNode = objectCodec.readTree(jsonParser);
        String deserialize = "";
        try {
            if (listOrObjectNode.isArray()) {
                for (JsonNode node : listOrObjectNode) {
                    list.add(node.asText());
                }
            } else {
                list.add(listOrObjectNode.asText());
            }
            deserialize = String.join(",", list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            deserialize = listOrObjectNode.asText();
        }
        return deserialize;
    }


}
