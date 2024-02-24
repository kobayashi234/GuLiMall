package com.exer.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exer.common.utils.PageUtils;
import com.exer.gulimall.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-22 20:17:19
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

