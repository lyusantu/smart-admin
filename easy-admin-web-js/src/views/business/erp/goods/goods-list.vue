<!--
  * 商品列表
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row" v-privilege="'goods:query'">
      <a-form-item label="商品分类" class="smart-query-form-item">
        <category-tree
            width="150px"
            v-model:value="queryForm.categoryId"
            placeholder="请选择商品分类"
            :categoryType="CATEGORY_TYPE_ENUM.GOODS.value"
        />
      </a-form-item>

      <a-form-item label="商品名称" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.searchWord" placeholder="商品名称" />
      </a-form-item>

      <a-form-item label="产地" name="place" class="smart-query-form-item">
        <DictSelect key-code="GOODS_PLACE" v-model:value="queryForm.place" width="120px" />
      </a-form-item>

      <a-form-item label="商品状态" name="goodsStatus" class="smart-query-form-item">
        <SmartEnumSelect enum-name="GOODS_STATUS_ENUM" v-model:value="queryForm.goodsStatus" width="160px" />
      </a-form-item>

      <a-form-item label="快速筛选" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.shelvesFlag" @change="onSearch">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="'true'">上架</a-radio-button>
          <a-radio-button :value="'false'">下架</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item">
        <a-button-group>
          <a-button type="primary" @click="onSearch" v-privilege="'goods:query'">
            <template #icon>
              <ReloadOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery" v-privilege="'goods:query'">
            <template #icon>
              <SearchOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->

  <a-card size="small" :bordered="false" :hoverable="true">
    <!---------- 表格操作行 begin ----------->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addGoods" type="primary" v-privilege="'goods:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>

        <a-button @click="confirmBatchDelete" danger :disabled="selectedRowKeyList.length === 0" v-privilege="'goods:batchDelete'">
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>

        <a-button @click="showImportModal" type="primary" v-privilege="'goods:importGoods'">
          <template #icon>
            <ImportOutlined />
          </template>
          导入
        </a-button>

        <a-button @click="onExportGoods" type="primary" v-privilege="'goods:exportGoods'">
          <template #icon>
            <ExportOutlined />
          </template>
          导出
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.ERP.GOODS" :refresh="queryData" />
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->
    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="goodsId"
        :scroll="{ x: 1000 }"
        bordered
        :pagination="false"
        :showSorterTooltip="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        @change="onChange"
        @resizeColumn="handleResizeColumn"
    >
      <!-- main.js中有全局表格高度配置，也可单独配置 -->
      <!-- :scroll="{ y: 300 }" -->
      <template #headerCell="{ column }">
        <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column" @change="queryData" />
      </template>
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'goodsName'">
          {{ text }}
        </template>
        <template v-if="column.dataIndex === 'place'">
          <DictPreview :options="descList.GOODS_PLACE" :value="text" />
        </template>
        <template v-if="column.dataIndex === 'remark'">
          <span>{{ text ? text : '' }}</span>
        </template>
        <template v-if="column.dataIndex === 'goodsStatus'">
          <span>{{ $smartEnumPlugin.getDescByValue('GOODS_STATUS_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'shelvesFlag'">
          <span>{{ text ? '上架' : '下架' }}</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addGoods(record)" type="link" v-privilege="'goods:update'">编辑</a-button>
            <a-button @click="deleteGoods(record)" danger type="link" v-privilege="'goods:delete'">删除</a-button>
          </div>
        </template>
      </template>
    </a-table>

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

    <GoodsFormModal ref="formModal" @reloadList="queryData" />

    <a-modal v-model:open="importModalShowFlag" title="导入" @onCancel="hideImportModal" @ok="hideImportModal">
      <div style="text-align: center; width: 400px; margin: 0 auto">
        <a-button @click="downloadExcel"> <download-outlined />第一步：下载模板</a-button>
        <br />
        <br />
        <a-upload
            v-model:fileList="fileList"
            name="file"
            :multiple="false"
            action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
            accept=".xls,.xlsx"
            :before-upload="beforeUpload"
            @remove="handleRemove"
        >
          <a-button>
            <upload-outlined />
            第二步：选择文件
          </a-button>
        </a-upload>

        <br />
        <a-button @click="onImportGoods">
          <ImportOutlined />
          第三步：开始导入
        </a-button>
      </div>
    </a-modal>
  </a-card>
</template>
<script setup>
import GoodsFormModal from './components/goods-form-modal.vue';
import { onMounted, reactive, ref } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/framework/smart-loading';
import { goodsApi } from '/@/api/business/goods/goods-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import CategoryTree from '/@/components/business/category-tree-select/index.vue';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/erp/category-const';
import { smartSentry } from '/@/lib/smart-sentry';
import TableOperator from '/@/components/support/table-operator/index.vue';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
import { GOODS_STATUS_ENUM } from '/@/constants/business/erp/goods-const';
import DictSelect from '/@/components/support/dict-select/index.vue';
import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
import _ from 'lodash';
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue';
import DictPreview from '/@/components/dict-preview/index.vue';
import { useDict } from '/@/utils/dict';

const descList = useDict('GOODS_PLACE');
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '商品分类',
    dataIndex: 'categoryName',
    resizable: true,
    filterOptions: {
      type: 'category-tree',
      key: 'categoryId',
      categoryType: CATEGORY_TYPE_ENUM.GOODS.value,
    },
    width: 150,
  },
  {
    title: '商品名称',
    dataIndex: 'goodsName',
    resizable: true,
    filterOptions: {
      type: 'input',
      key: 'searchWord',
    },
    width: 150,
  },
  {
    title: '商品状态',
    dataIndex: 'goodsStatus',
    resizable: true,
    sorter: true,
    filterOptions: {
      type: 'enum-select',
      enumName: 'GOODS_STATUS_ENUM',
    },
    width: 150,
  },
  {
    title: '产地',
    dataIndex: 'place',
    resizable: true,
    filterOptions: {
      type: 'dict-select',
      keyCode: 'GOODS_PLACE',
    },
    width: 150,
  },
  {
    title: '价格',
    dataIndex: 'price',
    resizable: true,
    sorter: true,
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'shelvesFlag',
    resizable: true,
    sorter: true,
    width: 80,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    ellipsis: true,
    resizable: true,
    width: 150,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    resizable: true,
    width: 150,
  },
  {
    title: '操作',
    dataIndex: 'action',
    resizable: true,
    fixed: 'right',
    width: 100,
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  categoryId: undefined,
  searchWord: '',
  goodsStatus: undefined,
  place: undefined,
  shelvesFlag: undefined,
  goodsType: undefined,
  pageNum: 1,
  pageSize: 10,
  sortItemList: [],
};
// 查询表单form
const queryForm = reactive(_.cloneDeep(queryFormState));
// 表格加载loading
const tableLoading = ref(false);
// 表格数据
const tableData = ref([]);
// 总数
const total = ref(0);
function handleResizeColumn(w, col) {
  columns.value.forEach((item) => {
    if (item.dataIndex === col.dataIndex) {
      item.width = Math.floor(w);
      // 拖动宽度标识
      item.dragAndDropFlag = true;
    }
  });
}
// 重置查询条件
function resetQuery() {
  let pageSize = queryForm.pageSize;
  Object.assign(queryForm, _.cloneDeep(queryFormState));
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
  tableLoading.value = true;
  try {
    let queryResult = await goodsApi.queryGoodsList(queryForm);

    tableData.value = queryResult.data.list;
    total.value = queryResult.data.total;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    tableLoading.value = false;
  }
}

onMounted(queryData);

// ---------------------------- 添加/修改 ----------------------------
const formModal = ref();

function addGoods(goodsData) {
  formModal.value.showDrawer(goodsData);
}
// ---------------------------- 单个删除 ----------------------------

function deleteGoods(goodsData) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除【' + goodsData.goodsName + '】吗?',
    okText: '删除',
    okType: 'danger',
    onOk() {
      singleDelete(goodsData);
    },
    cancelText: '取消',
    onCancel() {},
  });
}

