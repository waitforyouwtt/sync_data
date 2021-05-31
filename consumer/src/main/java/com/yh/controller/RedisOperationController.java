package com.yh.controller;

import cn.hutool.json.JSONUtil;
import com.yh.entity.AppProductResource;
import com.yh.entity.query.QuerySingleResource;
import com.yh.service.SingleFindService;
import com.yh.utils.ConstantPool;
import com.yh.utils.ObjectUtils;
import com.yh.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Api(value = "RedisOperationController")
@RestController
@RequestMapping("/redis")
public class RedisOperationController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
   SingleFindService singleFindService;

    @ApiOperation(value = "",tags = {"RedisOperationController"})
    @GetMapping("/syncResourceToRedis")
    public String syncResourceToRedis(){
        //获取缓存
        Object object =  redisUtils.get(ConstantPool.RESOURCES_REDIS_KEY);
        log.info("从缓存获取的数据"+ object);
        String str = "";
        if (object == null){
            List<AppProductResource> resources = singleFindService.queryAllResources();
            str = JSONUtil.toJsonStr(resources) ;
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(ConstantPool.RESOURCES_REDIS_KEY,str,1000L, TimeUnit.MINUTES);
            log.info("数据插入缓存" + str);
        }
        Object redis =  redisUtils.get(ConstantPool.RESOURCES_REDIS_KEY);
                 str = redis.toString();
        return str;
    }

    @ApiOperation(value = "",tags = {"RedisOperationController"})
    @GetMapping("/queryResourceToRedis")
    public List<AppProductResource> queryResourceToRedis(List<QuerySingleResource> queryResources){
        List<AppProductResource> result = new ArrayList<>();

        List<AppProductResource> suns = new ArrayList<>();
        List<AppProductResource> parent = new ArrayList<>();

        //获取缓存
        Object object =  redisUtils.get(ConstantPool.RESOURCES_REDIS_KEY);
        log.info("从缓存获取的数据"+ object);

        //参数拼接
        Map<String,QuerySingleResource> paramMap = new HashMap<>();
        queryResources.stream().forEach(v->{
            String key = v.getProductCode()+"_"+v.getResourceCode()+"_"+v.getResourceName();
            paramMap.put(key,v);
        });

        if (object != null){
            List<AppProductResource> resources = ObjectUtils.castList(object, AppProductResource.class);
            List<AppProductResource> resourcesY = resources.stream().filter(v-> "Y".equals(v.getStatus())).collect(Collectors.toList());
            for (AppProductResource resource : resourcesY){
                String key = resource.getProductCode()+"_"+resource.getResourceCode()+"_"+resource.getResourceName();
                if (paramMap.get(key) != null){
                    suns.add(resource);
                }
            }
            if (!CollectionUtils.isEmpty(suns)){
                suns.removeIf(Objects::isNull);
                //List<String> products = suns.stream().map(AppProductResource::getProductCode).distinct().collect(toList());
                //List<String> resourceCodes = suns.stream().map(AppProductResource::getParentCode).distinct().collect(toList());

                Map<String,AppProductResource> map = map(suns);

                for (AppProductResource resource : resourcesY){
                    String key = resource.getProductCode()+"_"+resource.getResourceCode();
                    if (map.get(key) != null){
                        parent.add(resource);
                    }
                }
            }
            result.addAll(suns);
            result.addAll(parent);
            return result;
        }
       return result;
    }

    private Map<String,AppProductResource> map  (List<AppProductResource> suns){
        //参数拼接
        Map<String,AppProductResource> paramMap = new HashMap<>();
        suns.stream().forEach(v->{
            String key = v.getProductCode()+"_"+v.getResourceCode();
            paramMap.put(key,v);
        });
        return paramMap;
    }

}
