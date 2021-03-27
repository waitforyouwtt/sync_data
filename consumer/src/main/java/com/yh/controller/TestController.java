package com.yh.controller;

import com.yh.entity.UserBase;
import com.yh.service.SingleFindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "TestController")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
   SingleFindService singleFindService;

    @ApiOperation(value = "根据userinfo.id 查询用户信息",notes = "根据userinfo.id 查询用户信息",tags = {"TestController"})
    @GetMapping("/queryByUserId/{id}")
    public List<UserBase> queryByUserId(@PathVariable("id")long id){
       return singleFindService.queryByUserId(id);
    }
}
