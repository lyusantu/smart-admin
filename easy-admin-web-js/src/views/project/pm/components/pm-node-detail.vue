<template v-else>
  <a-row class="smart-table-btn-block">
    <div class="smart-table-operate-block">
      <a-button @click="addNode" type="primary">
        <template #icon>
          <PlusOutlined />
        </template>
        新建节点
      </a-button>

      <a-button @click="addStage" type="primary">
        <template #icon>
          <PlusOutlined />
        </template>
        新建阶段
      </a-button>
    </div>
  </a-row>

  <div class="borBoxOvflo" v-if="list.length >= 1">
    <div class="ovflo">
      <a-row :gutter="24" v-for="item in list" :key="item.projectNodeId" class="nodeBox">
        <a-col :span="4" @dblclick="handleDoubleClick(item)">
          <template v-if="!item.isEditing">
            <div class="container" :class="item.complete === 0 ? 'successDiv' : 'errorDiv'">
              <div class="rectangle">{{ item.projectNodeName }}</div>
              <div class="triangle" :class="item.complete === 0 ? 'successDiv' : 'errorDiv'"></div>
            </div>
          </template>
          <template v-else>
            <a-input
              v-model:value="item.projectNodeName"
              @keyup.enter="saveText(item)"
              @blur="handleBlur"
              ref="textInput"
              show-count
              :maxlength="10"
            />
          </template>
        </a-col>
        <a-col :span="20">
          <!--   :percent=0 percent指的是process占比的百分比, label-placement="vertical"文字展示在下方 -->
          <a-steps :current="-1" size="small" style="padding-top: 10px" label-placement="vertical">
            <a-step
              v-for="(stage, index) in item.listProjectStage"
              :key="stage.stageId"
              :status="getStatus(stage, index)"
              :title="stage.stageName"
              @click="itemNodeClick(stage, index)"
              style="flex: 0"
            />
          </a-steps>
        </a-col>
      </a-row>
    </div>
  </div>
  <a-empty v-if="list.length <= 0"></a-empty>
  <NodeAddForm ref="addNodeForm" @reloadList="ajaxQuery" />
  <StageAddForm ref="addStageForm" @reloadList="ajaxQuery" />
</template>

