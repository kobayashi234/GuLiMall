package com.exer.gulimall.product;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exer.common.utils.Query;
import com.exer.gulimall.product.entity.AttrGroupEntity;
import com.exer.gulimall.product.service.AttrGroupService;
import com.exer.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    AttrGroupService attrGroupService;

    @Test
    public void test1(){
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.and(item -> item.eq(AttrGroupEntity::getAttrGroupId, "主体")
                .or().like(AttrGroupEntity::getAttrGroupName, "主体"));

        wrapper.eq(AttrGroupEntity::getAttrGroupId, 1L);

        List<AttrGroupEntity> list = attrGroupService.list(wrapper);
        System.out.println(list);

    }

}
