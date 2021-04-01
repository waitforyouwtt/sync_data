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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        int start = 0;
        int end   = 0;

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService importThreadPool = new ThreadPoolExecutor(5, 2000, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());

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
                            return doWork(start, end);
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
        importThreadPool.shutdown();
        return "成功";
    }


    private String doWork(Integer start, Integer end){
        AppProductResourceDao resourceDao   = SpringContextHolder.getBean(AppProductResourceDao.class);
        List<MenuInfo> menuInfos = feign.findMenuBetweenIds(start,end);
        if (CollectionUtils.isEmpty(menuInfos)){
            log.info("此区间没有数据");
            return "false";
        }
        log.info("需要同步的区间总体条数:{}",menuInfos.size());

        if (!CollectionUtils.isEmpty(menuInfos)){
            List<AppProductResource> productResources = convertMenuToResource(menuInfos);
            if (!CollectionUtils.isEmpty(productResources)){
                log.info("开始插入数据库");
                resourceDao.insertOrUpdateBatch(productResources);
            }
        }
        log.info("发出线程任务完成的信号");
        return "true";
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

        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(countMenus.size()/1000);

        try {
            for (List<Integer> lists : partition){
                Integer start = Collections.min(lists);
                Integer end = Collections.max(lists);
                log.info("每段的开始值&结束值：{},{}",start,end);

                ImportMenuPermissionTask task = new ImportMenuPermissionTask(start,end, countDownLatch);
                executor.execute(task);
            }
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
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

        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(countRoles.size()/1000);

        try {
            for (List<Integer> lists : partition){
                Integer start = Collections.min(lists);
                Integer end = Collections.max(lists);
                log.info("每段的开始值&结束值：{},{}",start,end);

                ImportRoleTask task = new ImportRoleTask(start,end, countDownLatch);
                executor.execute(task);
            }
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
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
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(countRoleResource.size()/1000);
        try {
            for (List<Integer> lists : partition){
                Integer start = Collections.min(lists);
                Integer end = Collections.max(lists);
                log.info("每段的开始值&结束值：{},{}",start,end);

                ImportRoleResourceTask task = new ImportRoleResourceTask(start,end, countDownLatch);
                executor.execute(task);
            }
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
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
        int start = 0;
        int end   = 0;

        //使用线程池来发送各个用户的消息/通知任务集合
        ThreadFactory importThreadFactory = new ThreadFactoryBuilder().setNameFormat("import-data-pool-%d").build();
        ExecutorService importThreadPool = new ThreadPoolExecutor(5, 2000, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), importThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());

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
                    return doWork2(start, end);
                }
            });
        });
        return "成功";
    }

    private String doWork2(Integer start, Integer end){
        AppUserRoleDao appUserRoleDao  = SpringContextHolder.getBean(AppUserRoleDao.class);
        List<RelationUserRole> relationUserRoles = feign.relationUserRoles(start, end);
        if (CollectionUtils.isEmpty(relationUserRoles)){
            log.info("此区间没有数据");
            return "fail";
        }
        log.info("需要同步的区间总体条数:{}",relationUserRoles.size());
        List<AppUserRole> roleResources = convertRoleUser(relationUserRoles);
        if (!CollectionUtils.isEmpty(roleResources)){
            appUserRoleDao.insertOrUpdateBatch( roleResources );
        }


/*        List<MenuInfo> menuInfos = feign.findMenuBetweenIds(start,end);
        if (CollectionUtils.isEmpty(menuInfos)){
            log.info("此区间没有数据");
            return "false";
        }
        log.info("需要同步的区间总体条数:{}",menuInfos.size());

        if (!CollectionUtils.isEmpty(menuInfos)){
            List<AppProductResource> productResources = convertMenuToResource(menuInfos);
            if (!CollectionUtils.isEmpty(productResources)){
                log.info("开始插入数据库");
                resourceDao.insertOrUpdateBatch(productResources);
            }
        }*/
        log.info("发出线程任务完成的信号");
        return "true";
    }

    //* 任务执行器----------------------------------------------------------------------------------------------------------*/

    class ImportMenuTask implements Runnable {
        Integer start;
        Integer end;
        private CountDownLatch countDownLatch;
        private AppProductResourceDao resourceDao   = SpringContextHolder.getBean(AppProductResourceDao.class);

        public ImportMenuTask(Integer start,Integer end,CountDownLatch countDownLatch){
            this.start = start;
            this.end   = end;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            List<MenuInfo> menuInfos = feign.findMenuBetweenIds(start,end);
            if (CollectionUtils.isEmpty(menuInfos)){
                log.info("此区间没有数据");
                return;
            }
            log.info("需要同步的区间总体条数:{}",menuInfos.size());

            if (!CollectionUtils.isEmpty(menuInfos)){
                List<AppProductResource> productResources = convertMenuToResource(menuInfos);
                if (!CollectionUtils.isEmpty(productResources)){
                    log.info("开始插入数据库");
                    resourceDao.insertOrUpdateBatch(productResources);
                }
            }
            log.info("发出线程任务完成的信号");
            countDownLatch.countDown();
        }
    }

    class ImportMenuPermissionTask implements Runnable{
        Integer start;
        Integer end;
        private CountDownLatch countDownLatch;
        private AppProductResourceDao resourceDao   = SpringContextHolder.getBean(AppProductResourceDao.class);

        public ImportMenuPermissionTask(Integer start,Integer end,CountDownLatch countDownLatch){
            this.start = start;
            this.end   = end;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            List<MenuPermission> menuPermissions = feign.menuPermissionBetweenIds(start,end);
            log.info("需要同步的区间总体条数:{}",menuPermissions.size());
            if (CollectionUtils.isEmpty(menuPermissions)){
                log.info("此区间没有数据");
                return;
            }
            log.info("需要同步的区间总体条数:{}",menuPermissions.size());

            if (!CollectionUtils.isEmpty(menuPermissions)){
                List<AppProductResource> productResources = convertMenuPermissionResource(menuPermissions);
                if (!CollectionUtils.isEmpty(productResources)){
                    log.info("开始插入数据库");
                    resourceDao.insertOrUpdateBatch(productResources);
                }
            }
            log.info("发出线程任务完成的信号");
            countDownLatch.countDown();
        }
    }

    class ImportRoleTask implements Runnable{
        Integer start;
        Integer end;
        private CountDownLatch countDownLatch;
        private AppProductRoleDao resourceDao   = SpringContextHolder.getBean(AppProductRoleDao.class);

        public ImportRoleTask(Integer start,Integer end,CountDownLatch countDownLatch){
            this.start = start;
            this.end   = end;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            List<RoleInfo> roleInfos = feign.findRoleBetweenIds(start,end);
            if (CollectionUtils.isEmpty(roleInfos)){
                log.info("此区间没有数据");
                return;
            }
            log.info("需要同步的区间总体条数:{}",roleInfos.size());

            if (!CollectionUtils.isEmpty(roleInfos)){
                List<AppProductRole> roles = convertRoles(roleInfos);
                if (!CollectionUtils.isEmpty(roles)){
                    log.info("开始插入数据库");
                    resourceDao.insertOrUpdateBatch(roles);
                }
            }
            log.info("发出线程任务完成的信号");
            countDownLatch.countDown();
        }
    }

    class ImportRoleResourceTask implements Runnable{
        Integer start;
        Integer end;
        private CountDownLatch countDownLatch;
        private AppRoleResourceDao roleResourceDao  = SpringContextHolder.getBean(AppRoleResourceDao.class);
        public ImportRoleResourceTask(Integer start,Integer end,CountDownLatch countDownLatch){
            this.start = start;
            this.end   = end;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            List<RelationRoleMenuPermission> relationRoleMenuPermissions = feign.relationRoleMenuPermissions(start,end);
            if (CollectionUtils.isEmpty(relationRoleMenuPermissions)){
                log.info("此区间没有数据");
                return;
            }
            log.info("需要同步的区间总体条数:{}",relationRoleMenuPermissions.size());
            if (!CollectionUtils.isEmpty(relationRoleMenuPermissions)){
                List<AppRoleResource> roleResources = convertRoleResource(relationRoleMenuPermissions);
                if (!CollectionUtils.isEmpty(roleResources)){
                    roleResourceDao.insertOrUpdateBatch( roleResources );
                }
            }
            countDownLatch.countDown();
        }
    }


    class ImportRelationUserRoleTask implements Runnable{
        Integer start;
        Integer end;
        private CountDownLatch countDownLatch;
        private AppUserRoleDao appUserRoleDao  = SpringContextHolder.getBean(AppUserRoleDao.class);
        public ImportRelationUserRoleTask(Integer start,Integer end,CountDownLatch countDownLatch){
            this.start = start;
            this.end   = end;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            List<RelationUserRole> relationUserRoles = feign.relationUserRoles(start, end);
            if (CollectionUtils.isEmpty(relationUserRoles)){
                log.info("此区间没有数据");
                return;
            }
            log.info("需要同步的区间总体条数:{}",relationUserRoles.size());
            if (!CollectionUtils.isEmpty(relationUserRoles)){
                List<AppUserRole> roleResources = convertRoleUser(relationUserRoles);
                if (!CollectionUtils.isEmpty(roleResources)){
                    appUserRoleDao.insertOrUpdateBatch( roleResources );
                }
            }
            log.info("发出线程任务完成的信号"+Thread.currentThread().getName());
            countDownLatch.countDown();
        }
    }

   //实体转换器-----------------------------------------------------------------------------------------------------
    private List<AppProductResource> convertMenuToResource(List<MenuInfo> menus){

        List<String> productCodes = menus.stream().map(MenuInfo::getBusinessType).distinct().collect(Collectors.toList());
        List<AppTenantInfo> tenantCodes = singleFindService.findTenantCodes(productCodes);

        Map<String, String> dataMap = new HashMap<>();

        menus.forEach(m -> {
            Optional<AppTenantInfo> appTenantInfoObj =  tenantCodes.stream()
                    .filter(t -> StringUtils.isNotBlank(m.getBusinessType())
                            && t.getProductCode().equals(m.getBusinessType()))
                    .findAny();
            if(appTenantInfoObj.isPresent()) {
                dataMap.put(m.getBusinessType(),appTenantInfoObj.get().getCode());
                return;
            }

            dataMap.put(m.getBusinessType(), "default");
        });

        List<AppProductResource> resources = Collections.synchronizedList(new ArrayList());
        String tenantCode = null;
        AppProductResource queryResult = null;
        for (MenuInfo info: menus){
            if (StringUtils.isBlank(info.getBusinessType())){
                continue;
            }
            //根据应用获取租户信息
            if(StringUtils.isNotBlank(dataMap.get(info.getBusinessType()))){
                tenantCode = dataMap.get(info.getBusinessType());
            }
            //数据去掉重复
            queryResult = singleFindService.resourceDetails(info.getBusinessType(),tenantCode,String.valueOf(info.getId()));
            if (!Objects.isNull(queryResult)){
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

    private List<AppProductResource> convertMenuPermissionResource(List<MenuPermission> data) {

        List<AppProductResource> resources = new ArrayList<>();
        for (MenuPermission info: data){
            //根据应用获取租户信息
            String tenantCode = singleFindService.findTenantCode(info.getBusinessType());

            //数据去掉重复
            AppProductResource queryResult = singleFindService.resourceDetails(info.getBusinessType(),tenantCode,String.valueOf(info.getId()));
            if (!Objects.isNull(queryResult)){
                continue;
            }

            AppProductResource resource = new AppProductResource();
            resource.setProductCode(info.getBusinessType());
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setTenantCode(tenantCode);
            /**
             * operation_objective_id
             * select * from menu_info where id = '26';
             */
            MenuInfo menu = feign.findById(info.getOperationObjectiveId().toString());
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

    public List<AppProductRole> convertRoles(List<RoleInfo> roleInfos){
        List<AppProductRole> roles = new ArrayList<>();
        for (RoleInfo  info : roleInfos){

            //通过角色id倒推productCode
            List<String> productCodes = feign.findProductCodeByRoleId(info.getId().toString());

            if (!CollectionUtils.isEmpty(productCodes)){
                for (String code : productCodes){
                    if (info.getId() == null){
                        continue;
                    }
                    //根据应用获取租户信息
                    String tenantCode  =  singleFindService.findTenantCode(code);

                    //数据去掉重复
                    AppProductRole queryResult = singleFindService.roleDetails(code, tenantCode, info.getId().toString());
                    if (!Objects.isNull(queryResult)){
                        continue;
                    }

                    AppProductRole role = new AppProductRole();
                    role.setProductCode(code);
                    role.setUniqueCode(StringCustomizedUtils.uniqueCode());
                    role.setTenantCode(tenantCode);
                    role.setRoleCode(info.getId().toString());
                    role.setRoleName(info.getRoleName());
                    //存放角色
                    role.setDescription(info.getRoleTypeCode());
                    //存储第三方role_code
                    role.setOutRoleCode(info.getRoleCode());

                    role.setRoleStatus("Y");
                    role.setPlatform("purchase");
                    //存放父级编码
                    role.setExtension1(String.valueOf(info.getParentId()));
                    //存放path
                    role.setExtension2(info.getRolePath());
                    role.setIsDelete(0);
                    if (info.getCreatedBy() == null){
                        role.setCreatedBy("admin");
                    }else{
                        role.setCreatedBy(info.getCreatedBy());
                    }
                    if (info.getUpdatedBy() == null){
                        role.setUpdatedBy("admin");
                    }else{
                        role.setUpdatedBy(info.getUpdatedBy());
                    }
                    role.setCreatedTime(new Date());
                    role.setUpdatedTime(new Date());
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public List<AppRoleResource> convertRoleResource(List<RelationRoleMenuPermission> roleMenuPermissions){
        /*SingleFindService singleFindService = SpringContextHolder.getBean(SingleFindService.class);
        DataSynchronizeFeign feign = SpringContextHolder.getBean(DataSynchronizeFeign.class);*/

        List<AppRoleResource> roleResources = new ArrayList<>();

        for (RelationRoleMenuPermission  info : roleMenuPermissions){
            if (info.getRoleId() == null){
                continue;
            }
            //根据permissionId获取菜单按钮信息
            MenuPermission menuPermission = feign.findMenuPermissionId(info.getMenuPermissionId().toString());
            if (Objects.isNull(menuPermission)){
                continue;
            }
            //根据operationObjectiveId获取菜单信息
            MenuInfo menuInfo = feign.findById(String.valueOf(menuPermission.getOperationObjectiveId()));
            if (Objects.isNull(menuInfo)){
                continue;
            }
            //获取租户编码
            String tenantCode = singleFindService.findTenantCode(menuPermission.getBusinessType());
            //根据应用编码查询应用信息
            AppProduct product = singleFindService.findProductInfoCode(menuPermission.getBusinessType());
            if (Objects.isNull(product)){
                continue;
            }
            //判定重复
            AppRoleResource roleResourceResult = singleFindService.roleResourceDetails(menuPermission.getBusinessType(),tenantCode,String.valueOf(menuPermission.getId()),info.getRoleId().toString());
            if (!Objects.isNull(roleResourceResult)){
                continue;
            }

            AppRoleResource view = new AppRoleResource();
            view.setProductCode(menuPermission.getBusinessType());
            view.setProductName(product.getProductName());
            view.setTenantCode(tenantCode);
            view.setRoleCode(info.getRoleId().toString());
            //存放的是按钮的id 哦
            view.setResourceCode(String.valueOf(menuPermission.getId()));
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
            roleResources.add(view);
        }
        return roleResources;
    }

    private List<AppUserRole> convertRoleUser(List<RelationUserRole> relationUserRoles) {
        List<AppUserRole> userRoles = Collections.synchronizedList(new ArrayList());
        for (RelationUserRole info : relationUserRoles){
            if (info.getUserId() == null){
                continue;
            }
            List<UserBase> users = singleFindService.queryByUserId(info.getUserId());
            if (Objects.isNull(users)){
                continue;
            }
            UserBase user = users.get(0);
            List<String> productCodes = singleFindService.findProductCodes(info.getRoleId());
            if (CollectionUtils.isEmpty(productCodes)){
                continue;
            }
            for (String productCode: productCodes){
                //查询租户编码
                String tenantCode = singleFindService.findTenantCode(productCode);
                AppUserRole userRole = singleFindService.roleUserDetails(productCode,tenantCode,user.getUserCode(),info.getRoleId().toString());
                if (!Objects.isNull(userRole)){
                    break;
                }
                AppUserRole view = new AppUserRole();
                view.setProductCode(productCode);
                view.setTenantCode(tenantCode);
                view.setUserCode(user.getUserCode());
                view.setUserName(user.getRealName());
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
            }
        }
        return userRoles;
    }
}
