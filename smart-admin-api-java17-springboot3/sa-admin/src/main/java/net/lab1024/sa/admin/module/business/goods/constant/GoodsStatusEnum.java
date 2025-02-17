package net.lab1024.sa.admin.module.business.goods.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 商品状态
 */
@AllArgsConstructor
@Getter
public enum GoodsStatusEnum implements BaseEnum {

    APPOINTMENT(1, "预约中"),
    SELL(2, "售卖中"),
    SELL_OUT(3, "售罄");

    private final Integer value;

    private final String desc;
}
