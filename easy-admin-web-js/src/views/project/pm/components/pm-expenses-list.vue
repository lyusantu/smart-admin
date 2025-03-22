<template>
  <a-card size="small" :bordered="false" :hoverable="false">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="add" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          新增支出明细
        </a-button>
      </div>
      <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="null" :refresh="ajaxQuery" />
    </a-row>

    <a-table :scroll="{ x: 1300 }" size="small" :dataSource="tableData" bordered :columns="columns" rowKey="bankId" :pagination="false">
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'invoicesCount'">
          <template v-if="text === 0">
            <a-button type="link">无</a-button>
          </template>
          <template v-else>
            <a-button @click="download(record.invoices)" type="link">下载{{ record.invoicesCount }}张发票</a-button>
          </template>
        </template>

        <template v-if="column.dataIndex === 'expensesType'">
          <span>{{ $smartEnumPlugin.getDescByValue('PROJECT_EXPENSES_TYPE', text) }}</span>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="update(record)" type="link">修改</a-button>
            <a-button @click="onDelete(record.expensesId)" type="link" danger>删除</a-button>
          </div>
        </template>
      </template>
    </a-table>
  </a-card>

  <AddForm ref="addForm" @reloadList="ajaxQuery" />
</template>
<script setup>
  import { ref, watch } from 'vue';
  import { fileApi } from '/@/api/support/file-api';
  import { projectApi } from '/@/api/project/pm/pm-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { Modal } from 'ant-design-vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import AddForm from './pm-expenses-form.vue';

  const props = defineProps({
    projectId: {
      type: Number,
      default: null,
    },
  });

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'serialNumber',
    },
    {
      title: '费用类型',
      dataIndex: 'expensesType',
      ellipsis: true,
    },
    {
      title: '费用名称',
      dataIndex: 'expensesName',
      ellipsis: true,
    },
    {
      title: '费用金额',
      dataIndex: 'amount',
    },
    {
      title: '发票',
      dataIndex: 'invoicesCount',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 160,
    },
  ]);

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let result = await projectApi.listProjectExpenses(props.projectId);
      tableData.value = result.data;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const addForm = ref();

  function add() {
    let data = {
      projectId: props.projectId,
    };
    addForm.value.show(data);
  }

  async function download(invoices) {
    console.log(invoices);
    const array = JSON.parse(invoices);
    try {
      for (const invoice of array) {
        await fileApi.downLoadFile(invoice.fileKey);
      }
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function update(data) {
    let param = {
      projectId: props.projectId,
      expensesId: data.expensesId, //费用ID
      expensesType: data.expensesType, //费用类型
      expensesName: data.expensesName, //费用名称
      amount: data.amount, //费用金额
      invoices: JSON.parse(data.invoices),
    };
    addForm.value.show(param);
  }

  function onDelete(expensesId) {
    Modal.confirm({
      title: '确定要删除吗？',
      content: '删除后，该信息将不可恢复',
      okText: '删除',
      okType: 'danger',
      onOk() {
        del(expensesId);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  function onDownload() {}

  async function del(expensesId) {
    try {
      SmartLoading.show();
      await projectApi.deleteProjectExpenses(expensesId);
      ajaxQuery();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
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
