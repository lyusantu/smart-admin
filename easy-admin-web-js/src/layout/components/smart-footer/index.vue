<!--
  * 底部：版权、公司等信息
-->
<template>
  <div class="version">
    <a target="_blank" class="smart-copyright" href="/"> ©2020-{{ currentYear }} EasyAdmin </a>
  </div>
</template>
<script setup>
  import dayjs from 'dayjs';

  const currentYear = dayjs().year();
</script>

<style lang="less" scoped>
  .version {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.45);
    a {
      color: rgba(0, 0, 0, 0.45);
    }
    a:hover {
      color: @primary-color;
    }
  }
</style>
