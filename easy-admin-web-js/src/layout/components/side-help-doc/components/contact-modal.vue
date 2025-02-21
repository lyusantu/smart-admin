<template>
  <a-modal :open="visible" width="600px" title="联系客服人员" :closable="false" :maskClosable="true">
    <a-row><div style="margin-left: 180px;font-weight:bolder">客服电话：029-88888888</div> </a-row>
    <br />
    <div class="app-qr-box">
      <div class="app-qr">
        <img :src="weixin" />
        <span class="qr-desc strong">客服微信</span>
        <span class="qr-desc">xxxxxx</span>
      </div>
      <div class="app-qr">
        <img :src="douyin" />
        <span class="qr-desc strong">客服抖音</span>
        <span class="qr-desc">xxxxxx</span>
      </div>
      <div class="app-qr">
        <img :src="xiaohongshu" />
        <span class="qr-desc strong">客服小红书</span>
        <span class="qr-desc">xxxxxx</span>
      </div>
    </div>
    <template #footer>
      <a-button type="primary" @click="hide">知道了</a-button>
    </template>
  </a-modal>
</template>
<script setup>
  import { ref, reactive, nextTick, computed } from 'vue';
  import weixin from '/@/assets/images/logo/logo.png';
  import douyin from '/@/assets/images/logo/logo.png';
  import xiaohongshu from '/@/assets/images/logo/logo.png';

  defineExpose({
    show,
  });

  const visible = ref(false);
  function show() {
    visible.value = true;
  }
  function hide() {
    visible.value = false;
  }
</script>
<style lang="less" scoped>
  .app-qr-box {
    display: flex;
    height: 170px;
    align-items: center;
    justify-content: space-around;
    .app-qr {
      display: flex;
      align-items: center;
      width: 33%;
      justify-content: center;
      flex-direction: column;
      > img {
        width: 100%;
        max-width: 150px;
        height: 100%;
        max-height: 150px;
      }
      .strong {
        font-weight: 600;
      }
      .qr-desc {
        display: flex;
        align-items: center;
        font-size: 12px;
        text-align: center;
        overflow-x: hidden;
        > img {
          width: 15px;
          height: 18px;
          margin-right: 9px;
        }
      }
    }
  }

  .ant-carousel :deep(.slick-slide) {
    text-align: center;
    height: 120px;
    line-height: 120px;
    width: 120px;
    background: #364d79;
    overflow: hidden;
  }

  .ant-carousel :deep(.slick-slide h3) {
    color: #fff;
  }
</style>
