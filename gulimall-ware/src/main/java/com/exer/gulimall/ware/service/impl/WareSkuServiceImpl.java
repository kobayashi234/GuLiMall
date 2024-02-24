package com.exer.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exer.common.utils.R;
import com.exer.gulimall.ware.feign.ProductFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.ware.dao.WareSkuDao;
import com.exer.gulimall.ware.entity.WareSkuEntity;
import com.exer.gulimall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        String wareId = (String) params.get("wareId");
        String skuId = (String) params.get("skuId");

        if (StringUtils.hasText(wareId) && "0".equalsIgnoreCase(wareId)) {
            wrapper.eq(WareSkuEntity::getWareId, wareId);
        }
        if (StringUtils.hasText(skuId) && "0".equalsIgnoreCase(skuId)) {
            wrapper.eq(WareSkuEntity::getSkuId, skuId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> wareSkuEntities = this.baseMapper.selectList(new LambdaQueryWrapper<WareSkuEntity>()
                .eq(WareSkuEntity::getSkuId, skuId).eq(WareSkuEntity::getWareId, wareId));
        if (wareSkuEntities == null || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSku = new WareSkuEntity();
            wareSku.setSkuId(skuId);
            wareSku.setWareId(wareId);
            wareSku.setStock(skuNum);
            wareSku.setStockLocked(0);

            //TODO 远程查询sku的名字,如果失败事物不会回滚
            //TODO 还可以勇什么办法让异常出现以后不回滚
            try {
                R r = productFeignService.info(skuId);
                String skuName = (String) r.get("skuInfo");

                if (r.getCode() == 0) {
                    wareSku.setSkuName(skuName);
                }
            }catch (Exception e){

            }
            this.baseMapper.insert(wareSku);

        } else {
            this.baseMapper.addStock(skuId, wareId, skuNum);
        }
    }


}