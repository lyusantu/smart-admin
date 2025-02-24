<template>
  <div class="detail-header">
    <a-page-header :title="'项目名称：' + detail.projectName">
      <div>
        <a-descriptions size="small" :column="1">
          <a-descriptions-item label="项目状态">{{ getState(detail.state) }}</a-descriptions-item>
          <a-descriptions-item label="项目开始时间">{{ detail.startTime }}</a-descriptions-item>
          <a-descriptions-item label="项目结束时间">{{ detail.endTime }}</a-descriptions-item>
          <a-descriptions-item label="预估采购成本">{{ detail.cost }}</a-descriptions-item>
        </a-descriptions>
      </div>
    </a-page-header>
  </div>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="employee" tab="节点概览">
        <NodeDetail :projectId="projectId" />
      </a-tab-pane>
      <a-tab-pane key="bank" tab="支出明细">
        <ExpensesList :projectId="projectId" />
      </a-tab-pane>
    </a-tabs>
  </a-card>

</template>

<script setup>
  import { computed, onMounted, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import { projectApi } from '/@/api/project/pm/pm-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import NodeDetail from './components/pm-node-detail.vue';
  import ExpensesList from './components/pm-expenses-list.vue';
  import { CATEGORY_TYPE_ENUM } from '../../../constants/business/erp/category-const.js';

  const route = useRoute();
  let projectId = undefined;
  let detail = ref({});

  onMounted(() => {
    projectId = Number(route.query.projectId);
    getDetail();
  });

  async function getDetail() {
    try {
      let result = await projectApi.detail(projectId);
      detail.value = result.data;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  function getState(state) {
    if (state === 0) {
      return '未开始';
    } else if (state === 1) {
      return '进行中';
    } else if (state === 2) {
      return '已挂起';
    } else if (state === 3) {
      return '已完成';
    } else if (state === 4) {
      return '已关闭';
    } else if (state === 5) {
      return '已延期';
    } else {
      return '状态异常';
    }
  }
</script>

<style lang="less" scoped>
  .detail-header {
    background-color: #fff;
    padding: 10px;
  }
</style>
