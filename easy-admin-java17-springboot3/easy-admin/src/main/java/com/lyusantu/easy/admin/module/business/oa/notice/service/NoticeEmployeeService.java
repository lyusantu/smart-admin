package com.lyusantu.easy.admin.module.business.oa.notice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lyusantu.easy.admin.module.business.oa.notice.domain.vo.*;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.admin.module.business.oa.notice.constant.NoticeVisibleRangeDataTypeEnum;
import com.lyusantu.easy.admin.module.business.oa.notice.mapper.NoticeMapper;
import com.lyusantu.easy.admin.module.business.oa.notice.domain.form.NoticeEmployeeQueryForm;
import com.lyusantu.easy.admin.module.business.oa.notice.domain.form.NoticeViewRecordQueryForm;
import com.lyusantu.easy.admin.module.system.department.service.DepartmentService;
import com.lyusantu.easy.admin.module.system.employee.domain.entity.EmployeeEntity;
import com.lyusantu.easy.admin.module.system.employee.service.EmployeeService;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.util.BeanUtil;
import com.lyusantu.easy.base.common.util.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 员工查看 通知。公告
 */
@RequiredArgsConstructor
@Service
public class NoticeEmployeeService {

    private final NoticeMapper noticeDao;

    private final NoticeService noticeService;

    private final EmployeeService employeeService;

    private final DepartmentService departmentService;

    /**
     * 查询我的 通知、公告清单
     */
    public ResponseDTO<PageResult<NoticeEmployeeVO>> queryList(Long requestEmployeeId, NoticeEmployeeQueryForm noticeEmployeeQueryForm) {
        Page<?> page = PageUtil.convert2PageQuery(noticeEmployeeQueryForm);

        List<Long> employeeDepartmentIdList = Lists.newArrayList();
        EmployeeEntity employeeEntity = employeeService.getById(requestEmployeeId);
        // 如果不是管理员 则获取请求人的 部门及其子部门
        if (!employeeEntity.getAdministratorFlag() && employeeEntity.getDepartmentId() != null) {
            employeeDepartmentIdList = departmentService.selfAndChildrenIdList(employeeEntity.getDepartmentId());
        }

        List<NoticeEmployeeVO> noticeList = null;
        //只查询未读的
        if (noticeEmployeeQueryForm.getNotViewFlag() != null && noticeEmployeeQueryForm.getNotViewFlag()) {
            noticeList = noticeDao.queryEmployeeNotViewNotice(page,
                    requestEmployeeId,
                    noticeEmployeeQueryForm,
                    employeeDepartmentIdList,
                    false,
                    employeeEntity.getAdministratorFlag(),
                    NoticeVisibleRangeDataTypeEnum.DEPARTMENT.getValue(),
                    NoticeVisibleRangeDataTypeEnum.EMPLOYEE.getValue());
        } else {
            // 查询全部
            noticeList = noticeDao.queryEmployeeNotice(page,
                    requestEmployeeId,
                    noticeEmployeeQueryForm,
                    employeeDepartmentIdList,
                    false,
                    employeeEntity.getAdministratorFlag(),
                    NoticeVisibleRangeDataTypeEnum.DEPARTMENT.getValue(),
                    NoticeVisibleRangeDataTypeEnum.EMPLOYEE.getValue());
        }
        // 设置发布日期
        noticeList.forEach(notice -> notice.setPublishDate(notice.getPublishTime().toLocalDate()));

        return ResponseDTO.ok(PageUtil.convert2PageResult(page, noticeList));
    }


    /**
     * 查询我的 待查看的 通知、公告清单
     */
    public ResponseDTO<NoticeDetailVO> view(Long requestEmployeeId, Long noticeId, String ip, String userAgent) {
        NoticeUpdateFormVO updateFormVO = noticeService.getUpdateFormVO(noticeId);
        if (updateFormVO == null || Boolean.TRUE.equals(updateFormVO.getDeletedFlag())) {
            return ResponseDTO.userErrorParam("通知公告不存在");
        }

        EmployeeEntity employeeEntity = employeeService.getById(requestEmployeeId);
        if (!updateFormVO.getAllVisibleFlag() && !checkVisibleRange(updateFormVO.getVisibleRangeList(), requestEmployeeId, employeeEntity.getDepartmentId())) {
            return ResponseDTO.userErrorParam("对不起，您没有权限查看内容");
        }

        NoticeDetailVO noticeDetailVO = BeanUtil.copy(updateFormVO, NoticeDetailVO.class);
        long viewCount = noticeDao.viewRecordCount(noticeId, requestEmployeeId);
        if (viewCount == 0) {
            noticeDao.insertViewRecord(noticeId, requestEmployeeId, ip, userAgent, 1);
            // 该员工对于这个通知是第一次查看 页面浏览量+1 用户浏览量+1
            noticeDao.updateViewCount(noticeId, 1, 1);
            noticeDetailVO.setPageViewCount(noticeDetailVO.getPageViewCount() + 1);
            noticeDetailVO.setUserViewCount(noticeDetailVO.getUserViewCount() + 1);
        } else {
            noticeDao.updateViewRecord(noticeId, requestEmployeeId, ip, userAgent);
            // 该员工对于这个通知不是第一次查看 页面浏览量+1 用户浏览量+0
            noticeDao.updateViewCount(noticeId, 1, 0);
            noticeDetailVO.setPageViewCount(noticeDetailVO.getPageViewCount() + 1);
        }

        return ResponseDTO.ok(noticeDetailVO);
    }

    /**
     * 校验是否有查看权限的范围
     */
    public boolean checkVisibleRange(List<NoticeVisibleRangeVO> visibleRangeList, Long employeeId, Long departmentId) {
        // 员工范围
        boolean anyMatch = visibleRangeList.stream().anyMatch(e -> NoticeVisibleRangeDataTypeEnum.EMPLOYEE.equalsValue(e.getDataType()) && Objects.equals(e.getDataId(), employeeId));
        if (anyMatch) {
            return true;
        }

        //部门范围
        List<Long> visibleDepartmentIdList = visibleRangeList.stream().filter(e -> NoticeVisibleRangeDataTypeEnum.DEPARTMENT.equalsValue(e.getDataType()))
                .map(NoticeVisibleRangeVO::getDataId).collect(Collectors.toList());

        for (Long visibleDepartmentId : visibleDepartmentIdList) {
            List<Long> departmentIdList = departmentService.selfAndChildrenIdList(visibleDepartmentId);
            if (departmentIdList.contains(departmentId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 分页查询  查看记录
     */
    public PageResult<NoticeViewRecordVO> queryViewRecord(NoticeViewRecordQueryForm noticeViewRecordQueryForm) {
        Page<?> page = PageUtil.convert2PageQuery(noticeViewRecordQueryForm);
        List<NoticeViewRecordVO> noticeViewRecordList = noticeDao.queryNoticeViewRecordList(page, noticeViewRecordQueryForm);
        return PageUtil.convert2PageResult(page, noticeViewRecordList);
    }
}
