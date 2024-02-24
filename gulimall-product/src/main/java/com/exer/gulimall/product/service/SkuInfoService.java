package com.exer.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exer.common.utils.PageUtils;
import com.exer.gulimall.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-20 22:05:43
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

