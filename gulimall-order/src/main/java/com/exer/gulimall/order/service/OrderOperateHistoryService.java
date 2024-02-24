package com.exer.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exer.common.utils.PageUtils;
import com.exer.gulimall.order.entity.OrderOperateHistoryEntity;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-22 20:43:30
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

