package com.yh.controller;

import com.yh.entity.MenuInfo;
import com.yh.service.MenuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuInfoController {

    @Autowired
    MenuInfoService menuInfoService;

    @GetMapping("/list")
    public List<MenuInfo> findMenus(){
        return menuInfoService.findMenus();
    }


}
