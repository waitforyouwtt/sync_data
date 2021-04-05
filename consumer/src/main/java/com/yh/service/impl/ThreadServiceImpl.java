package com.yh.service.impl;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yh.dao.AppProductResourceDao;
import com.yh.dao.AppProductRoleDao;
import com.yh.dao.AppRoleResourceDao;
import com.yh.dao.AppUserRoleDao;
import com.yh.entity.*;
import com.yh.feign.DataSynchronizeFeign;
import com.yh.service.SingleFindService;
import com.yh.service.ThreadService;
import com.yh.utils.SpringContextHolder;
import com.yh.utils.StringCustomizedUtils;
import com.yh.view.ProductRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ThreadServiceImpl implements ThreadService {

    @Autowired
    DataSynchronizeFeign feign;

    @Autowired
    SingleFindService singleFindService;

    //第一步方法区：--------------------------------------------------------------------------------------------------------------
    @Async
    @Override
    public String menuSyncResource() {
        List<Integer> countMenus = feign.findCountMenus();
        if (CollectionUtils.isEmpty(countMenus)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countMenus.size());
        List<List<Integer>> partition = Lists.partition(countMenus, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        List<AppProductResource> lists = singleFindService.queryAllResources();
        Map<String, AppProductResource> tempMap = new HashMap<>();
        for(AppProductResource info: lists){
            String key = info.getProductCode().concat("_").concat(info.getTenantCode()).concat("_").concat(info.getResourceCode());
            log.info("全量查询的资源key:{}",key);
            tempMap.put(key, info);
        }

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(5, 2000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //执行多个带返回结果的发送任务,并取得多个消息/通知发送返回结果
        CompletionService<String> sendMultiMsgSvc = new ExecutorCompletionService(threadPool);
                partition.forEach(p -> {
                    sendMultiMsgSvc.submit(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            List<Integer> lists = p;
                            int start = Collections.min(lists);
                            int end = Collections.max(lists);
                            log.info("每段的开始值&结束值：{},{}", start, end);
                            return saveResourceByMenu(start, end,tempMap);
                        }
                    });
                });

//        //获取总体操作结果
//        StringBuilder processResult = new StringBuilder();
//        msgBodyRequest.getReceiveUsersList().parallelStream().forEach(rcvUser -> {
//            try {
//                processResult.append(sendMultiMsgSvc.take().get());
//            } catch (InterruptedException | ExecutionException e) {
//                throw new BusinessException(MsgCenterErrorCodeEnum.MSG_SEND_PROCESS_COMPLETE_FAILED.getCode(),
//                        MsgCenterErrorCodeEnum.MSG_SEND_PROCESS_COMPLETE_FAILED.getMessage() + e.getStackTrace());
//            }
//        });
//
//        if (StringUtils.isNotBlank(processResult.toString())) {
//            throw new BusinessException(MsgCenterErrorCodeEnum.MSG_SEND_PROCESS_COMPLETE_FAILED.getCode(),
//                    processResult.toString());
//        }
        threadPool.shutdown();
        return "成功";
    }

    @Async
    @Override
    public String menuPermissionSyncResource() {
        List<Integer> countMenus = feign.findCountPermission();
        if (CollectionUtils.isEmpty(countMenus)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countMenus.size());
        List<List<Integer>> partition = Lists.partition(countMenus, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        List<AppProductResource> lists = singleFindService.queryAllResources();
        Map<String, AppProductResource> tempMap = new HashMap<>();
        for(AppProductResource info: lists){
            String key = info.getProductCode().concat("_").concat(info.getTenantCode()).concat("_").concat(info.getResourceCode());
            log.info("全量查询的资源key:{}",key);
            tempMap.put(key, info);
        }

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService importThreadPool = new ThreadPoolExecutor(5, 2000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //执行多个带返回结果的发送任务,并取得多个消息/通知发送返回结果
        CompletionService<String> sendMultiMsgSvc = new ExecutorCompletionService(importThreadPool);
        partition.forEach(p -> {
            sendMultiMsgSvc.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    List<Integer> lists = p;
                    int start = Collections.min(lists);
                    int end = Collections.max(lists);
                    log.info("每段的开始值&结束值：{},{}", start, end);
                    return saveResourceByButton(start, end, tempMap);
                }
            });
        });

        return "成功";
    }

    @Async
    @Override
    public String roleSync() {
        List<String> countRoles = feign.findCountRoles();
        if (CollectionUtils.isEmpty(countRoles)){
            return "暂无数据需要同步";
        }
        List<Integer> codesInteger = countRoles.stream().map(Integer::parseInt).collect(Collectors.toList());
        log.info("需要同步数据的总体条数:{}",codesInteger.size());

        List<List<Integer>> partition = Lists.partition(codesInteger, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        List<AppProductRole> lists = singleFindService.queryAllRoles();
        Map<String, AppProductRole> tempMap = new HashMap<>();
        for(AppProductRole role: lists){
            String key = role.getProductCode().concat("_").concat(role.getTenantCode()).concat("_").concat(role.getRoleCode());
            log.info("全量查询的用户角色key:{}",key);
            tempMap.put(key, role);
        }

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService importThreadPool = new ThreadPoolExecutor(5, 2000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //执行多个带返回结果的发送任务,并取得多个消息/通知发送返回结果
        CompletionService<String> sendMultiMsgSvc = new ExecutorCompletionService(importThreadPool);
        partition.forEach(p -> {
            sendMultiMsgSvc.submit(new Callable<String>() {
                @Override
                public String call() {
                    List<Integer> lists = p;
                    int start = Collections.min(lists);
                    int end = Collections.max(lists);
                    log.info("每段的开始值&结束值：{},{}", start, end);
                    saveRole(start, end,tempMap);
                    return "success";
                }
            });
        });
        return "成功";
    }

    @Async
    @Override
    public String roleResource() {
        List<Integer> countRoleResource = feign.findCountRelationRoleMenuPermissions();
        if (CollectionUtils.isEmpty(countRoleResource)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countRoleResource.size());
        List<List<Integer>> partition = Lists.partition(countRoleResource, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        //查询全部
        List<AppRoleResource> queryAll = singleFindService.roleResources();
        Map<String,AppRoleResource> tempMap = new HashMap<>();
        for(AppRoleResource info: queryAll){
            String key = info.getProductCode().concat("_").concat(info.getTenantCode()).concat("_").concat(info.getRoleCode()).concat("_").concat(info.getResourceCode());
            log.info("全量查询角色资源的key:{}",key);
            tempMap.put(key, info);
        }

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService importThreadPool = new ThreadPoolExecutor(5, 2000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //执行多个带返回结果的发送任务,并取得多个消息/通知发送返回结果
        CompletionService<String> service = new ExecutorCompletionService(importThreadPool);

        partition.forEach(p -> {
            service.submit(new Callable<String>() {
                @Override
                public String call() {
                    List<Integer> lists = p;
                    int start = Collections.min(lists);
                    int end = Collections.max(lists);
                    log.info("每段的开始值&结束值：{},{}", start, end);
                    try{
                        saveRoleResource(start, end,tempMap);
                    }catch (Exception e){
                        log.error("导入数据发生异常：{}",e.getMessage());
                    }
                    return "success";
                }
            });
        });

        return "成功";
    }

    @Async
    @Override
    public String syncRelationUserRoles() {
        List<Integer> countRelationUserRoles = feign.findCountRelationUserRoles();
        if (CollectionUtils.isEmpty(countRelationUserRoles)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countRelationUserRoles.size());
        List<List<Integer>> partition = Lists.partition(countRelationUserRoles, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        List<AppUserRole> lists = singleFindService.queryUserroles();
        Map<String, AppUserRole> tempMap = new HashMap<>();
        for(AppUserRole appUserRole: lists){
            String key = appUserRole.getProductCode().concat("_").concat(appUserRole.getTenantCode()).concat("_").concat(appUserRole.getUserCode()).concat("_").concat(appUserRole.getRoleCode());
            log.info("全量查询的用户角色key:{}",key);
            tempMap.put(key, appUserRole);
        }

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService importThreadPool = new ThreadPoolExecutor(5, 2000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //执行多个带返回结果的发送任务,并取得多个消息/通知发送返回结果
        CompletionService<String> sendMultiMsgSvc = new ExecutorCompletionService(importThreadPool);
        partition.forEach(p -> {
            sendMultiMsgSvc.submit(new Callable<String>() {
                @Override
                public String call() {
                    List<Integer> lists = p;
                    int start = Collections.min(lists);
                    int end = Collections.max(lists);
                    log.info("每段的开始值&结束值：{},{}", start, end);
                    try{
                        saveUserRole(start, end,tempMap);
                    }catch (Exception e){
                        log.error("导入数据发生异常：{}",e.getMessage());
                    }
                    return "success";
                }
            });
        });
        return "成功";
    }
    //第二步落库区：------------------------------------------------------------------------------------------------------------------------
    private String saveResourceByMenu(Integer start, Integer end, Map<String, AppProductResource> tempMap) {
        AppProductResourceDao resourceDao = SpringContextHolder.getBean(AppProductResourceDao.class);
        List<MenuInfo> menuInfos = feign.findMenuBetweenIds(start, end);
        if (CollectionUtils.isEmpty(menuInfos)) {
            log.info("此区间没有数据");
            return "fail";
        }
        log.info("需要同步的区间总体条数:{}", menuInfos.size());
        List<AppProductResource> data = new ArrayList<>();
        try {
            data = convertMenuToResource(menuInfos, tempMap);
        } catch (Exception e) {
            log.error("菜单转换资源发生异常:{}", e.getStackTrace());
        }
        if (!CollectionUtils.isEmpty(data)) {
            List<List<AppProductResource>> partition = Lists.partition(data, 500);
            for (List<AppProductResource> params : partition) {
                log.info("开始插入数据库");
                resourceDao.insertOrUpdateBatch(params);
            }
        }
        log.info("发出线程任务完成的信号");
        return "true";
    }

    private String saveResourceByButton(Integer start, Integer end, Map<String, AppProductResource> tempMap) {
        AppProductResourceDao resourceDao = SpringContextHolder.getBean(AppProductResourceDao.class);
        List<MenuPermission> menuPermissions = feign.menuPermissionBetweenIds(start, end);
        if (CollectionUtils.isEmpty(menuPermissions)) {
            log.info("此区间没有数据");
            return "fail";
        }
        log.info("需要同步的区间总体条数:{}", menuPermissions.size());

        List<AppProductResource> data = new ArrayList<>();
        try {
            data = convertMenuPermissionResource(menuPermissions, tempMap);
        } catch (Exception e) {
            log.error("菜单按钮转换资源发生异常:{}", e.getStackTrace());
        }
        if (!CollectionUtils.isEmpty(data)) {
            List<List<AppProductResource>> partition = Lists.partition(data, 500);
            for (List<AppProductResource> params : partition) {
                log.info("开始插入数据库");
                resourceDao.insertOrUpdateBatch(params);
            }
        }
        log.info("发出线程任务完成的信号");
        return "true";
    }

    private String saveRole(Integer start, Integer end,Map<String, AppProductRole> tempMap){
        AppProductRoleDao roleDao = SpringContextHolder.getBean(AppProductRoleDao.class);
        List<RoleInfo> roleInfos = feign.findRoleBetweenIds(start,end);
        if (CollectionUtils.isEmpty(roleInfos)){
            log.info("此区间没有数据");
            return "fail";
        }
        log.info("需要同步的区间总体条数:{}",roleInfos.size());
        List<AppProductRole> data = new ArrayList<>();
        try{
             data = convertRoles(roleInfos,tempMap);
        }catch (Exception e){
            log.error("转换角色发生异常：{}",e.getStackTrace());
        }

        if (!CollectionUtils.isEmpty(data)){
            List<List<AppProductRole>> partition = Lists.partition(data, 500);
            for (List<AppProductRole> params : partition) {
                log.info("开始插入数据库");
                roleDao.insertOrUpdateBatch(params);
            }
        }
        log.info("发出线程任务完成的信号");
        return "true";
    }

    private String saveRoleResource(Integer start, Integer end, Map<String, AppRoleResource> tempMap) {
        AppRoleResourceDao roleResourceDao = SpringContextHolder.getBean(AppRoleResourceDao.class);
        List<RelationRoleMenuPermission> data = feign.relationRoleMenuPermissions(start, end);
        if (CollectionUtils.isEmpty(data)) {
            log.info("此区间没有数据");
            return "fail";
        }

        log.info("需要同步的区间总体条数:{}", data.size());
        List<AppRoleResource> models = new ArrayList<>();
        try{
            models = convertRoleResource(data, tempMap);
        }catch (Exception e){
            log.error("转换角色资源发生异常：{}",e.getStackTrace());
        }

        if (!CollectionUtils.isEmpty(models)) {
            List<List<AppRoleResource>> partition = Lists.partition(models, 500);
            for (List<AppRoleResource> params : partition) {
                roleResourceDao.insertOrUpdateBatch(params);
            }
        }

        log.info("发出线程任务完成的信号");
        return "true";
    }

    private String saveUserRole(Integer start, Integer end,Map<String, AppUserRole> tempMap){
        AppUserRoleDao appUserRoleDao  = SpringContextHolder.getBean(AppUserRoleDao.class);
        List<RelationUserRole> relationUserRoles = feign.relationUserRoles(start, end);

        if (CollectionUtils.isEmpty(relationUserRoles)){
            log.info("此区间没有数据");
            return "fail";
        }
        log.info("需要同步的区间总体条数:{}",relationUserRoles.size());
        List<AppUserRole> roleUsers = new ArrayList<>();
        try{
            roleUsers = convertRoleUser(relationUserRoles,tempMap);
        }catch (Exception e){
            log.error("转换用户角色发生异常：{}",e.getStackTrace());
        }
        if (!CollectionUtils.isEmpty(roleUsers)){
            List<List<AppUserRole>> partition = Lists.partition(roleUsers, 500);
            for (List<AppUserRole> list : partition){
                appUserRoleDao.insertOrUpdateBatch( list );
            }
        }

        log.info("发出线程任务完成的信号");
        return "true";
    }

    //第三步：实体转换区-----------------------------------------------------------------------------------------------------
    private List<AppProductResource> convertMenuToResource(List<MenuInfo> menus,Map<String, AppProductResource> tempMap){
        //查询租户信息
        List<String> productCodes = menus.stream().map(MenuInfo::getBusinessType).distinct().collect(Collectors.toList());
        List<AppTenantInfo> tenantCodes = singleFindService.findTenantCodes(productCodes);
        //放置租户信息
        Map<String, String> tenantMap = new HashMap<>();
        menus.forEach(m -> {
            Optional<AppTenantInfo> appTenantInfoObj =  tenantCodes.stream()
                    .filter(t -> StringUtils.isNotBlank(m.getBusinessType()) && t.getProductCode().equals(m.getBusinessType())).findAny();
            if(appTenantInfoObj.isPresent()) {
                tenantMap.put(m.getBusinessType(),appTenantInfoObj.get().getCode());
                return;
            }
            tenantMap.put(m.getBusinessType(), "default");
        });

        List<AppProductResource> resources = Collections.synchronizedList(new ArrayList());
        String tenantCode = null;

        for (MenuInfo info: menus){
            if (StringUtils.isBlank(info.getBusinessType())){
                continue;
            }
            //根据应用获取租户信息
            if(StringUtils.isNotBlank(tenantMap.get(info.getBusinessType()))){
                tenantCode = tenantMap.get(info.getBusinessType());
            }
            //数据拼装key
            String key = info.getBusinessType().concat("_").concat(tenantCode).concat("_").concat(info.getId().toString());
            //数据去掉重复
            AppProductResource queryResult = tempMap.get(key);
            if (!Objects.isNull(queryResult)) {
                continue;
            }

            AppProductResource resource = new AppProductResource();
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setProductCode(info.getBusinessType());
            resource.setTenantCode(tenantCode);

            if (info.getParentId() == null || info.getParentId() ==0){
                resource.setParentCode("0");
            }else{
                resource.setParentCode(String.valueOf(info.getParentId()));
            }
            resource.setResourceCode(String.valueOf(info.getId()));
            resource.setResourceName(info.getName());
            resource.setPath(info.getDisplayUrl());
            //固定为菜单
            resource.setType("1");
            resource.setOrderNum(info.getRank());
            //根据生产环境而来
            if (info.getStatus() == 0){
                resource.setStatus("Y");
            }else{
                resource.setStatus("N");
            }
            resource.setIsDelete(0);
            if (StringUtils.isBlank(info.getCreatedBy())){
                resource.setCreatedBy("admin");
            }else{
                resource.setCreatedBy(info.getCreatedBy());
            }
            if (StringUtils.isBlank(info.getUpdatedBy())){
                resource.setUpdatedBy("admin");
            }else{
                resource.setUpdatedBy(info.getUpdatedBy());
            }
            //存放菜单code
            resource.setExpand2(String.valueOf(info.getCode()));
            //存放菜单父级id
            resource.setExpand3(String.valueOf(info.getParentId()));
            resource.setPlatform("purchase");
            resource.setUpdatedTime(new Date());
            resource.setCreatedTime(new Date());
            resources.add(resource);
        }
        return resources;
    }

    private List<AppProductResource> convertMenuPermissionResource(List<MenuPermission> data, Map<String, AppProductResource> tempMap) {
        //查询租户信息
        List<String> productCodes = data.stream().map(MenuPermission::getBusinessType).distinct().collect(Collectors.toList());
        List<AppTenantInfo> tenantCodes = singleFindService.findTenantCodes(productCodes);
        Map<String, String> tenantMap = new HashMap<>();
        data.forEach(m -> {
            Optional<AppTenantInfo> appTenantInfoObj =  tenantCodes.stream()
                    .filter(t -> StringUtils.isNotBlank(m.getBusinessType()) && t.getProductCode().equals(m.getBusinessType())).findAny();
            if(appTenantInfoObj.isPresent()) {
                tenantMap.put(m.getBusinessType(),appTenantInfoObj.get().getCode());
                return;
            }
            tenantMap.put(m.getBusinessType(), "default");
        });

        List<Integer> operationObjectiveIds = data.stream().map(MenuPermission::getOperationObjectiveId).distinct().collect(Collectors.toList());
        //Integer转string
        String menuIdString = operationObjectiveIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<MenuInfo> menus = feign.findMenuByIds(menuIdString);
        Map<Integer, MenuInfo> menuInfoMap = new HashMap<>();
        for (MenuInfo menuInfo: menus){
            menuInfoMap.put(menuInfo.getId(), menuInfo);
        }

        List<AppProductResource> resources = new ArrayList<>();
        String tenantCode = null;
        for (MenuPermission info: data){
            //根据应用获取租户信息
            if(StringUtils.isNotBlank(tenantMap.get(info.getBusinessType()))){
                tenantCode = tenantMap.get(info.getBusinessType());
            }
            //数据拼装key
            String key = info.getBusinessType().concat("_").concat(tenantCode).concat("_").concat(info.getId().toString());
            //数据去掉重复
            AppProductResource queryResult = tempMap.get(key);
            if (!Objects.isNull(queryResult)) {
                continue;
            }

            AppProductResource resource = new AppProductResource();
            resource.setProductCode(info.getBusinessType());
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setTenantCode(tenantCode);
            //根据OperationObjectiveId菜单
            MenuInfo menu = menuInfoMap.get(info.getOperationObjectiveId().toString());
            if (Objects.isNull(menu)){
                continue;
            }
            resource.setParentCode(String.valueOf(menu.getId()));
            resource.setResourceCode(String.valueOf(info.getId()));
            resource.setResourceName(info.getPermissionName());
            resource.setPath(info.getApiUrl());
            //固定为按钮
            resource.setType("2");
            if (info.getRank() == null){
                resource.setOrderNum(0);
            }else{
                resource.setOrderNum(info.getRank());
            }
            resource.setCreatedTime(new Date());
            resource.setUpdatedTime(new Date());
            if (info.getCreatedBy() == null){
                resource.setCreatedBy("admin");
            }else{
                resource.setCreatedBy(info.getCreatedBy());
            }
            if (info.getUpdatedBy() == null){
                resource.setUpdatedBy("admin");
            }else{
                resource.setUpdatedBy(info.getUpdatedBy());
            }
            //存放按钮code
            resource.setExpand2(info.getPermissionCode());
            //存放菜单父级id
            resource.setExpand3(String.valueOf(menu.getId()));
            resource.setStatus("Y");
            resource.setIsDelete(0);
            resource.setPlatform("purchase");
            resources.add(resource);
        }
        return resources;
    }

    public List<AppProductRole> convertRoles(List<RoleInfo> data,Map<String, AppProductRole> tempMap){
        List<AppProductRole> result = new ArrayList<>();
        //long转string
        List<Long> roleIds = data.stream().map(RoleInfo::getId).distinct().collect(Collectors.toList());
        String longs = StringUtils.join(roleIds.toArray(),",");
        //根据角色id倒推productCode
        List<ProductRoleVO> productRoles = this.feign.findProductCodeByRoleIds(longs);
        Map<Long, List<String>> productMap = new HashMap<>();
        for (ProductRoleVO p : productRoles){
             if (org.apache.commons.lang3.StringUtils.isBlank(p.getProductCode())){
                 productMap.put(p.getRoleId(),new ArrayList<>());
             }else{
                 List<String> product = Arrays.asList(p.getProductCode().split(","));
                 productMap.put(p.getRoleId(),product);
             }
        }
        List<AppTenantInfo> tenantInfos = this.singleFindService.findTenants();
        Map<String,String> tenantMap = new HashMap<>();
        for (AppTenantInfo info: tenantInfos){
            tenantMap.put(info.getProductCode(),info.getCode());
        }

        //默认应用
        List<String> productCodes;
        //查询全部应用
        List<AppProduct> productLists = this.singleFindService.findProductLists();
        for (RoleInfo  info : data){
            //如果根据角色id倒推relation_role_menu_permission未得到应用，则默认全部应用
            if(CollectionUtils.isEmpty(productMap.get(info.getId()))){
                List<String> collect = new ArrayList<>();
                if (!CollectionUtils.isEmpty(productLists)){
                    collect = productLists.stream().map(AppProduct::getProductCode).distinct().collect(Collectors.toList());
                }
                productCodes= collect;
            }else{
                productCodes = productMap.get(info.getId());
            }

            if (!CollectionUtils.isEmpty(productCodes)){
                for (String code : productCodes){
                    if (info.getId() == null){
                        continue;
                    }
                    //根据应用获取租户信息
                    String tenantCode  = tenantMap.get(code);
                    if (StringUtils.isBlank(tenantCode)){
                        tenantCode = "default";
                    }
                    //数据拼装key
                    String key = code.concat("_").concat(tenantCode).concat("_").concat(info.getId().toString());
                    //数据去掉重复
                    AppProductRole queryResult = tempMap.get(key);
                    if (!Objects.isNull(queryResult)) {
                        continue;
                    }

                    AppProductRole role = new AppProductRole();
                    role.setProductCode(code);
                    role.setUniqueCode(StringCustomizedUtils.uniqueCode());
                    if (StringUtils.isBlank(tenantCode)){
                        role.setTenantCode("defatult");
                    }else{
                        role.setTenantCode(tenantCode);
                    }

                    role.setRoleCode(info.getId().toString());
                    if (StringUtils.isNotBlank(info.getRoleName())){
                        role.setRoleName(info.getRoleName());
                    }
                    //存放角色
                    if (StringUtils.isNotBlank(info.getRoleTypeCode())){
                        role.setDescription(info.getRoleTypeCode());
                    }
                    //存储第三方role_code
                    if (StringUtils.isNotBlank(info.getRoleCode())){
                        role.setOutRoleCode(info.getRoleCode());
                    }
                    role.setRoleStatus("Y");
                    role.setPlatform("purchase");
                    //存放父级编码
                    if (info.getParentId() != null){
                        role.setExtension1(String.valueOf(info.getParentId()));
                    }
                    //存放path
                    if (StringUtils.isNotBlank(info.getRolePath())){
                        role.setExtension2(info.getRolePath());
                    }
                    role.setIsDelete(0);
                    if (StringUtils.isBlank(info.getCreatedBy())){
                        role.setCreatedBy("admin");
                    }else{
                        role.setCreatedBy(info.getCreatedBy());
                    }
                    if (StringUtils.isBlank(info.getUpdatedBy())){
                        role.setUpdatedBy("admin");
                    }else{
                        role.setUpdatedBy(info.getUpdatedBy());
                    }
                    role.setCreatedTime(new Date());
                    role.setUpdatedTime(new Date());

                    result.add(role);
                    log.info("待插入待总条数：{}",result.size());
                }
            }
        }
        return result;
    }

    public List<AppRoleResource> convertRoleResource(List<RelationRoleMenuPermission> roleMenuPermissions,Map<String,AppRoleResource> tempMap){
        List<AppRoleResource> result = new ArrayList<>();

        //获取菜单按钮信息
        List<Integer> menuIds = roleMenuPermissions.stream().map(RelationRoleMenuPermission::getMenuPermissionId).distinct().collect(Collectors.toList());
        //integer转string
        String menuString = menuIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<MenuPermission> menuPermissions = feign.findMenuPermissionIds(menuString);
        Map<Integer, MenuPermission > map = new HashMap<>();
        //存放按钮信息
        for (MenuPermission mp: menuPermissions){
            map.put(mp.getId(), mp);
        }

        //获取菜单信息
        List<Integer> objectiveIds = menuPermissions.stream().map(MenuPermission::getOperationObjectiveId).distinct().collect(Collectors.toList());
        //integer转string
        String menuIdString = objectiveIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        //存放菜单信息
        List<MenuInfo> menus = feign.findMenuByIds(menuIdString);
        Map<Integer, MenuInfo> menuInfoMap = new HashMap<>();
        for (MenuInfo menuInfo: menus){
            menuInfoMap.put(menuInfo.getId(), menuInfo);
        }

        //获取应用信息
        List<String> productCodes = menuPermissions.stream().map(MenuPermission::getBusinessType).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(productCodes)){
            return new ArrayList<>();
        }
        List<AppProduct> products = this.singleFindService.findProductLists(productCodes);
        Map<String, AppProduct> productMap = new HashMap<>();
        for (AppProduct product : products) {
            productMap.put(product.getProductCode(), product);
        }

        //获取租户信息
        List<AppTenantInfo> tenants = this.singleFindService.findTenantCodes(productCodes);
        Map<String, String> tenantMap = new HashMap<>();
        for (AppTenantInfo tenant : tenants) {
            tenantMap.put(tenant.getProductCode(), tenant.getCode());
        }

        for (RelationRoleMenuPermission  info : roleMenuPermissions){
            if (info.getRoleId() == null){
                continue;
            }
            //根据permissionId获取菜单按钮信息
            MenuPermission menuPermission = map.get(info.getMenuPermissionId());
            if (Objects.isNull(menuPermission)){
                continue;
            }
            //根据operationObjectiveId获取菜单信息
            MenuInfo menuInfo = menuInfoMap.get(menuPermission.getOperationObjectiveId());
            if (Objects.isNull(menuInfo)){
                continue;
            }
            //获取租户编码
            String tenantCode = tenantMap.get(menuPermission.getBusinessType());
            //根据应用编码查询应用信息
            AppProduct product = productMap.get(menuPermission.getBusinessType());
            if (Objects.isNull(product)){
                continue;
            }
            //String key = info.getProductCode().concat("_").concat(info.getTenantCode()).concat("_").concat(info.getRoleCode()).concat("_").concat(info.getResourceCode());
            String key = menuPermission.getBusinessType().concat("_").concat(tenantMap.get(menuPermission.getBusinessType())).concat("_").concat(info.getRoleId().toString()).concat("_").concat(menuPermission.getId().toString());
            //数据拼装key
            AppRoleResource roleResource = tempMap.get(key);
            //数据去掉重复
            if (!Objects.isNull(roleResource)) {
                continue;
            }

            AppRoleResource view = new AppRoleResource();
            view.setProductCode(menuPermission.getBusinessType());
            view.setProductName(product.getProductName());
            view.setTenantCode(tenantCode);
            view.setRoleCode(info.getRoleId().toString());
            //存放的是按钮的id 哦
            view.setResourceCode(String.valueOf(menuInfo.getId()));
            view.setResourceName(menuPermission.getPermissionName());
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
            result.add(view);
            log.info("当前插入行：{}");
        }
        log.info("待插入角色资源的总条数[out]：{}",result.size());
        return result;
    }

    private List<AppUserRole> convertRoleUser(List<RelationUserRole> relationUserRoles, Map<String, AppUserRole> tempMap) {
        SingleFindService singleFindService = SpringContextHolder.getBean(SingleFindService.class);
        //查询用户信息
        List<Long> userLongs = relationUserRoles.stream().map(RelationUserRole::getUserId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userLongs)) {
            return new ArrayList<>();
        }
        //根据user_info_id 查询用户信息
        List<UserBase> userBases = singleFindService.queryByUserIds(userLongs);
        //角色
        List<Long> roleIds = relationUserRoles.stream().map(RelationUserRole::getRoleId).distinct().collect(Collectors.toList());
        List<AppProductRole> roles = singleFindService.findProductCodes2(roleIds);

        Map<Long, UserBase> userMap = new HashMap<>();

        relationUserRoles.stream().forEach(m -> {
            Optional<UserBase> userObj = userBases.stream()
                    .filter(t -> StringUtils.isNotBlank(m.getUserId().toString())
                            && t.getUserInfoId().equals(m.getUserId())).findAny();
            if (userObj.isPresent()) {
                userMap.put(m.getUserId(), userObj.get());
                return;
            }
            userMap.put(m.getUserId(), null);
        });
        //Map<Long,UserBase> userMap =userBases.stream().collect(Collectors.toMap(UserBase::getUserInfoId, Function.identity()));

        //应用也来源于角色
        List<String> productCodes = roles.stream().map(AppProductRole::getProductCode).distinct().collect(Collectors.toList());
        log.info("应用的集合： {}", productCodes);
        if (CollectionUtils.isEmpty(productCodes)) {
            return new ArrayList<>();
        }
        List<AppUserRole> userRoles = new ArrayList<>();

        List<AppTenantInfo> tenants = singleFindService.findTenantCodes(productCodes);
        //获取租户信息
        Map<String, String> tenantMap = new HashMap<>();
        for (AppTenantInfo tenant : tenants) {
            tenantMap.put(tenant.getProductCode(), tenant.getCode());
        }
        for (RelationUserRole info : relationUserRoles) {
            for (String productCode : productCodes) {
                //查询租户编码
                if (null == userMap.get(info.getUserId())) {
                    continue;
                }
                //数据拼装key
                String key = productCode.concat("_").concat(tenantMap.get(productCode)).concat("_").concat(userMap.get(info.getUserId()).getUserCode().concat("_").concat(info.getRoleId().toString()));
                //数据去掉重复
                AppUserRole userRole = tempMap.get(key);
                if (!Objects.isNull(userRole)) {
                    continue;
                }
                AppUserRole view = new AppUserRole();
                view.setProductCode(productCode);
                view.setTenantCode(tenantMap.get(productCode));
                view.setUserCode(userMap.get(info.getUserId()).getUserCode());
                view.setUserName(userMap.get(info.getUserId()).getRealName());
                view.setRoleCode(info.getRoleId().toString());
                view.setPlatform("purchase");
                view.setIsDelete(0);
                if (StringUtils.isBlank(info.getCreatedBy())) {
                    view.setCreatedBy("admin");
                } else {
                    view.setCreatedBy(info.getCreatedBy());
                }
                if (StringUtils.isBlank(info.getUpdatedBy())) {
                    view.setUpdatedBy("admin");
                } else {
                    view.setUpdatedBy(info.getUpdatedBy());
                }
                view.setCreatedTime(new Date());
                view.setUpdatedTime(new Date());
                userRoles.add(view);
                log.info("total总条数：" + userRoles.size());
            }
        }
        log.info("待插入的数据的总条数：{}", userRoles.size());
        return userRoles;
    }

}
