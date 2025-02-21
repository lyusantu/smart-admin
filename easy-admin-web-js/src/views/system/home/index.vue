<!--
  * 首页
-->
<template>
  <!--  顶部用户信息-->
  <a-row>
    <HomeHeader />
  </a-row>
  <!--下方左右布局-->

    <a-col :span="24">
      <a-row :gutter="[10, 10]">
        <!--快捷入口-->
        <a-col :span="8">
          <HomeQuickEntry />
        </a-col>
        <!--公告信息-->
        <a-col :span="8">
          <HomeNotice title="公告" :noticeTypeId="1" />
        </a-col>
        <!--企业动态-->
        <a-col :span="8">
          <HomeNotice title="通知" :noticeTypeId="2" />
        </a-col>
      </a-row>
    </a-col>

    <!--左侧-->
    <a-col :span="24">
      <a-row :gutter="[10, 10]">
        <!--待办、已办-->
        <a-col :span="8">
          <ToBeDoneCard />
        </a-col>
        <a-col :span="8">
          <MyTask />
        </a-col>
        <a-col :span="8">
          <ChangelogCard />
        </a-col>
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
  import MyTask from './task-my.vue';
  import ChangelogCard from './components/changelog-card.vue';
  import HomeQuickEntry from './components/quick-entry/home-quick-entry.vue';
  import AdModal from './ad-modal.vue';
</script>
<style lang="less" scoped>
  @import './index.less';
</style>
