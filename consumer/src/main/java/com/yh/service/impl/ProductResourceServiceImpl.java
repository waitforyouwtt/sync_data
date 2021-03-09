package com.yh.service.impl;

import com.yh.dao.AppProductResourceDao;
import com.yh.service.ProductResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ProductResourceServiceImpl implements ProductResourceService {

    @Resource
    AppProductResourceDao resourceDao;
}
