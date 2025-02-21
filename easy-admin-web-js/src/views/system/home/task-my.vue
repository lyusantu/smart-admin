<template>
  <default-home-card extra="更多" icon="SoundOutlined" title="我的项目" @extraClick="onMore">
    <a-spin :spinning="loading">
      <div class="content-wrapper">
        <a-empty v-if="$lodash.isEmpty(data)" />
        <ul v-else>
          <li>
            <a-tooltip placement="top">
              <template #title>
                <span>赛孚信息化管理平台</span>
              </template>
              <a class="content" @click="toDetail(1)">
                赛孚信息化管理平台
              </a>
            </a-tooltip>
            <span class="status">已完成</span>
          </li>

          <li class="unread">
            <a-tooltip placement="top">
              <template #title>
                <span>赛孚信息化管理平台</span>
              </template>
              <a class="content" @click="toDetail(1)">
                赛孚信息化管理平台12345678
              </a>
            </a-tooltip>
            <span class="status">已延期 523 天</span>
          </li>

          <li class="unread">
            <a-tooltip placement="top">
              <template #title>
                <span>赛孚信息化管理平台</span>
              </template>
              <a class="content" @click="toDetail(1)">
                赛孚信息化管理平台12345678
              </a>
            </a-tooltip>
            <span class="status">进行中，还有20天</span>
          </li>

        </ul>
      </div>
    </a-spin>
  </default-home-card>
</template>
<script setup>
  import { onMounted, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { noticeApi } from '/@/api/business/oa/notice-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';

  const props = defineProps({
    noticeTypeId: {
      type: Number,
      default: 1,
    },
  });

  const queryForm = {
    noticeTypeId: props.noticeTypeId,
    pageNum: 1,
    pageSize: 6,
    searchCount: false,
  };

  let data = ref([]);

  const loading = ref(false);
  // 查询列表
  async function queryNoticeList() {
    try {
      loading.value = true;
      const result = await noticeApi.queryEmployeeNotice(queryForm);
      data.value = result.data.list;
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      loading.value = false;
    }
  }

  onMounted(() => {
    queryNoticeList();
  });

  // 查看更多
  function onMore() {
    router.push({
      path: '/oa/notice/notice-employee-list',
    });
  }

  // 进入详情
  const router = useRouter();
  function toDetail(noticeId) {
    router.push({
      path: '/oa/notice/notice-employee-detail',
      query: { noticeId },
    });
  }
</script>
<style lang="less" scoped>
  @read-color: #666;
  .content-wrapper {
    height: 150px;
    overflow-y: hidden;
    overflow-x: hidden;
  }
  ul li {
    display: flex;
    justify-content: space-between;
    margin-bottom: 4px;

    .content {
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      word-break: break-all;
      margin-right: 5px;
      width: 250px;
    }

    .status {
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      word-break: break-all;
      margin-right: 5px;
    }

  }

  .read a {
    color: @read-color !important;
  }
</style>
