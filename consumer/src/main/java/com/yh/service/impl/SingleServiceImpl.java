package com.yh.service.impl;


import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yh.dao.AppUserRoleDao;
import com.yh.entity.AppProductRole;
import com.yh.entity.AppTenantInfo;
import com.yh.entity.AppUserRole;
import com.yh.entity.RelationUserRole;
import com.yh.entity.UserBase;
import com.yh.feign.DataSynchronizeFeign;
import com.yh.service.SingleFindService;
import com.yh.service.SingleService;
import com.yh.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SingleServiceImpl implements SingleService {

    @Autowired
    DataSynchronizeFeign feign;

    @Autowired
    AppUserRoleDao appUserRoleDao  ;


    @Autowired
    SingleFindService singleFindService;

    @Override
    public String syncRelationUserRoles() {

        List<Long> countRelationUserRoles = feign.findCountRelationUserRoles();
        if (CollectionUtils.isEmpty(countRelationUserRoles)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countRelationUserRoles.size());
        List<List<Long>> partition = Lists.partition(countRelationUserRoles, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        List<AppUserRole> lists = singleFindService.queryUserroles();
        Map<String, AppUserRole> tempMap = new HashMap<>();
        for(AppUserRole appUserRole: lists){
            String key = appUserRole.getProductCode().concat("_").concat(appUserRole.getTenantCode()).concat("_").concat(appUserRole.getUserCode()).concat("_").concat(appUserRole.getRoleCode());
            log.info("全量查询的用户角色key:{}",key);
            tempMap.put(key, appUserRole);
        }

        for (List<Long> list : partition){
            Long start = Collections.min(list);
            Long end = Collections.max(list);
            log.info("每段的开始值&结束值：{},{}", start, end);
            return doWork2(start, end,tempMap);
        }
        return null;
    }


    private String doWork2(Long start, Long end,Map<String, AppUserRole> tempMap){

        List<RelationUserRole> relationUserRoles = feign.relationUserRoles(start, end);

        if (CollectionUtils.isEmpty(relationUserRoles)){
            log.info("此区间没有数据");
            return "fail";
        }

        log.info("需要同步的区间总体条数:{}",relationUserRoles.size());
        List<AppUserRole> roleUsers = convertRoleUser(relationUserRoles,tempMap);

        if (!CollectionUtils.isEmpty(roleUsers)){
            List<List<AppUserRole>> partition = Lists.partition(roleUsers, 300);
            for (List<AppUserRole> list : partition){
                appUserRoleDao.insertOrUpdateBatch( list );
            }
        }

        log.info("发出线程任务完成的信号");
        return "true";
    }

    private List<AppUserRole> convertRoleUser(List<RelationUserRole> relationUserRoles,Map<String, AppUserRole> tempMap) {

        //用户
        List<Long> userLongs = relationUserRoles.stream().map(RelationUserRole::getUserId).distinct().collect(Collectors.toList());
        List<UserBase> userBases = this.singleFindService.queryByUserIds(userLongs);

        Map<Long,UserBase> userMap = new HashMap<>();
        relationUserRoles.stream().forEach(m->{
            Optional<UserBase> userObj =  userBases.stream().filter(t -> StringUtils.isNotBlank(m.getUserId().toString())
                    && (t.getUserInfoId() == m.getUserId())).findAny();
            if(userObj.isPresent()) {
                userMap.put(m.getUserId(),userObj.get());
                return;
            }
            userMap.put(m.getUserId(),null);
        });

        //角色
        List<Long> roleIds = relationUserRoles.stream().map(RelationUserRole::getRoleId).distinct().collect(Collectors.toList());
        List<AppProductRole> roles = this.singleFindService.findProductCodes2(roleIds);

        //应用也来源于角色
        List<String> productCodes = roles.stream().map(AppProductRole::getProductCode).distinct().collect(Collectors.toList());

        //租户
        List<AppTenantInfo> tenants = this.singleFindService.findTenants();
        //租户
        Map<String, String> tenantMap = new HashMap<>();
        tenants.stream().forEach(v->{
            tenantMap.put(v.getProductCode(), v.getCode());
        });

        //未同步的用户
        List<Long> noSysData = new ArrayList<>();

        log.info("应用的集合： {}",productCodes);
        List<AppUserRole> userRoles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productCodes)){
            for (RelationUserRole info : relationUserRoles){
                for (String productCode: productCodes){

                    if (userMap.get(info.getUserId()) == null){
                        noSysData.add(info.getUserId());
                        continue;
                    }

                    //查询租户编码
                    String key = productCode.concat("_").concat(tenantMap.get(productCode)).concat("_")
                            .concat(userMap.get(info.getUserId()).getUserCode().concat("_")
                            .concat(info.getRoleId().toString()));
                    log.info("key:"+key);

                    AppUserRole userRole = tempMap.get(key);
                    if (!Objects.isNull(userRole)){
                        break;
                    }

                    AppUserRole view = new AppUserRole();
                    view.setProductCode(productCode);
                    view.setTenantCode(tenantMap.get(productCode));
                    view.setUserCode(userMap.get(info.getUserId()).getUserCode());
                    view.setUserName(userMap.get(info.getUserId()).getRealName());
                    view.setRoleCode(info.getRoleId().toString());
                    view.setPlatform("purchase");
                    view.setIsDelete(0);
                    if (StringUtils.isBlank(info.getCreatedBy())){
                        view.setCreatedBy("admin");
                    }else{
                        view.setCreatedBy(info.getCreatedBy());
                    }
                    if (StringUtils.isBlank(info.getUpdatedBy())){
                        view.setUpdatedBy("admin");
                    }else{
                        view.setUpdatedBy(info.getUpdatedBy());
                    }

                    view.setCreatedTime(new Date());
                    view.setUpdatedTime(new Date());
                    userRoles.add(view);
                    log.info("total总条数："+userRoles.size());
                }
            }
            log.info("待插入的数据的总条数：{}",userRoles.size());
        }
        log.info("未获取到符合到用户的同步数据有:{}",JSONUtil.toJsonStr(noSysData));
        return userRoles;
    }
}
