package com.exer.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exer.common.utils.PageUtils;
import com.exer.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author ldy
 * @email ldy@gmail.com
 * @date 2023-09-22 20:46:19
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

