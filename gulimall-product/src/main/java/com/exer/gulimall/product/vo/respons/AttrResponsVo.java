package com.exer.gulimall.product.vo.respons;

import com.exer.gulimall.product.vo.request.AttrVo;
import lombok.Data;

@Data
public class AttrResponsVo extends AttrVo {
    private String catelogName;
    private String groupName;

    private Long[] catelogPath;

}
