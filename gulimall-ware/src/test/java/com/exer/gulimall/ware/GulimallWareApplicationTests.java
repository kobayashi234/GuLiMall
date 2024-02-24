package com.exer.gulimall.ware;

import com.exer.gulimall.ware.entity.PurchaseEntity;
import com.exer.gulimall.ware.service.PurchaseDetailService;
import com.exer.gulimall.ware.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallWareApplicationTests {

    @Autowired
    PurchaseService purchaseService;

    @Test
    void contextLoads() {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setStatus(0);
        purchaseService.save(purchaseEntity);
    }

}