async function singleDelete(goodsData) {
  try {
    SmartLoading.show();
    await goodsApi.deleteGoods(goodsData.goodsId);
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
    content: '确定要删除选中商品吗?',
    okText: '删除',
    okType: 'danger',
    onOk() {
      batchDelete();
    },
    cancelText: '取消',
    onCancel() {},
  });
}

async function batchDelete() {
  try {
    SmartLoading.show();
    await goodsApi.batchDelete(selectedRowKeyList.value);
    message.success('删除成功');
    queryData();
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    SmartLoading.hide();
  }
}

// ------------------------------- 导出和导入 ---------------------------------
// 导入弹窗
const importModalShowFlag = ref(false);

const fileList = ref([]);
// 显示导入
function showImportModal() {
  fileList.value = [];
  importModalShowFlag.value = true;
}

// 关闭 导入
function hideImportModal() {
  importModalShowFlag.value = false;
}

function handleChange() {}

function handleDrop() {}

function handleRemove(file) {
  const index = fileList.value.indexOf(file);
  const newFileList = fileList.value.slice();
  newFileList.splice(index, 1);
  fileList.value = newFileList;
}
function beforeUpload(file) {
  fileList.value = [...(fileList.value || []), file];
  return false;
}

function downloadExcel() {
  window.open('https://smartadmin.vip/cdn/%E5%95%86%E5%93%81%E6%A8%A1%E6%9D%BF.xls');
}

async function onImportGoods() {
  const formData = new FormData();
  fileList.value.forEach((file) => {
    formData.append('file', file.originFileObj);
  });

  SmartLoading.show();
  try {
    let res = await goodsApi.importGoods(formData);
    message.success(res.msg);
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    SmartLoading.hide();
  }
}

async function onExportGoods() {
  await goodsApi.exportGoods();
}

function onChange(pagination, filters, sorter, { action }) {
  if (action === 'sort') {
    const { order, field } = sorter;
    let column = camelToUnderscore(field);
    let findIndex = queryForm.sortItemList.findIndex((e) => e.column === column);
    if (findIndex !== -1) {
      queryForm.sortItemList.splice(findIndex, 1);
    }
    if (order) {
      let isAsc = order !== 'ascend';
      queryForm.sortItemList.push({
        column,
        isAsc,
      });
    }
    queryData();
  }
}

function camelToUnderscore(str) {
  return str.replace(/([A-Z])/g, '_$1').toLowerCase();
}
</script>
