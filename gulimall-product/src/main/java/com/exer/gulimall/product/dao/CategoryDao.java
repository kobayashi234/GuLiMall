package com.exer.gulimall.product.dao;

import com.exer.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-20 22:05:43
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
