<template>
  <a-modal
    :title="form.projectNodeId ? '编辑节点' : '添加节点'"
    :width="500"
    :open="visibleFlag"
    @cancel="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="节点名称" name="nodeName">
        <a-input style="width: 100%" v-model:value="form.nodeName" placeholder="请输入节点名称" />
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

  async function show(data) {
    Object.assign(form, formDefault);
    if (data && !_.isEmpty(data)) {
      Object.assign(form, data);
    }

    console.log('data=' + form.projectNodeId);

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
    projectNodeId: undefined,
    projectId: undefined, //项目ID
    nodeName: undefined, //节点名称
  };

  let form = reactive({ ...formDefault });

  const rules = {
    nodeName: [{ required: true, message: '请输入节点名称' }],
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
      if (form.projectNodeId) {
        // 编辑
        await projectApi.updateProjectNode(form);
      } else {
        await projectApi.addProjectNode(form);
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
