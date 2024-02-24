package com.exer.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exer.common.utils.PageUtils;
import com.exer.gulimall.product.entity.AttrEntity;
import com.exer.gulimall.product.vo.AttrGroupRelationVo;
import com.exer.gulimall.product.vo.request.AttrVo;
import com.exer.gulimall.product.vo.respons.AttrResponsVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-20 22:05:43
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long cateLogId, String type);

    AttrResponsVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] relationVo);

    PageUtils getNoRealtion(Long attrgroupId, Map<String, Object> params);
}

