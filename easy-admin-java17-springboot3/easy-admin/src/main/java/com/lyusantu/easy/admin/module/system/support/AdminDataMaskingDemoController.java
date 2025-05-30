package com.lyusantu.easy.admin.module.system.support;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.lyusantu.easy.base.common.controller.SupportBaseController;
import com.lyusantu.easy.base.common.domain.ResponseDTO;
import com.lyusantu.easy.base.constant.SwaggerTagConst;
import com.lyusantu.easy.base.module.support.datamasking.DataMasking;
import com.lyusantu.easy.base.module.support.datamasking.DataMaskingTypeEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据脱敏demo
 */
@RequiredArgsConstructor
@RestController
@Tag(name = SwaggerTagConst.Support.DATA_MASKING)
public class AdminDataMaskingDemoController extends SupportBaseController {

    @Operation(summary = "数据脱敏demo")
    @GetMapping("/dataMasking/demo/query")
    public ResponseDTO<List<DataVO>> query() {
        List<DataVO> list = new ArrayList<>();
        for (int i = 0; i < RandomUtil.randomInt(10,16); i++) {
            DataVO data = new DataVO();
            data.setUserId(RandomUtil.randomLong(1328479238, 83274298347982L));
            data.setPhone("1" + RandomUtil.randomNumbers(10));
            data.setIdCard("410" + RandomUtil.randomNumbers(3) + RandomUtil.randomInt(1980, 2010) + RandomUtil.randomInt(10, 12) + RandomUtil.randomInt(10, 30) + RandomUtil.randomNumbers(4));
            data.setAddress(RandomUtil.randomBoolean() ? "河南省洛阳市洛龙区一零二四大街1024号" : "河南省郑州市高新区六边形大街六边形大楼");
            data.setPassword(RandomUtil.randomString(10));
            data.setEmail(RandomUtil.randomString(RandomUtil.randomInt(6, 10)) + "@" + RandomUtil.randomString(2) + ".com");
            data.setCarLicense("豫" + RandomStringUtils.randomAlphabetic(1).toUpperCase()+" " + RandomStringUtils.randomAlphanumeric(5).toUpperCase());
            data.setBankCard("6225" + RandomStringUtils.randomNumeric(14));
            data.setOther(RandomStringUtils.randomAlphanumeric(1, 12));
            list.add(data);
        }

        return ResponseDTO.ok(list);
    }


    @Data
    public static class DataVO {

        @DataMasking(DataMaskingTypeEnum.USER_ID)
        private Long userId;

        @DataMasking(DataMaskingTypeEnum.PHONE)
        private String phone;

        @DataMasking(DataMaskingTypeEnum.ID_CARD)
        private String idCard;

        @DataMasking(DataMaskingTypeEnum.ADDRESS)
        private String address;

        @DataMasking(DataMaskingTypeEnum.PASSWORD)
        private String password;

        @DataMasking(DataMaskingTypeEnum.EMAIL)
        private String email;

        @DataMasking(DataMaskingTypeEnum.CAR_LICENSE)
        private String carLicense;

        @DataMasking(DataMaskingTypeEnum.BANK_CARD)
        private String bankCard;

        @DataMasking
        private String other;

    }

}
