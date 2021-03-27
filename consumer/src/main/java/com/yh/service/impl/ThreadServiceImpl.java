package com.yh.service.impl;

import com.google.common.collect.Lists;
import com.yh.dao.AppProductResourceDao;
import com.yh.dao.AppProductRoleDao;
import com.yh.dao.AppRoleResourceDao;
import com.yh.dao.AppUserRoleDao;
import com.yh.entity.*;
import com.yh.feign.DataSynchronizeFeign;
import com.yh.handlepool.Constant;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(countMenus.size()/1000);

        try {
            for (List<Integer> lists : partition){
                Integer start = Collections.min(lists);
                Integer end = Collections.max(lists);
                log.info("每段的开始值&结束值：{},{}",start,end);

                ImportMenuTask task = new ImportMenuTask(start,end, countDownLatch);
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

    @Override
    public String syncRelationUserRoles() {
        List<Integer> countRelationUserRoles = feign.findCountRelationUserRoles();
        if (CollectionUtils.isEmpty(countRelationUserRoles)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countRelationUserRoles.size());
        List<List<Integer>> partition = Lists.partition(countRelationUserRoles, 1000);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(countRelationUserRoles.size()/1000);
        try {
            for (List<Integer> lists : partition){
                Integer start = Collections.min(lists);
                Integer end = Collections.max(lists);
                log.info("每段的开始值&结束值：{},{}",start,end);

                ImportRelationUserRoleTask task = new ImportRelationUserRoleTask(start,end, countDownLatch);
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

    private List<AppUserRole> convertRoleUser(List<RelationUserRole> relationUserRoles) {
        List<AppUserRole> roleResources = new ArrayList<>();
        for (RelationUserRole info : relationUserRoles){
            if (info.getUserId() == null){
                continue;
            }
            UserInfo user = singleFindService.findUserInfoById(info.getUserId().toString());
            if (Objects.isNull(user) || StringUtils.isBlank(user.getUserCode())){
                continue;
            }
            List<String> productCodes = singleFindService.findProductCodes(info.getRoleId());
            if (CollectionUtils.isEmpty(productCodes)){
                continue;
            }
            for (String productCode: productCodes){
                AppUserRole view = new AppUserRole();
                view.setProductCode(productCode);
                view.setTenantCode(singleFindService.findTenantCode(productCode));
                view.setUserCode(user.getUserNumber());
                view.setUserName(user.getName());
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
                roleResources.add(view);
            }
        }
        return roleResources;
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

    public List<AppRoleResource> convertRoleResource(List<RelationRoleMenuPermission> roleMenuPermissions){
        SingleFindService singleFindService = SpringContextHolder.getBean(SingleFindService.class);
        DataSynchronizeFeign feign = SpringContextHolder.getBean(DataSynchronizeFeign.class);

        List<AppRoleResource> roleResources = new ArrayList<>();

        for (RelationRoleMenuPermission  info : roleMenuPermissions){
            AppRoleResource view = new AppRoleResource();
            if (info.getRoleId() == null){
                continue;
            }
            MenuPermission menuPermission = feign.findMenuPermissionId(info.getMenuPermissionId().toString());
            if (Objects.isNull(menuPermission)){
                continue;
            }
            MenuInfo menuInfo = feign.findById(String.valueOf(menuPermission.getOperationObjectiveId()));
            if (Objects.isNull(menuInfo)){
                continue;
            }

            String tenantCode = singleFindService.findTenantCode(menuPermission.getBusinessType());

            //判定重复
            AppRoleResource roleResourceResult = singleFindService.roleResourceDetails(menuPermission.getBusinessType(),tenantCode,menuPermission.getPermissionCode(),info.getRoleId().toString());
            if (!Objects.isNull(roleResourceResult)){
                continue;
            }
            view.setProductCode(menuPermission.getBusinessType());
            view.setProductName(menuPermission.getBusinessType());
            view.setRoleCode(info.getRoleId().toString());
            view.setResourceCode(String.valueOf(menuInfo.getId()));
            view.setResourceName(menuPermission.getPermissionName());
            view.setTenantCode(tenantCode);
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
        private AppProductRoleDao resourceDao   = SpringContextHolder.getBean(AppProductRoleDao.class);
        private CountDownLatch countDownLatch;

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

    public List<AppProductRole> convertRoles(List<RoleInfo> roleInfos){
        // DataSynchronizeFeign feign   = SpringContextHolder.getBean(DataSynchronizeFeign.class);

        List<AppProductRole> roles = new ArrayList<>();
        for (RoleInfo  info : roleInfos){
            List<String> productCodes = feign.findProductCodeByRoleId(info.getId().toString());
            if (!CollectionUtils.isEmpty(productCodes)){
                for (String code : productCodes){
                    if (info.getId() == null){
                        continue;
                    }
                    String tenantCode  =  singleFindService.findTenantCode(code);

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

                    role.setDescription(info.getRoleTypeCode());
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

    private List<AppProductResource> convertMenuPermissionResource(List<MenuPermission> data) {
        /*DataSynchronizeFeign feign   = SpringContextHolder.getBean(DataSynchronizeFeign.class);*/

        List<AppProductResource> resources = new ArrayList<>();
        for (MenuPermission info: data){
            AppProductResource resource = new AppProductResource();
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setProductCode(info.getBusinessType());
            resource.setTenantCode(singleFindService.findTenantCode(info.getBusinessType()));
            /**
             * operation_objective_id
             * select * from menu_info where id = '26';
             */
            MenuInfo menu = feign.findById(info.getOperationObjectiveId().toString());
            if (Objects.isNull(menu)){
                resource.setParentCode("0");
            }else{
                resource.setParentCode(menu.getCode());
            }
            String resource_fix = Constant.BUTTON+info.getId()+"_";

            resource.setResourceCode(resource_fix+info.getPermissionCode());
            resource.setResourceName(resource_fix+info.getPermissionName());
            resource.setPath(info.getApiUrl());
            //固定为菜单
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
            if (Objects.isNull(menu) || StringUtils.isBlank(menu.getId().toString())){
                break;
            }else{
                resource.setParentCode(String.valueOf(menu.getId()));
            }
            resource.setExpand2(String.valueOf(info.getId()));
            resource.setStatus("Y");
            resource.setIsDelete(0);
            resource.setPlatform("purchase");
            resources.add(resource);
        }
        return resources;
    }


   //实体转换器-----------------------------------------------------------------------------------------------------
    private List<AppProductResource> convertMenuToResource(List<MenuInfo> menus){
        List<AppProductResource> resources = new ArrayList<>();
        for (MenuInfo info: menus){
            if (StringUtils.isBlank(info.getBusinessType()) || StringUtils.isBlank(info.getCode())){
                continue;
            }
            //根据应用获取租户信息
            String tenantCode = singleFindService.findTenantCode(info.getBusinessType());
            //前缀menu
            String resource_fix = Constant.MENU+info.getId()+"_";

            AppProductResource queryResult = singleFindService.resourceDetails(info.getBusinessType(),tenantCode,resource_fix+info.getCode());
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
            resource.setResourceCode(resource_fix+info.getCode());
            resource.setResourceName(resource_fix+info.getName());
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
            //存放菜单id
            resource.setExpand2(String.valueOf(info.getId()));
            //存放菜单父级id
            resource.setExpand3(String.valueOf(info.getParentId()));
            resource.setPlatform("purchase");
            resource.setUpdatedTime(new Date());
            resource.setCreatedTime(new Date());
            resources.add(resource);
        }
        return resources;
    }
}
