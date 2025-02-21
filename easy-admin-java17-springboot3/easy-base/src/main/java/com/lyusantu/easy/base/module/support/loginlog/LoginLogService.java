package com.lyusantu.easy.base.module.support.loginlog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyusantu.easy.base.module.support.loginlog.domain.LoginLogQueryForm;
import com.lyusantu.easy.base.module.support.loginlog.domain.LoginLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lyusantu.easy.base.common.domain.page.PageResult;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.common.enumeration.UserTypeEnum;
import com.lyusantu.easy.base.common.util.PageUtil;
import com.lyusantu.easy.base.module.support.loginlog.domain.LoginLogEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录日志 Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LoginLogService {

    private final LoginLogMapper loginLogMapper;

    /**
     * 分页查询
     */
    public ResponseDTO<PageResult<LoginLogVO>> queryByPage(LoginLogQueryForm queryForm) {
        Page page = PageUtil.convert2PageQuery(queryForm);
        List<LoginLogVO> logList = loginLogMapper.queryByPage(page, queryForm);
        PageResult<LoginLogVO> pageResult = PageUtil.convert2PageResult(page, logList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 添加
     */
    public void log(LoginLogEntity loginLogEntity) {
        try {
            loginLogMapper.insert(loginLogEntity);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 查询上一个登录记录
     */
    public LoginLogVO queryLastByUserId(Long userId, UserTypeEnum userTypeEnum, LoginLogResultEnum loginLogResultEnum) {
        return loginLogMapper.queryLastByUserId(userId, userTypeEnum.getValue(), loginLogResultEnum.getValue());
    }

}
