package com.exer.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.exer.common.validator.AddGroup;
import com.exer.common.validator.UpdateGroup;
import com.exer.common.validator.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.exer.gulimall.product.entity.BrandEntity;
import com.exer.gulimall.product.service.BrandService;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-20 22:05:43
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand){

        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){
		brandService.updateDetail(brand);
        return R.ok();
    }

    @PostMapping("/update/status")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
