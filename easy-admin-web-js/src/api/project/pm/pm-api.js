/**
 * 项目信息表 api 封装
 *
 * @Author:    lyusantu
 * @Date:      2025-02-20 10:29:51
 * @Copyright  lyusantu
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const projectApi = {

  listTemplate() {
    return postRequest('/pm/node/listNodeTemplate');
  },

  /**
   * 详情
   */
  detail: (projectId) => {
    return getRequest(`/pm/project/detail/${projectId}`);
  },

  addProjectNode: (param) => {
    return postRequest("/pm/project/node/add", param);
  },

  updateProjectNode: (param) => {
    return postRequest("/pm/project/node/update", param);
  },

  listProjectNode: (projectId) => {
    return getRequest(`/pm/project/node/list/${projectId}`);
  },

  /**
   * 分页查询
   */
  queryPage: (param) => {
    return postRequest('/pm/project/queryPage', param);
  },

  /**
   * 增加
   */
  add: (param) => {
    return postRequest('/pm/project/add', param);
  },

  /**
   * 修改
   */
  update: (param) => {
    return postRequest('/pm/project/update', param);
  },

  /**
   * 删除
   */
  delete: (id) => {
    return getRequest(`/pm/project/delete/${id}`);
  },

  /**
   * 批量删除
   */
  batchDelete: (idList) => {
    return postRequest('/pm/project/batchDelete', idList);
  },
};
