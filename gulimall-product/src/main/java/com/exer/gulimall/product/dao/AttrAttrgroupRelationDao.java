package com.exer.gulimall.product.dao;

import com.exer.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-20 22:05:43
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBachRelation(@Param("collect") List<AttrAttrgroupRelationEntity> collect);
}
