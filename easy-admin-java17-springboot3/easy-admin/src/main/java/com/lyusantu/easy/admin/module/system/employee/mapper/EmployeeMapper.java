package com.lyusantu.easy.admin.module.system.employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.admin.module.system.employee.domain.entity.EmployeeEntity;
import com.lyusantu.easy.admin.module.system.employee.domain.form.EmployeeQueryForm;
import com.lyusantu.easy.admin.module.system.employee.domain.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 员工 Mapper
 */
@Mapper
@Component
public interface EmployeeMapper extends BaseMapper<EmployeeEntity> {
    /**
     * 查询员工列表
     */
    List<EmployeeVO> queryEmployee(Page page, @Param("queryForm") EmployeeQueryForm queryForm, @Param("departmentIdList") List<Long> departmentIdList);

    /**
     * 查询员工
     */
    List<EmployeeVO> selectEmployeeByDisabledAndDeleted(@Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 更新禁用状态
     */
    void updateDisableFlag(@Param("employeeId") Long employeeId, @Param("disabledFlag") Boolean disabledFlag);


    /**
     * 通过登录名查询
     */
    EmployeeEntity getByLoginName(@Param("loginName") String loginName, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 通过姓名查询
     */
    EmployeeEntity getByActualName(@Param("actualName") String actualName, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 通过手机号查询
     */
    EmployeeEntity getByPhone(@Param("phone") String phone, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 通过邮箱账号查询
     */
    EmployeeEntity getByEmail(@Param("email") String email, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取所有员工
     */
    List<EmployeeVO> listAll();

    /**
     * 获取某个部门员工数
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取一批员工
     */
    List<EmployeeVO> getEmployeeByIds(@Param("employeeIds") Collection<Long> employeeIds);

    /**
     * 查询单个员工信息
     */
    EmployeeVO getEmployeeById(@Param("employeeId") Long employeeId);


    /**
     * 获取某个部门的员工
     */
    List<EmployeeEntity> selectByDepartmentId(@Param("departmentId") Long departmentId, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 查询某些部门下用户名是xxx的员工
     */
    List<EmployeeEntity> selectByActualName(@Param("departmentIdList") List<Long> departmentIdList, @Param("actualName") String actualName, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 获取某批部门的员工Id
     */
    List<Long> getEmployeeIdByDepartmentIdList(@Param("departmentIds") List<Long> departmentIds, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取所有
     */
    List<Long> getEmployeeId(@Param("leaveFlag") Boolean leaveFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 获取某个部门的员工Id
     */
    List<Long> getEmployeeIdByDepartmentId(@Param("departmentId") Long departmentId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 员工重置密码
     */
    Integer updatePassword(@Param("employeeId") Long employeeId, @Param("password") String password);

}
