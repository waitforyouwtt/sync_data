package com.yh.optimization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class XxxServiceImpl implements XxxService{


    @Async
    @Transactional
    @Override
    public void updateXXX() {
       log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆人家是异步事务实现方法哦☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
    }
}
