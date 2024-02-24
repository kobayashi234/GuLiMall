package com.exer.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exer.gulimall.ware.entity.WareSkuEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.ware.dao.PurchaseDetailDao;
import com.exer.gulimall.ware.entity.PurchaseDetailEntity;
import com.exer.gulimall.ware.service.PurchaseDetailService;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new LambdaQueryWrapper<>();
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");

        if (StringUtils.hasText(status)) {
            wrapper.eq(PurchaseDetailEntity::getStatus, status);
        }
        if (StringUtils.hasText(wareId) && "0".equalsIgnoreCase(wareId)) {
            wrapper.eq(PurchaseDetailEntity::getWareId, wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        return this.list(new LambdaQueryWrapper<PurchaseDetailEntity>()
                .eq(PurchaseDetailEntity::getPurchaseId, id));

    }

}