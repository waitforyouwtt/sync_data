package com.yh;

import com.google.common.collect.Lists;
import com.yh.entity.MenuInfo;
import com.yh.service.MenuInfoService;
import com.yh.service.RelationUserRoleService;
import com.yh.service.SingleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProviderApplicationTest {

    @Autowired
    RelationUserRoleService relationUserRoleService;

    @Autowired
    MenuInfoService menuInfoService;

    @Autowired
    SingleQueryService singleQueryService;

    @Test
    public void testxx(){
        Long min = 9L;
        Long max = 1642L;
        List<MenuInfo> menuBetweenIds = menuInfoService.findMenuBetweenIds(min, max);
        log.info("得到的数据:{}",menuBetweenIds);
    }

    @Test
    public void test(){
        long min = 300000l;
        long max = 500000l;
        List<Integer> integers = relationUserRoleService.findIds(min, max);
        List<List<Integer>> partition = Lists.partition(integers, 1000);
        for (List<Integer> lists: partition){
            Integer start = Collections.min(lists);
            Integer end = Collections.max(lists);
            log.info("每段的开始值&结束值：{},{}",start,end);
        }
    }

    @Test
    public void testxxx(){
        Integer offset = 4;
        Integer num = 1000;
        List<MenuInfo> menuPage = menuInfoService.findMenuPage(offset, num);
        log.info("xxxx:{}",menuPage);
    }

    @Test
    public void xxxxx(){
        String code = "100";
        String xx   = "1669" ;
        String xxx  = "1558";
        List<MenuInfo> info = singleQueryService.findByAppCodeAndParentId(code, xx);
        log.info("得到的信息：{}",info);

    }
}
