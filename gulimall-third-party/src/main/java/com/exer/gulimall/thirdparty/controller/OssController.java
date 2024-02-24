package com.exer.gulimall.thirdparty.controller;

import com.exer.gulimall.thirdparty.qiniu.QiNiuUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OssController {
    @Autowired
    QiNiuUpload qiNiuUpload;

    @RequestMapping("oss/policy")
    public String policy(){
        String token = qiNiuUpload.getToken();
        return token;
    }
}
