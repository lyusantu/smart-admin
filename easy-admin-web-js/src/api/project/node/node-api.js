/**
 * 节点模板表 api 封装
 *
 * @Author:    lyusantu
 * @Date:      2025-02-19 14:33:24
 * @Copyright  lyusantu
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const nodeApi = {
  /**
   * 分页查询  @author  lyusantu
   */
  queryPage: (param) => {
    return postRequest('/pm/node/queryPage', param);
  },

  /**
   * 增加  @author  lyusantu
   */
  add: (param) => {
    return postRequest('/pm/node/add', param);
  },

  /**
   * 修改  @author  lyusantu
   */
  update: (param) => {
    return postRequest('/pm/node/update', param);
  },

  /**
   * 删除  @author  lyusantu
   */
  delete: (id) => {
    return getRequest(`/pm/node/delete/${id}`);
  },

  /**
   * 批量删除  @author  lyusantu
   */
  batchDelete: (idList) => {
    return postRequest('/pm/node/batchDelete', idList);
  },

  listNode: (id) => {
    return getRequest(`/pm/node/listNode/${id}`);
  },

};
