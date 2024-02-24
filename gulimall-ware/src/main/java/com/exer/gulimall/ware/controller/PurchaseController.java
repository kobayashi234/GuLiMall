package com.exer.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.exer.gulimall.ware.entity.PurchaseDetailEntity;
import com.exer.gulimall.ware.service.PurchaseDetailService;
import com.exer.gulimall.ware.vo.MergeVo;
import com.exer.gulimall.ware.vo.PurchaseDoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.exer.gulimall.ware.entity.PurchaseEntity;
import com.exer.gulimall.ware.service.PurchaseService;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.R;


/**
 * 采购信息
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-22 20:46:19
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseDetailService purchaseDetailService;


    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }


    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo vo) {
        List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listByIds(vo.getItems());
        for (PurchaseDetailEntity entity : detailEntities) {
            if(entity.getStatus() != 0 || entity.getStatus() != 1)
                return R.error("选择的订单状态不符合要求");
        }

        purchaseService.mergePurchase(vo);

        return R.ok();
    }

    ///ware/purchase/unreceive/list
    @RequestMapping("/unreceive/list")
    public R unReceive(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceive(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVO doneVO){
        purchaseService.done(doneVO);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
