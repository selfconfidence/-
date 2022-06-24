package com.manyun.business.service;

import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.OrderVo;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IOrderService extends IService<Order> {
    TableDataInfo<OrderVo> pageQueryList(OrderQuery orderQuery, String userId) ;

    /**
     * 检查未支付订单
     * @param userId
     * @return
     */
    List<Order> checkUnpaidOrder (String userId);

    /**
     * 创建订单
     * @param orderCreateDto
     * @return
     */
    String createOrder(OrderCreateDto orderCreateDto);

    void notifyPaySuccess(String outHost);
}
