package com.exer.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.product.dao.SpuImagesDao;
import com.exer.gulimall.product.entity.SpuImagesEntity;
import com.exer.gulimall.product.service.SpuImagesService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveImages(Long id, List<String> images) {
        if(images != null || images.size() == 0){
        }else{
            List<SpuImagesEntity> collect = images.stream().map(item -> {
                SpuImagesEntity spuImages = new SpuImagesEntity();
                spuImages.setSpuId(id);
                spuImages.setImgUrl(item);
                return spuImages;
            }).collect(Collectors.toList());

            this.saveBatch(collect);
        }
    }

}