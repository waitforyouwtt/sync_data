package com.yh.feign;

import com.baomidou.mybatisplus.extension.api.R;
import com.yh.entity.OauthProductVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shenshu", url = "http://o2o-support-dev.o2o-support-idaas-application.devgw.yonghui.cn/v1/product/add/oauth")
public interface ShenShuFeign {

    @ApiOperation("添加oauth应用")
    @PostMapping(value = "/add/oauth")
    R<String> addOauth(@RequestBody OauthProductVO vo);
}