<!--
  * 首页
-->
<template>

  <a-col :span="24">
    <a-row :gutter="[10, 10]">
      <!-- 我的待办 -->
      <a-col :span="24">
        <HomeHeader />
      </a-col>
      <!--快捷入口-->
<!--      <a-col :span="8">
        <HomeQuickEntry />
      </a-col>-->
    </a-row>
  </a-col>

  <a-col :span="24">
    <a-row :gutter="[10, 10]">
      <!-- 我的待办项目 -->
      <a-col :span="16">
        <TaskMy :projectId="2" />
      </a-col>

      <!--待办、已办-->
      <a-col :span="8">
        <ToBeDoneCard />
      </a-col>

<!--
      &lt;!&ndash;公告信息&ndash;&gt;
      <a-col :span="6">
        <HomeNotice title="公告" :noticeTypeId="1" />
      </a-col>

      <a-col :span="6">
        <HomeNotice title="通知" :noticeTypeId="2" />
      </a-col>
-->

    </a-row>
  </a-col>

    <!-- 首页弹窗 -->
<!--    <AdModal/>-->

</template>
<script setup>
  import { computed } from 'vue';
  import HomeHeader from './home-header.vue';
  import HomeNotice from './home-notice.vue';
  import ToBeDoneCard from './components/to-be-done-card/home-to-be-done.vue';
  import ChangelogCard from './components/changelog-card.vue';
  import HomeQuickEntry from './components/quick-entry/home-quick-entry.vue';
  import AdModal from './ad-modal.vue';
  import TaskMy from "./home-mytask.vue";
</script>
<style lang="less" scoped>
  @import './index.less';
</style>