<script setup>
  import { ref, watch } from 'vue';
  import { projectApi } from '/@/api/project/pm/pm-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import NodeAddForm from './pm-node-form.vue';
  import StageAddForm from './pm-stage-form.vue';

  const list = ref([]);
  const props = defineProps({
    projectId: {
      type: Number,
      default: null,
    },
  });

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

  async function ajaxQuery() {
    try {
      SmartLoading.show();
      let result = await projectApi.detail(props.projectId);
      list.value = result.data.listProjectNode;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  const addNodeForm = ref();

  function addNode() {
    let data = {
      projectId: props.projectId,
    };
    addNodeForm.value.show(data);
  }

  const addStageForm = ref();

  function addStage() {
    let data = {
      projectId: props.projectId,
    };
    addStageForm.value.show(data);
  }

  const handleDoubleClick = (item) => {
    let data = {
      projectId: props.projectId,
      projectNodeId: item.projectNodeId,
      nodeName: item.projectNodeName,
    };
    addNodeForm.value.show(data);
  };

  const saveText = (item) => {
    console.log('保存文本', item);
    item.isEditing = false;
  };

  const handleBlur = () => {
    console.log('失去焦点');
  };

  const getStatus = (item, stepIndex) => {
    if (item.state === 0 || item.status === 5) {
      return 'wait';
    } else if (item.state === 1) {
      return 'process';
    } else if (item.state === 2 || item.status === 3) {
      return 'finish';
    } else if (item.state === 4) {
      return 'error';
    }
  };

  const itemNodeClick = (item, stepIndex) => {
    let data = {
      stageId: item.stageId,
      projectId: props.projectId,
    };
    addStageForm.value.show(data);
  };
</script>

<style scoped lang="scss">
  :deep(.n-tabs-tab__label) {
    font-size: 16px;
  }

  .n-steps {
    padding: 3px;
    user-select: none;
  }

  .leftRig {
    display: flex;
    align-items: center;
    font-size: 15px;

    .wid-mu {
      min-width: 80px;
    }

    .widinput {
      width: 300px;
    }
  }

  .borBoxOvflo {
    border: 1px dashed #ccc;
  }

  .ovflo {
    margin: 5px;
    overflow: auto;
    max-height: 500px;
    padding: 5px 20px 5px 20px;
    border-radius: 8px;
  }

  .ovflo::-webkit-scrollbar {
    width: 8px;
    height: 8px;
  }

  .ovflo::-webkit-scrollbar-track {
    background-color: #f1f1f1;
    border-radius: 4px;
  }

  .ovflo::-webkit-scrollbar-thumb {
    border: 6px solid rgba(0, 0, 0, 0);
    box-shadow: 8px 0 0 #a5adb7 inset;
    background-color: #c1c1c1;
    border-radius: 4px;
  }

  .step-wrap {
    height: 50px !important;
  }

  .pageContBox {
    display: flex;
    justify-content: end;
    margin-top: 20px;
  }

  .modalPsw {
    .titleH2 {
      text-align: center;
      font-weight: 500;
      font-size: 18px;
      padding-bottom: 20px;
      border-bottom: 1px solid #000;
      margin-bottom: 20px;
    }
  }

  .flexBtn {
    display: flex;
    justify-content: center;

    .n-button {
      width: 100px;
      margin: 0 20px;
    }
  }

  :deep(.el-steps) {
    .el-step__icon-inner {
      display: none;
    }

    .is-status {
      display: block;
    }

    .el-step__head.is-finish {
      .el-step__icon {
        background-color: #409eff;
      }
    }

    // 设置进度条样式
    .el-step {
      flex-basis: 0% !important;

      .el-step__main {
        min-width: 180px;
        word-break: keep-all;
        white-space: nowrap;
      }

      .el-step__head {
        min-width: 180px;
        //   max-width: 180px;
      }

      .el-step__line-inner {
        display: none;
      }

      .el-step__line {
        // background-color: red;
        // left: 0;
        // right: -100%;
        margin-top: -2px;
        margin-left: 10px;
        border-radius: 10px;
        height: 8px;
        background: linear-gradient(45deg, #00b8f5 25%, #26c2f6 0, #26c2f6 50%, #00b8f5 0, #00b8f5 75%, #26c2f6 0);
        background-size: 10px 10px;
      }
    }
  }

  .el-steps::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  .el-steps::-webkit-scrollbar-track {
    background-color: #f5f5f5;
    border-radius: 4px;
  }

  .el-steps::-webkit-scrollbar-thumb {
    background-color: #c1c1c1;
    border-radius: 4px;
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  }

  .nodeBox {
    height: 80px;
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }

  .rectangle {
    color: white;
    width: 150px;
    height: 100%;
    background-color: skyblue;
    line-height: 50px;
    text-align: center;
    //文本超出部分以...形式展示
    text-overflow: -o-ellipsis-lastline;
    //整体超出部分隐藏
    overflow: hidden;
    //文本超出部分以...形式展示，同第一行样式代码
    text-overflow: ellipsis;
    //display 块级元素展示
    display: -webkit-box;
    //设置文本行数为2行
    -webkit-line-clamp: 1;
    //设置文本行数为2行
    line-clamp: 1;
    //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）
    -webkit-box-orient: vertical;
    float: left;
  }

  .triangle {
    width: 0;
    height: 0;
    border-top: 25px solid transparent;
    border-bottom: 25px solid transparent;
    border-left: 25px solid skyblue;
    float: left;
  }

  .successDiv {
    .rectangle {
      background-color: limegreen;
    }

    border-left-color: limegreen;
  }

  .errorDiv {
    .rectangle {
      background-color: #1677ff;
    }

    border-left-color: #1677ff;
  }

  .draggable-ul {
    width: 100%;
    overflow: hidden;
    margin-top: -16px;

    .draggable-li {
      text-align: center;
      width: 100%;
      padding: 16px 10px;
      color: #333;
      border-bottom: 1px solid #efeff5;
    }
  }

  .cursor-move {
    cursor: move;
  }
</style>
