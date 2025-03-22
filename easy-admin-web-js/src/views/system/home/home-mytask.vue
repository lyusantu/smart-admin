<template>
  <default-home-card icon="CalculatorTwoTone" title="我的任务">
    <a-table size="small" :dataSource="tableData" bordered :columns="columns" rowKey="bankId" :pagination="false">
      <template #bodyCell="{ text, record, column }">

        <template v-if="column.dataIndex === 'startTime'">
          <span>{{ record.startTime }} - {{ record.endTime }}</span>
        </template>

        <template v-if="column.dataIndex === 'state'">
          <span>{{ $smartEnumPlugin.getDescByValue('PROJECT_STAGE_STATE', text) }}</span>
        </template>

        <template v-if="column.dataIndex === 'priority'">
          <span>{{ $smartEnumPlugin.getDescByValue('PROJECT_STAGE_PRIORITY', text) }}</span>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="detail(record)" type="link">查看</a-button>
          </div>
        </template>
      </template>
    </a-table>
  </default-home-card>
</template>
<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
  import { projectApi } from '/@/api/project/pm/pm-api';

  const props = defineProps({
    projectId: {
      type: Number,
      default: null,
    },
  });

  const columns = ref([
    {
      title: '项目',
      dataIndex: 'projectName',
    },
    {
      title: '节点',
      dataIndex: 'projectNodeName',
    },
    {
      title: '阶段',
      dataIndex: 'stageName',
    },
    {
      title: '优先级',
      dataIndex: 'priority',
    },
    {
      title: '状态',
      dataIndex: 'state',
    },
    {
      title: '项目时间',
      dataIndex: 'startTime',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
  ]);

  const tableLoading = ref(false);
  const tableData = ref([]);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let result = await projectApi.listMyTask();
      tableData.value = result.data;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  watch(
    () => props.projectId,
    (value) => {
      if (value) {
        ajaxQuery();
      }
    },
    {
      immediate: true,
    }
  );
</script>
