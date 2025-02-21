package com.lyusantu.easy.base.module.support.codegenerator.service.variable.front;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.base.CaseFormat;
import com.lyusantu.easy.base.module.support.codegenerator.constant.CodeQueryFieldQueryTypeEnum;
import com.lyusantu.easy.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;
import com.lyusantu.easy.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import com.lyusantu.easy.base.module.support.codegenerator.domain.model.CodeField;
import com.lyusantu.easy.base.module.support.codegenerator.domain.model.CodeQueryField;

import java.util.*;

public class ListVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        List<Map<String, Object>> variableList = new ArrayList<>();
        List<CodeQueryField> queryFields = form.getQueryFields();
        HashSet<String> frontImportSet = new HashSet<>();
        frontImportSet.add("import " + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, form.getBasic().getModuleName()) + "Form from './" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, form.getBasic().getModuleName()) + "-form.vue';");

        for (CodeQueryField queryField : queryFields) {
            Map<String, Object> objectMap = BeanUtil.beanToMap(queryField);

            CodeField codeField = getCodeFieldByColumnName(queryField.getColumnNameList().get(0), form);
            objectMap.put("frontEnumName", codeField.getEnumName());
            objectMap.put("dict", codeField.getDict());

            if(CodeQueryFieldQueryTypeEnum.ENUM.equalsValue(queryField.getQueryTypeEnum())){
                frontImportSet.add("import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';");
            }

            if(CodeQueryFieldQueryTypeEnum.DICT.equalsValue(queryField.getQueryTypeEnum())){
                frontImportSet.add("import DictSelect from '/@/components/support/dict-select/index.vue';");
            }

            if(CodeQueryFieldQueryTypeEnum.DATE_RANGE.equalsValue(queryField.getQueryTypeEnum())){
                frontImportSet.add("import { defaultTimeRanges } from '/@/lib/default-time-ranges';");
            }
            variableList.add(objectMap);

        }
        variablesMap.put("queryFields",variableList);
        variablesMap.put("frontImportList",new ArrayList<>(frontImportSet));
        return variablesMap;
    }
}
