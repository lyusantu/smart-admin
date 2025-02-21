<!--
  * 项目信息表
  *
  * @Author:    lyusantu
  * @Date:      2025-02-20 10:29:51
  * @Copyright  lyusantu
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="请输入项目名称" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.projectName" placeholder="请输入项目名称" />
      </a-form-item>
      <a-form-item label="项目开始时间" class="smart-query-form-item">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="queryForm.startTime" style="width: 200px" />
      </a-form-item>
      <a-form-item label="项目结束时间" class="smart-query-form-item">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="queryForm.endTime" style="width: 200px" />
      </a-form-item>
      <a-form-item label="状态" class="smart-query-form-item">
        <DictSelect keyCode="PROJECT_STATE" placeholder="请选择状态" v-model:value="queryForm.state" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button type="primary" @click="onSearch">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->

  <a-card size="small" :bordered="false" :hoverable="true">
    <!---------- 表格操作行 begin ----------->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="showForm" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
        <a-button @click="confirmBatchDelete" type="primary" danger :disabled="selectedRowKeyList.length === 0">
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="projectId"
      bordered
      :loading="tableLoading"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'state'">
          <template v-if="text === 0">
            <a-tag color="gray">未开始</a-tag>
          </template>
          <template v-else-if="text === 1">
            <a-tag color="success">进行中</a-tag>
          </template>
          <template v-else-if="text === 2">
            <a-tag color="warning">已挂起</a-tag>
          </template>
          <template v-else-if="text === 3">
            <a-tag color="success">已完成</a-tag>
          </template>
          <template v-else-if="text === 4">
            <a-tag color="gray">已关闭</a-tag>
          </template>
          <template v-else-if="text === 5">
            <a-tag color="warning">已延期{{ record.projectId }}天</a-tag>
          </template>
          <template v-else>
            <a-tag color="error">状态异常</a-tag>
          </template>
        </template>

        <template v-if="column.dataIndex === 'startTime'">
          {{record.startTime}} - {{record.endTime}}
        </template>

        <!-- 有图片预览时 注释解开并把下面的'picture'修改成自己的图片字段名即可 -->
        <!-- <template v-if="column.dataIndex === 'picture'">
            <FilePreview :fileList="text" type="picture" />
          </template> -->

        <!-- 使用字典时 注释解开并把下面的'dict'修改成自己的字典字段名即可 有多个字典字段就复制多份同理修改 不然不显示字典 -->
        <!-- 方便修改tag的颜色 orange green purple success processing error default warning -->
        <!-- <template v-if="column.dataIndex === 'dict'">
          <a-tag color="cyan">
            {{ text && text.length > 0 ? text.map((e) => e.valueName).join(',') : '暂无' }}
          </a-tag>
        </template> -->

        <template v-if="column.dataIndex === 'projectName'">
          <a @click="detail(record.projectId)">{{ record.projectName }}</a>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="showForm(record)" type="link">编辑</a-button>
            <a-button @click="showForm(record)" type="link">挂起</a-button>
            <a-button @click="showForm(record)" type="link">完成</a-button>
            <a-button @click="showForm(record)" type="link" dashed>关闭</a-button>
            <a-button @click="onDelete(record)" type="link" danger>删除</a-button>
          </div>
        </template>
      </template>
    </a-table>
    <!---------- 表格 end ----------->

    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="queryData"
        @showSizeChange="queryData"
        :show-total="(total) => `共${total}条`"
      />
    </div>

    <ProjectForm ref="formRef" @reloadList="queryData" />
  </a-card>
</template>
<script setup>
  import { reactive, ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { projectApi } from '/@/api/project/pm/pm-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import ProjectForm from './pm-form.vue';
  import {useRouter} from "vue-router";
  //import FilePreview from '/@/components/support/file-preview/index.vue'; // 图片预览组件

  // ---------------------------- 表格列 ----------------------------

  const columns = ref([
    {
      title: '项目名称',
      dataIndex: 'projectName',
      ellipsis: true,
    },
    {
      title: '项目时间',
      dataIndex: 'startTime',
      ellipsis: true,
    },
    {
      title: '状态',
      dataIndex: 'state',
      ellipsis: true,
    },
    {
      title: '完成时间',
      dataIndex: 'completeTime',
      ellipsis: true,
    },
    {
      title: '项目预估采购成本',
      dataIndex: 'cost',
      ellipsis: true,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 200,
    },
  ]);

  // ---------------------------- 查询数据表单和方法 ----------------------------

  const queryFormState = {
    projectName: undefined, //请输入项目名称
    startTime: undefined, //项目开始时间
    endTime: undefined, //项目结束时间
    state: undefined, //状态：0未开始、1进行中、2已挂起、3已完成、4已关闭
    pageNum: 1,
    pageSize: 10,
  };
  // 查询表单form
  const queryForm = reactive({ ...queryFormState });
  // 表格加载loading
  const tableLoading = ref(false);
  // 表格数据
  const tableData = ref([]);
  // 总数
  const total = ref(0);

  // 重置查询条件
  function resetQuery() {
    let pageSize = queryForm.pageSize;
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryData();
  }

  // 搜索
  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  // 查询数据
  async function queryData() {
    if (queryForm.state !== undefined && queryForm.state.length === 0) {
      queryForm.state = undefined;
    }

    tableLoading.value = true;
    try {
      let queryResult = await projectApi.queryPage(queryForm);
      tableData.value = queryResult.data.list;
      total.value = queryResult.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function onChangeStartTime(dates, dateStrings) {
    queryForm.startTimeBegin = dateStrings[0];
    queryForm.startTimeEnd = dateStrings[1];
  }

  function onChangeEndTime(dates, dateStrings) {
    queryForm.endTimeBegin = dateStrings[0];
    queryForm.endTimeEnd = dateStrings[1];
  }

  onMounted(queryData);

  // ---------------------------- 添加/修改 ----------------------------
  const formRef = ref();

  function showForm(data) {
    formRef.value.show(data);
  }

  let router = useRouter();
  function detail(projectId) {
    router.push({ path: '/project/pm/pm-detail', query: { projectId: projectId } });
  }

  // ---------------------------- 单个删除 ----------------------------
  //确认删除
  function onDelete(data) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除选吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        requestDelete(data);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  //请求删除
  async function requestDelete(data) {
    SmartLoading.show();
    try {
      let deleteForm = {
        goodsIdList: selectedRowKeyList.value,
      };
      await projectApi.delete(data.projectId);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ---------------------------- 批量删除 ----------------------------

  // 选择表格行
  const selectedRowKeyList = ref([]);

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  // 批量删除
  function confirmBatchDelete() {
    Modal.confirm({
      title: '提示',
      content: '确定要批量删除这些数据吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        requestBatchDelete();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  //请求批量删除
  async function requestBatchDelete() {
    try {
      SmartLoading.show();
      await projectApi.batchDelete(selectedRowKeyList.value);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
