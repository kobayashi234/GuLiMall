package com.exer.gulimall.thirdparty;

import com.exer.gulimall.thirdparty.qiniu.QiNiuUpload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallThirdPartyApplicationTests {
    @Autowired
    QiNiuUpload qiNiuUpload;
    @Test
    void contextLoads() {
        qiNiuUpload.Upload("D:\\28f296629cca865e.jpg", "28f296629cca865e.jpg");
    }

}
