package com.lyusantu.easy.base.module.support.securityprotect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.securityprotect.domain.LoginFailEntity;
import com.lyusantu.easy.base.module.support.securityprotect.domain.LoginFailVO;
import com.lyusantu.easy.base.module.support.securityprotect.domain.LoginFailQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录失败
 */
@Mapper
@Component
public interface LoginFailMapper extends BaseMapper<LoginFailEntity> {

    /**
     * 根据用户id和类型查询
     *
     * @param userId
     * @param userType
     * @return
     */
    LoginFailEntity selectByUserIdAndUserType(@Param("userId") Long userId, @Param("userType") Integer userType);

    /**
     * 根据用户id和类型查询 进行删除
     *
     * @param userId
     * @param userType
     * @return
     */
    void deleteByUserIdAndUserType(@Param("userId") Long userId, @Param("userType") Integer userType);

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<LoginFailVO> queryPage(Page page, @Param("queryForm") LoginFailQueryForm queryForm);
}
