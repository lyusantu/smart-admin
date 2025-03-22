<template>
  <a-modal
    :title="form.stageId ? '编辑阶段' : '添加阶段'"
    :width="500"
    :open="visibleFlag"
    @cancel="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="所属节点" name="projectNodeId">
        <a-select v-model:value="form.projectNodeId" style="width: 100%" :allowClear="true" placeholder="请选择项目节点">
          <a-select-option v-for="item in listProjectState" :key="item.projectNodeId" :value="item.projectNodeId">
            {{ item.projectNodeName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="阶段名称" name="stageName">
        <a-input style="width: 100%" v-model:value="form.stageName" placeholder="请输入阶段名称" />
      </a-form-item>

      <a-form-item label="优先级" name="priority">
        <smart-enum-select class="form-item" v-model:value="form.priority" placeholder="请选择优先级" enum-name="PROJECT_STAGE_PRIORITY" />
      </a-form-item>

      <a-form-item label="负责人" name="director">
        <EmployeeSelect ref="employeeSelect" placeholder="请选择负责人" width="100%" v-model:value="form.director" :leaveFlag="false" />
      </a-form-item>

      <a-form-item label="阶段时间" name="stageTime" class="smart-query-form-item">
        <a-range-picker v-model:value="form.stageTime" :presets="defaultTimeRanges" style="width: 100%" @change="onChangeStartTime" />
      </a-form-item>

      <a-form-item label="阶段内容：" name="context">
        <a-textarea v-model:value="form.context" placeholder="请输入阶段内容" :rows="3" />
      </a-form-item>

      <a-form-item label="备注：" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
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
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import { projectApi } from '/@/api/project/pm/pm-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import EmployeeSelect from '/@/components/system/employee-select/index.vue';
  import dayjs from "dayjs";

  // ------------------------ 事件 ------------------------
  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);
  const listProjectState = ref([]);

  async function show(data) {
    Object.assign(form, formDefault);
    if (data && !_.isEmpty(data)) {
      Object.assign(form, data);
    }

    let result = await projectApi.listProjectNode(form.projectId);
    listProjectState.value = result.data;

    if (form.stageId) {
      let stage = await projectApi.getProjectStage(form.stageId);
      form.stageName = stage.data.stageName;
      form.priority = stage.data.priority;
      form.director = stage.data.director;
      form.projectNodeId = stage.data.projectNodeId;
      form.context = stage.data.context;
      form.remark = stage.data.remark;
      form.startTime = stage.data.startTime;
      form.endTime = stage.data.endTime;
      form.stageTime = [dayjs(form.startTime), dayjs(form.endTime)];
    }

    visibleFlag.value = true;
    await nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  function onChangeStartTime(dates, dateStrings) {
    form.startTime = dateStrings[0];
    form.endTime = dateStrings[1];
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const nodeList = ref([]);

  const formDefault = {
    stageId: undefined,
    projectNodeName: undefined,
    stageName: undefined,
    priority: undefined,
    director: undefined,
    stageTime: undefined,
    context: undefined,
    remark: undefined,
    startTime: undefined,
    endTime: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
    projectNodeId: [{ required: true, message: '请选择所属节点' }],
    stageName: [{ required: true, message: '请输入阶段名称' }],
    priority: [{ required: true, message: '请选择优先级' }],
    director: [{ required: true, message: '请选择负责人' }],
    stageTime: [{ required: true, message: '请选择阶段时间' }],
    context: [{ required: true, message: '请输入阶段内容' }],
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
      if (form.stageId) {
        // 编辑
        await projectApi.updateProjectStage(form);
      } else {
        await projectApi.addProjectStage(form);
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
