package com.exer.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDoneVO {
    Long id;
    private List<PurchaseItemDoneVo> items;
}
