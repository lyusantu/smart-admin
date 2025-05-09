/*
 * 员工
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 22:08:45
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
export const PROJECT_STAGE_STATE = {
  NOT_STARTED: {
    value: 0,
    desc: '未开始',
  },
  IN_PROGRESS: {
    value: 1,
    desc: '进行中',
  },
  AHEAD_COMPLETED: {
    value: 2,
    desc: '提前完成',
  },
  COMPLETED: {
    value: 3,
    desc: '已完成',
  },
  DELAYED: {
    value: 4,
    desc: '已延期',
  },
  CLOSED: {
    value: 5,
    desc: '已关闭',
  },
}

export default {
  PROJECT_STAGE_STATE,
}
