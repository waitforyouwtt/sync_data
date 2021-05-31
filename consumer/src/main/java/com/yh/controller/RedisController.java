package com.yh.controller;

import cn.hutool.json.JSONUtil;
import com.yh.entity.UserBase;
import com.yh.entity.UserInfo;
import com.yh.service.SingleFindService;
import com.yh.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "RedisController")
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
   SingleFindService singleFindService;

    @ApiOperation(value = "根据userinfo.id 查询用户信息",notes = "根据userinfo.id 查询用户信息",tags = {"RedisController"})
    @GetMapping("/queryByUserId/{id}")
    public List<UserBase> queryByUserId(@PathVariable("id")long id){
       return singleFindService.queryByUserId(id);
    }

    @ApiOperation(value = "",tags = {"RedisController"})
    @GetMapping(value = "/queryOneRole/{id}")
    public String queryOneRole(@PathVariable(value = "id") String id){
        //查询缓存中是否存在
        boolean hasKey = redisUtils.exists(id);
        String str = "";
        if(hasKey){
            //获取缓存
            Object object =  redisUtils.get(id);
            log.info("从缓存获取的数据"+ object);
            str = object.toString();
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            str = buildUserInfo().toString();
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id,str,10L, TimeUnit.MINUTES);
            log.info("数据插入缓存" + str);
        }
        return str;
    }

    @ApiOperation(value = "",tags = {"RedisController"})
    @GetMapping(value = "/queryRoles/{id}")
    public String queryRoles(@PathVariable(value = "id") String id){
        //查询缓存中是否存在
        boolean hasKey = redisUtils.exists(id);
        String str = "";
        if(hasKey){
            //获取缓存
            Object object =  redisUtils.get(id);
            log.info("从缓存获取的数据"+ object);
            str = object.toString();
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            str = JSONUtil.toJsonStr(buildUserInfos()) ;
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id,str,10L, TimeUnit.MINUTES);
            log.info("数据插入缓存" + str);
        }
        return str;
    }

    @ApiOperation(value = "",tags = {"RedisController"})
    @GetMapping(value = "/updateRole/{id}")
    public String updateRole(@PathVariable(value = "id") String id){
        //查询缓存中是否存在
        boolean hasKey = redisUtils.exists(id);
        String str = "";
        if(hasKey){
            //删除缓存
            redisUtils.remove(id);
            log.info("从数据库中获取数据");
            str = JSONUtil.toJsonStr(updateUserInfos()) ;
            redisUtils.set(id,str,10L, TimeUnit.MINUTES);
            //获取缓存
            Object object =  redisUtils.get(id);
            log.info("从缓存获取的数据"+ object);
            str = object.toString();
        }
        return str;
    }

    private static List<UserInfo> buildUserInfos(){
        List<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("1001","1001","17621007255@163.com","云澜","云澜",1,1));
        list.add(new UserInfo("1003","1003","11408675820@163.com","凌云","凌云",1,1));
        list.add(new UserInfo("1004","1004","13249713680@163.com","木兰","木兰",0,0));
        list.add(new UserInfo("1005","10005","11567795060@163.com","穆清","穆清",1,1));
        return list;
    }

    private static List<UserInfo> updateUserInfos(){
        List<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("1001","1001","17621007255@163.com","云澜","云澜",1,1));
        list.add(new UserInfo("1002","1002","15290810931@163.com","花落","花落",0,0));
        list.add(new UserInfo("1003","1003","11408675820@163.com","凌云","凌云",1,1));
        list.add(new UserInfo("1004","1004","13249713680@163.com","木兰","木兰",0,0));
        list.add(new UserInfo("1005","1005","11567795060@163.com","穆清","穆清",1,1));
        list.add(new UserInfo("1006","1006","11567795060@163.com","子怡","子怡",1,1));
        return list;
    }

/*    public static void main(String[] args) {
        List<UserInfo> users  = updateUserInfos();
        List<UserInfo> params = buildUserInfos();
        List<UserInfo> result = new ArrayList<>();

        Map<String,UserInfo> paramMap = new HashMap<>();
        params.stream().forEach(v->{
            String key = v.getUserNumber()+"_"+v.getNickname();
            paramMap.put(key,v);
        });
        for (UserInfo info : users){
            String key = info.getUserNumber()+"_"+info.getNickname();
            if (paramMap.get(key) != null){
                result.add(info);
            }
        }
        log.info("返回的结果:{}",JSONUtil.toJsonStr(result));
    }*/

    private UserInfo buildUserInfo(){
        return new UserInfo("1001","1001","17621007255@163.com","云澜","云澜哥哥",1,1);
    }
}
