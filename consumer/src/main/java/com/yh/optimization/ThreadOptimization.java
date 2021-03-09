package com.yh.optimization;

import com.yh.entity.HouseFeedbackCount;
import com.yh.service.SingleFindService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ThreadOptimization {

    @Autowired
    SingleFindService singleFindService;

    @Autowired
    XxxService xxxService;

/*    public void xxx(){
        log.info("房屋数仓数据同步调度开始！");
        Date yesterday = null;
        HouseFeedbackCount hfb = getHouseFeedbackCountTime(21);
        if (hfb != null){
            yesterday = DateUtils.addDays(hfb.getEndDate(),1);
        }else{
            yesterday= DateUtils.addDays(new Date(),-1);//今天
        }
        Date today= DateUtils.addDays(new Date(),0);//今天
        String beginTime = DateUtils.toString(yesterday,DateUtils.YYYY_MM_DD)+" 00:00:00";
        String endTime = DateUtils.toString(today,DateUtils.YYYY_MM_DD)+" 23:59:59";
        System.out.println("生成任务开始"+beginTime+endTime);
        int pageSize = 20000;
        int size = houseService.getHouseTotal(beginTime, endTime);
        if(size > pageSize){
            int page = size%pageSize==0?(size/pageSize):(size/pageSize)+1;
            System.out.println(page);
            int begin = 0;
            int end = 0;
            for (int i = 0; i < page; i++) {
                begin = pageSize*i+1;
                end = pageSize*(i+1);
                houseService.updateHouse(beginTime, endTime, begin, end);
            }
        }else{
            houseService.updateHouse(beginTime, endTime, 1, size);
        }
        hfb = new HouseFeedbackCount();
        hfb.setBizname("房屋数仓数据同步定时生成任务");
    }*/

    public void xxx(){
        int pageSize = 20000;
        List<String> productCodes = Arrays.asList("1","2");
        //要同步到总条数
        int size = singleFindService.findUserProducts(productCodes).size();
        if (size > pageSize){
            int page = size%pageSize==0?(size/pageSize):(size/pageSize)+1;
            log.info("要执行的总页数：{}",page);
            int begin = 0;
            int end   = 0;
            for (int i = 0; i < page; i++) {
                begin = pageSize * i + 1;
                end = pageSize * (i + 1);
                //houseService.updateHouse(beginTime, endTime, begin, end);
                xxxService.updateXXX();
                log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆分页执行修改逻辑哦☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
            }
        }else{
            //houseService.updateHouse(beginTime, endTime, 1, size);
            xxxService.updateXXX();
            log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆直接执行修改逻辑哦☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
        }
    }
}
