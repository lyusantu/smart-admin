package com.lyusantu.easy.base.module.support.codegenerator.service.variable.front;

import com.lyusantu.easy.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import com.lyusantu.easy.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;

import java.util.*;

public class ApiVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        return variablesMap;
    }
}
