<!--
  * 节点模板表
  *
  * @Author:    lyusantu
  * @Date:      2025-02-19 14:33:24
  * @Copyright  lyusantu
-->
<template>
  <a-modal :title="form.nodeId ? '编辑' : '添加'" :width="600" :open="visibleFlag" @cancel="onClose" :maskClosable="false" :destroyOnClose="true">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="节点模板名称" name="nodeName">
        <a-input style="width: 100%" v-model:value="form.nodeName" placeholder="节点模板名称" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-input style="width: 100%" v-model:value="form.remark" placeholder="备注" />
      </a-form-item>

      <a-form-item
        v-for="(item, index) in form.list"
        :key="index"
        :label="`节点${index + 1}名称`"
        :name="[`list`, index, 'nodeName']"
        :rules="[{ required: true, message: `节点${index + 1}名称必填` }]"
      >
        <div style="display: flex; align-items: center">
          <a-input v-model:value="item.nodeName" style="width: 300px; margin-right: 8px" clearable show-count :maxlength="10" />
          <a-button type="primary" @click="addList(index)">添加</a-button>
          <a-button type="primary" danger @click="removeList(index)" style="margin-left: 8px">删除</a-button>
        </div>
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
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { nodeApi } from '/@/api/project/node/node-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { toJSON } from 'lodash/seq.js';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  async function show(rowData) {
    Object.assign(form, formDefault);
    // 每次都清空节点list
    form.list = [{ name: null, sign: 0 }];

    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      // 编辑时需要查询节点列表并渲染
      if (rowData.nodeId !== undefined) {
        // form.list = [{ name: '11', sign: 0 },{name: '2', sign: 1}];
        let result = await nodeApi.listNode(rowData.nodeId);
        form.list = result.data;
      }
    }
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
    list: [
      {
        nodeName: null,
        sign: 0,
      },
    ],
    nodeId: undefined,
    nodeName: undefined, //节点模板名称
    remark: undefined, //备注
  };

  let form = reactive({ ...formDefault });

  const rules = {
    nodeName: [{ required: true, message: '节点模板名称必填' }],
  };

  const addList = (index) => {
    form.list.splice(index + 1, 0, {
      name: null,
      sign: 0,
    });
  };

  const removeList = (index) => {
    if (form.list.length <= 1) return;

    Modal.confirm({
      title: '确定要删除吗？',
      content: '删除后，该信息将不可恢复',
      okText: '删除',
      okType: 'danger',
      onOk() {
        form.list.splice(index, 1);
      },
      cancelText: '取消',
    });
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      await save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    form.list.map((item, index) => (item.sign = index));

    SmartLoading.show();
    try {
      if (form.nodeId) {
        await nodeApi.update(form);
      } else {
        await nodeApi.add(form);
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
