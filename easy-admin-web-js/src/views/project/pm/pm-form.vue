<!--
  * 项目信息表
  *
  * @Author:    lyusantu
  * @Date:      2025-02-20 10:29:51
  * @Copyright  lyusantu
-->
<template>
  <a-modal :title="form.projectId ? '编辑' : '添加'" :width="600" :open="visibleFlag" @cancel="onClose" :maskClosable="false" :destroyOnClose="true">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="项目名称" name="projectName">
        <a-input style="width: 100%" v-model:value="form.projectName" placeholder="项目名称" />
      </a-form-item>
      <a-form-item label="采购成本" name="cost">
        <a-input-number style="width: 100%" v-model:value="form.cost" placeholder="采购成本" />
      </a-form-item>
      <a-form-item label="项目开始时间" name="startTime">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.startTime" style="width: 100%" placeholder="项目开始时间" />
      </a-form-item>
      <a-form-item label="项目结束时间" name="endTime">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.endTime" style="width: 100%" placeholder="项目结束时间" />
      </a-form-item>
      <!-- 新增的下拉框 -->
      <a-form-item label="节点模版" name="nodeTemplateId" :hidden="form.projectId">
        <a-select v-model:value="form.nodeTemplateId" style="width: 100%" :allowClear="true" placeholder="请选择节点模板">
          <a-select-option v-for="item in nodeList" :key="item.nodeId" :value="item.nodeId">
            {{ item.nodeName }}
          </a-select-option>
        </a-select>
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
  import { smartSentry } from '/@/lib/smart-sentry';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  async function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }

    // 节点模版下拉框
    let result = await projectApi.listTemplate();
    nodeList = await result.data;

    visibleFlag.value = true;
    await nextTick(() => {
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
    projectId: undefined, //项目ID
    projectName: undefined, //项目名称
    cost: undefined, //采购成本
    startTime: undefined, //项目开始时间
    endTime: undefined, //项目结束时间
    nodeTemplateId: undefined, // 项目模版ID
  };

  let form = reactive({ ...formDefault });

  let nodeList = [];

  const rules = {
    projectName: [{ required: true, message: '项目名称 必填' }],
    cost: [{ required: true, message: '采购成本 必填' }],
    startTime: [{ required: true, message: '项目开始时间 必填' }],
    endTime: [{ required: true, message: '项目结束时间 必填' }],
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
      if (form.projectId) {
        await projectApi.update(form);
      } else {
        await projectApi.add(form);
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

  defineExpose({
    show,
  });
</script>
