<!--
  * 项目支付明细表
  *
  * @Author:    lyusantu
  * @Date:      2025-02-24 10:14:42
  * @Copyright  lyusantu
-->
<template>
  <a-modal :title="form.expensesId ? '编辑' : '添加'" :width="600" :open="visibleFlag" @cancel="onClose" :maskClosable="false" :destroyOnClose="true">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="费用类型" name="expensesType">
        <smart-enum-select class="form-item" v-model:value="form.expensesType" placeholder="请选择费用类型" enum-name="PROJECT_EXPENSES_TYPE" />
      </a-form-item>

      <a-form-item label="费用名称" name="expensesName">
        <a-input style="width: 100%" v-model:value="form.expensesName" placeholder="费用名称" />
      </a-form-item>

      <a-form-item label="费用金额" name="amount">
        <a-input-number style="width: 100%" v-model:value="form.amount" placeholder="费用金额" />
      </a-form-item>
      <a-form-item label="发票" name="invoices">
        <Upload
          accept=".jpg,.jpeg,.png,.pdf"
          :maxUploadSize="9"
          buttonText="点击上传发票"
          :default-file-list="form.invoices || []"
          listType="picture-card"
          @change="uploadChange"
          :folder="FILE_FOLDER_TYPE_ENUM.COMMON.value"
        />
      </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { projectApi } from '/@/api/project/pm/pm-api';
  import Upload from '/@/components/support/file-upload/index.vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    // 使用字典时把下面这注释修改成自己的字典字段 有多个字典字段就复制多份同理修改 不然打开表单时不显示字典初始值
    // if (form.status && form.status.length > 0) {
    //   form.status = form.status.map((e) => e.valueCode);
    // }
    form.projectId = rowData.projectId;
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();
  const formDefault = {
    expensesId: undefined, //费用ID
    projectId: undefined, //项目Id
    expensesType: undefined, //费用类型
    expensesName: undefined, //费用名称
    amount: undefined, //费用金额
    invoices: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
    projectId: [{ required: true, message: '项目Id 必填' }],
    expensesType: [{ required: true, message: '费用类型 必填' }],
    expensesName: [{ required: true, message: '费用名称 必填' }],
    amount: [{ required: true, message: '费用金额 必填' }],
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    SmartLoading.show();
    try {
      if (form.expensesId) {
        await projectApi.updateProjectExpenses(form);
      } else {
        await projectApi.addProjectExpenses(form);
      }
      message.success('操作成功');
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  function uploadChange(fileList) {
    form.invoices = fileList;
  }

  defineExpose({
    show,
  });
</script>
