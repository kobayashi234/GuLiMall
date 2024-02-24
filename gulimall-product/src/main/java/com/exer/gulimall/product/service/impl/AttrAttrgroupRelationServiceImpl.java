package com.exer.gulimall.product.service.impl;

import com.exer.gulimall.product.vo.AttrGroupRelationVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exer.common.utils.PageUtils;
import com.exer.common.utils.Query;

import com.exer.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.exer.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.exer.gulimall.product.service.AttrAttrgroupRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveBatch(List<AttrGroupRelationVo> voList) {
        List<AttrAttrgroupRelationEntity> relationEntityList = voList.stream().map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(item.getAttrId());
            relationEntity.setAttrGroupId(item.getAttrGroupId());
            return relationEntity;
        }).collect(Collectors.toList());

        this.saveBatch(relationEntityList);
    }

}