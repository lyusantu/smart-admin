package com.lyusantu.easy.base.module.support.codegenerator.service.variable.front;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.base.CaseFormat;
import com.lyusantu.easy.base.common.util.StringUtil;
import com.lyusantu.easy.base.module.support.codegenerator.constant.CodeFrontComponentEnum;
import com.lyusantu.easy.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import com.lyusantu.easy.base.module.support.codegenerator.domain.model.CodeField;
import com.lyusantu.easy.base.module.support.codegenerator.domain.model.CodeInsertAndUpdateField;
import com.lyusantu.easy.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;

import java.util.*;

public class FormVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();
        List<Map<String, Object>> fieldsVariableList = new ArrayList<>();
        List<CodeInsertAndUpdateField> fieldList = form.getInsertAndUpdate().getFieldList();

        HashSet<String> frontImportSet = new HashSet<>();

        for (CodeInsertAndUpdateField field : fieldList) {
            // 不存在 添加 和 更新
            if (!(field.getInsertFlag() || field.getUpdateFlag())) {
                continue;
            }

            Map<String, Object> objectMap = BeanUtil.beanToMap(field);

            CodeField codeField = getCodeFieldByColumnName(field.getColumnName(), form);
            if (codeField == null) {
                continue;
            }
            objectMap.put("label", codeField.getLabel());
            objectMap.put("fieldName", codeField.getFieldName());
            objectMap.put("dict", codeField.getDict());

            if (StringUtil.isNotBlank(codeField.getEnumName())) {
                String upperUnderscoreEnum = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, codeField.getEnumName());
                objectMap.put("upperUnderscoreEnum", upperUnderscoreEnum);
            }

            fieldsVariableList.add(objectMap);

            if (CodeFrontComponentEnum.ENUM_SELECT.equalsValue(field.getFrontComponent())) {
                frontImportSet.add("import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';");
            }

            if (CodeFrontComponentEnum.BOOLEAN_SELECT.equalsValue(field.getFrontComponent())) {
                frontImportSet.add("import BooleanSelect from '/@/components/framework/boolean-select/index.vue';");
            }

            if (CodeFrontComponentEnum.DICT_SELECT.equalsValue(field.getFrontComponent())) {
                frontImportSet.add("import DictSelect from '/@/components/support/dict-select/index.vue';");
            }

            if (CodeFrontComponentEnum.FILE_UPLOAD.equalsValue(field.getFrontComponent())) {
                frontImportSet.add("import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';");
                frontImportSet.add("import FileUpload from '/@/components/support/file-upload/index.vue';");
            }
        }

        variablesMap.put("formFields", fieldsVariableList);
        variablesMap.put("frontImportList", new ArrayList<>(frontImportSet));

        return variablesMap;
    }
}
