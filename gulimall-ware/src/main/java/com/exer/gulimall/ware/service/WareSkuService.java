package com.exer.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exer.common.utils.PageUtils;
import com.exer.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-22 20:46:19
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

