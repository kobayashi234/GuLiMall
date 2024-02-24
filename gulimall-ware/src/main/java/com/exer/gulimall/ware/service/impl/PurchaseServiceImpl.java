package com.exer.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.exer.common.constant.WareConstant;
import com.exer.gulimall.ware.entity.PurchaseDetailEntity;
import com.exer.gulimall.ware.service.PurchaseDetailService;
import com.exer.gulimall.ware.service.WareSkuService;
import com.exer.gulimall.ware.vo.MergeVo;
import com.exer.gulimall.ware.vo.PurchaseDoneVO;
import com.exer.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.ware.dao.PurchaseDao;
import com.exer.gulimall.ware.entity.PurchaseEntity;
import com.exer.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new LambdaQueryWrapper<PurchaseEntity>().eq(PurchaseEntity::getStatus, 0).or()
                        .eq(PurchaseEntity::getStatus, 1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo vo) {
        Long purchaseId = vo.getPurchaseId();

        if (vo.getPurchaseId() == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.purchaseStatus.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // 采购单状态是0，1才可以合并
        List<Long> ids = vo.getItems();


        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = ids.stream().map(i -> {
            PurchaseDetailEntity detail = new PurchaseDetailEntity();
            detail.setId(i);
            detail.setPurchaseId(finalPurchaseId);
            detail.setStatus(WareConstant.purchaseDetailStatus.ASSIGNED.getCode());
            return detail;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);


    }

    @Override
    @Transactional
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或已分配
        List<PurchaseEntity> purchaseEntities = this.listByIds(ids);
        List<PurchaseEntity> collect = purchaseEntities.stream()
                .filter(item -> item.getStatus() == WareConstant.purchaseStatus.CREATED.getCode()
                        || item.getStatus() == WareConstant.purchaseStatus.ASSIGNED.getCode()).map(item -> {
                    //改为已领取
                    item.setStatus(WareConstant.purchaseStatus.RECEIVE.getCode());
                    return item;
                }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);

        //3、改变采购项的状态
        collect.forEach(item -> {
            purchaseDetailService.update(new LambdaUpdateWrapper<PurchaseDetailEntity>()
                    .eq(PurchaseDetailEntity::getPurchaseId, item.getId())
                    .set(PurchaseDetailEntity::getStatus, WareConstant.purchaseDetailStatus.BUYING.getCode()));
        });
    }

    @Transactional
    @Override
    public void done(PurchaseDoneVO doneVO) {
        //1、改变采购项状态
        List<PurchaseItemDoneVo> items = doneVO.getItems();
        boolean flag = true;
        ArrayList<PurchaseDetailEntity> detailEntities = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.purchaseDetailStatus.HASEERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.purchaseDetailStatus.FINISH.getCode());
                //入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            }
            detailEntity.setId(item.getItemId());
            detailEntities.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(detailEntities);

        //2、改变采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(doneVO.getId());
        purchaseEntity.setStatus(flag ? WareConstant.purchaseStatus.FINISH.getCode()
                : WareConstant.purchaseStatus.HASEERROR.getCode());
        this.updateById(purchaseEntity);

        //3、将成功采购的进行入库
    }

}