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
import com.yh.service.ThreadService2;
import com.yh.utils.SpringContextHolder;
import com.yh.utils.StringCustomizedUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.jws.Oneway;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ThreadServiceImpl2 implements ThreadService2 {

    @Autowired
    DataSynchronizeFeign feign;

    @Autowired
    SingleFindService singleFindService;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    //第一：----------------------------------------------------------------------------------------------------------------
    @Async
    @Override
    public String menuSyncResource() {
        List<Integer> countMenus = feign.findCountMenus();
        if (CollectionUtils.isEmpty(countMenus)){
            return "暂无数据需要同步";
        }
        log.info("需要同步数据的总体条数:{}",countMenus.size());

        int batchNum = 500;
        List<List<Integer>> partition = Lists.partition(countMenus, 500);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        for (List<Integer> lists : partition){
            Integer start = Collections.min(lists);
            Integer end = Collections.max(lists);
            threadRunMenu(start,end,batchNum);
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

        int batchNum = 500;

        List<List<Integer>> partition = Lists.partition(countMenus, 500);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        for (List<Integer> lists : partition){
            Integer start = Collections.min(lists);
            Integer end = Collections.max(lists);
            threadRunPermission(start,end,batchNum);
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
        int batchNum = 500;
        List<List<Integer>> partition = Lists.partition(codesInteger, 500);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        for (List<Integer> lists : partition){
            Integer start = Collections.min(lists);
            Integer end = Collections.max(lists);
            threadRunRole(start,end,batchNum);
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
        int batchNum = 500;
        List<List<Integer>> partition = Lists.partition(countRoleResource, 500);
        log.info("根据同步数据总条数运算得到的分段条数：{}",partition.size());

        for (List<Integer> lists : partition){
            Integer start = Collections.min(lists);
            Integer end = Collections.max(lists);
            log.info("每段的开始值&结束值：{},{}",start,end);
            threadRunRoleResource(start,end,batchNum);
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
        private AppUserRoleDao appUserRoleDao  = SpringContextHolder.getBean(AppUserRoleDao.class);

        Integer start;
        Integer end;
        private CountDownLatch countDownLatch;

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
                view.setUserCode(user.getUserCode());
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

    //第二------------------------------------------------------------------------------------------------------------------------------------------------
    private void threadRunMenu(Integer start,Integer end,Integer batchNum){
        log.info("每段的开始值&结束值：{},{}",start,end);
        List<MenuInfo> menuInfos = feign.findMenuBetweenIds(start,end);
        log.info("需要同步的区间总体条数:{}",menuInfos.size());
        if (CollectionUtils.isEmpty(menuInfos)){
            log.info("此区间没有数据");
            return;
        }

        int totalNum = menuInfos.size();
        int pageNum = totalNum % batchNum == 0 ? totalNum / batchNum : totalNum / batchNum + 1;
        ExecutorService executor = Executors.newFixedThreadPool(pageNum);
        CountDownLatch countDownLatch = new CountDownLatch(pageNum);
        List subData = null;
        int fromIndex, toIndex;
        for (int i = 0; i < pageNum; i++) {
            fromIndex = i * batchNum;
            toIndex = Math.min(totalNum, fromIndex + batchNum);
            subData = menuInfos.subList(fromIndex, toIndex);
            ImportMenuTask task = new ImportMenuTask(subData, countDownLatch);
            executor.execute(task);
        }

        try {
            // 主线程必须在启动其它线程后立即调用CountDownLatch.await()方法，
            // 这样主线程的操作就会在这个方法上阻塞，直到其它线程完成各自的任务。
            // 计数器的值等于0时，主线程就能通过await()方法恢复执行自己的任务。
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
    }

    private void threadRunPermission(Integer start, Integer end, int batchNum) {
        log.info("每段的开始值&结束值：{},{}",start,end);
        List<MenuPermission> menuPermissions = feign.menuPermissionBetweenIds(start,end);
        log.info("需要同步的区间总体条数:{}",menuPermissions.size());
        if (CollectionUtils.isEmpty(menuPermissions)){
            log.info("此区间没有数据");
            return;
        }

        int totalNum = menuPermissions.size();
        int pageNum = totalNum % batchNum == 0 ? totalNum / batchNum : totalNum / batchNum + 1;
        ExecutorService executor = Executors.newFixedThreadPool(pageNum);
        CountDownLatch countDownLatch = new CountDownLatch(pageNum);
        List subData = null;
        int fromIndex, toIndex;
        for (int i = 0; i < pageNum; i++) {
            fromIndex = i * batchNum;
            toIndex = Math.min(totalNum, fromIndex + batchNum);
            subData = menuPermissions.subList(fromIndex, toIndex);
            log.info("按钮-fromIndex,toIndex:{},{}",fromIndex,toIndex);
            ImportMenuPermissionTask task = new ImportMenuPermissionTask(subData, countDownLatch);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
    }

    private void threadRunRole(Integer start, Integer end, int batchNum) {
        log.info("每段的开始值&结束值：{},{}",start,end);
        List<RoleInfo> roleInfos = feign.findRoleBetweenIds(start,end);
        log.info("需要同步的区间总体条数:{}",roleInfos.size());
        if (CollectionUtils.isEmpty(roleInfos)){
            log.info("此区间没有数据");
            return;
        }

        int totalNum = roleInfos.size();
        int pageNum = totalNum % batchNum == 0 ? totalNum / batchNum : totalNum / batchNum + 1;
        ExecutorService executor = Executors.newFixedThreadPool(pageNum);
        CountDownLatch countDownLatch = new CountDownLatch(pageNum);
        List subData = null;
        int fromIndex, toIndex;
        for (int i = 0; i < pageNum; i++) {
            fromIndex = i * batchNum;
            toIndex = Math.min(totalNum, fromIndex + batchNum);
            subData = roleInfos.subList(fromIndex, toIndex);
            ImportRoleTask task = new ImportRoleTask(subData, countDownLatch);
            executor.execute(task);
        }

        try {
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
    }

    private void threadRunRoleResource(Integer start, Integer end, int batchNum) {
        log.info("每段的开始值&结束值：{},{}",start,end);
        List<RelationRoleMenuPermission> roleResources = feign.relationRoleMenuPermissions(start,end);
        log.info("需要同步的区间总体条数:{}",roleResources.size());
        if (CollectionUtils.isEmpty(roleResources)){
            log.info("此区间没有数据");
            return;
        }
        int totalNum = roleResources.size();
        int pageNum = totalNum % batchNum == 0 ? totalNum / batchNum : totalNum / batchNum + 1;
        ExecutorService executor = Executors.newFixedThreadPool(pageNum);
        CountDownLatch countDownLatch = new CountDownLatch(pageNum);
        List<RelationRoleMenuPermission> subData;
        int fromIndex, toIndex;
        for (int i = 0; i < pageNum; i++) {
            fromIndex = i * batchNum;
            toIndex = Math.min(totalNum, fromIndex + batchNum);
            subData = roleResources.subList(fromIndex, toIndex);
            ImportRoleResourceTask task = new ImportRoleResourceTask(subData, countDownLatch);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
            log.info("数据操作完成!可以在此开始其它业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭线程池，释放资源
            executor.shutdown();
        }
    }

    //第三-----------------------------------------------------------------------------------------------------------------------
    class ImportMenuTask implements Runnable {
        private AppProductResourceDao resourceDao   = SpringContextHolder.getBean(AppProductResourceDao.class);
        List<MenuInfo> data;
        private CountDownLatch countDownLatch;
        public ImportMenuTask(List<MenuInfo> data,CountDownLatch countDownLatch){
            this.data = data;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            if (!CollectionUtils.isEmpty(data)){
                List<AppProductResource> productResources = convertMenuToResource(data);
                if (!CollectionUtils.isEmpty(productResources)){
                    for (AppProductResource info: productResources){
                        log.info("开始插入数据库");
                        List<AppProductResource> resources = new ArrayList<>();
                        resources.add(info);
                        resourceDao.insertOrUpdateBatch(resources);
                    }
                }
            }
            log.info("发出线程任务完成的信号");
            countDownLatch.countDown();
        }
    }

    class ImportMenuPermissionTask implements Runnable{
        private AppProductResourceDao resourceDao   = SpringContextHolder.getBean(AppProductResourceDao.class);
        List<MenuPermission> data;

        private CountDownLatch countDownLatch;
        public ImportMenuPermissionTask(List<MenuPermission> data,CountDownLatch countDownLatch){
            this.data = data;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            if (!CollectionUtils.isEmpty(data)){
                List<AppProductResource> productResources = convertMenuPermissionResource(data);
                SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
                if (!CollectionUtils.isEmpty(productResources)){
                    int result = 0;
                    for (AppProductResource info: productResources){
                        log.info("开始插入数据库");
                        List<AppProductResource> params = new ArrayList<>();
                        params.add(info);
                        resourceDao.insertOrUpdateBatch(params);
                        result ++;
                        if(result == 100){//每100条提交一次防止内存溢出
                            session.commit();
                            session.clearCache();
                        }
                    }
                }
            }
            log.info("发出线程任务完成的信号");
            countDownLatch.countDown();
        }
    }

    class ImportRoleTask implements Runnable{
        List<RoleInfo> data;
        private CountDownLatch countDownLatch;
        private AppProductRoleDao resourceDao   = SpringContextHolder.getBean(AppProductRoleDao.class);

        public ImportRoleTask(List<RoleInfo> data,CountDownLatch countDownLatch){
            this.data = data;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            if (!CollectionUtils.isEmpty(data)){
                List<AppProductRole> roles = convertRoles(data);
                SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
                if (!CollectionUtils.isEmpty(roles)){
                    int result = 0;
                    for (AppProductRole info: roles){
                        log.info("开始插入数据库");
                        List<AppProductRole> params = new ArrayList<>();
                        params.add(info);
                        resourceDao.insertBatch(params);
                        result ++;
                        if(result == 100){//每100条提交一次防止内存溢出
                            session.commit();
                            session.clearCache();
                        }
                    }
                }
            }
            log.info("发出线程任务完成的信号");
            countDownLatch.countDown();
        }
    }

    class ImportRoleResourceTask implements Runnable{
        List<RelationRoleMenuPermission> data;
        private CountDownLatch countDownLatch;
        private AppRoleResourceDao roleResourceDao  = SpringContextHolder.getBean(AppRoleResourceDao.class);
        public ImportRoleResourceTask(List<RelationRoleMenuPermission> data,CountDownLatch countDownLatch){
            this.data = data;
            this.countDownLatch = countDownLatch;
        }
        @Override
        public void run() {
            if (!CollectionUtils.isEmpty(data)){
                List<AppRoleResource> roleResources = convertRoleResource(data);
                SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
                if (!CollectionUtils.isEmpty(roleResources)){
                    int result = 0;
                    for (AppRoleResource info : roleResources){
                        List<AppRoleResource> params = new ArrayList<>();
                        params.add(info);
                        roleResourceDao.insertOrUpdateBatch( params);
                        result ++;
                        if(result == 100){//每100条提交一次防止内存溢出
                            session.commit();
                            session.clearCache();
                        }
                    }
                }
            }
            log.info("发出线程任务完成的信号"+Thread.currentThread().getName());
            countDownLatch.countDown();
        }
    }

    //第四：-----------------------------------------------------------------------------------------------------------------------
    private List<AppProductResource> convertMenuToResource(List<MenuInfo> menus){
        List<AppProductResource> resources = new ArrayList<>();
        for (MenuInfo info: menus){
            if (StringUtils.isBlank(info.getBusinessType())){
                continue;
            }
            String tenantCode = singleFindService.findTenantCode(info.getBusinessType());

            AppProductResource queryResult = singleFindService.resourceDetails(info.getBusinessType(),tenantCode,info.getId().toString());
            if (!Objects.isNull(queryResult)){
                continue;
            }

            AppProductResource resource = new AppProductResource();
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setProductCode(info.getBusinessType());
            resource.setTenantCode(tenantCode);

            if (Objects.isNull(info.getParentId().toString())){
                resource.setParentCode("0");
            }else{
                resource.setParentCode(info.getParentId().toString());
            }
            resource.setResourceCode(info.getId().toString());
            resource.setResourceName(info.getName());
            resource.setPath(info.getDisplayUrl());
            //固定为菜单
            resource.setType("1");
            resource.setOrderNum(info.getRank());
            if (info.getStatus() == 0){
                resource.setStatus("N");
            }else{
                resource.setStatus("Y");
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
            resource.setExpand2(info.getCode());
            resource.setPlatform("purchase");
            resource.setUpdatedTime(new Date());
            resource.setCreatedTime(new Date());
            resources.add(resource);
        }
        return resources;
    }

    private List<AppProductResource> convertMenuPermissionResource(List<MenuPermission> data) {
        DataSynchronizeFeign feign   = SpringContextHolder.getBean(DataSynchronizeFeign.class);

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
                resource.setParentCode(menu.getId().toString());
                resource.setExpand3(menu.getId().toString());
            }
            resource.setResourceCode(info.getId().toString());
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
            resource.setExpand2(info.getPermissionCode());
            resource.setStatus("Y");
            resource.setIsDelete(0);
            resource.setPlatform("purchase");
            resources.add(resource);
        }
        return resources;
    }

    public List<AppProductRole> convertRoles(List<RoleInfo> roleInfos){
        DataSynchronizeFeign feign = SpringContextHolder.getBean(DataSynchronizeFeign.class);
        List<AppProductRole> roles = new ArrayList<>();
        for (RoleInfo  info : roleInfos){
            List<String> productCodes = feign.findProductCodeByRoleId(info.getId().toString());
            if (!CollectionUtils.isEmpty(productCodes)){
                for (String code : productCodes){
                    String tenantCode = singleFindService.findTenantCode(code);
                    AppProductRole appProductRole = singleFindService.roleDetails(code, tenantCode, info.getId().toString());
                    if (!Objects.isNull(appProductRole)){
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
                    role.setExtension1(info.getParentId().toString());
                    role.setExtension2(info.getRolePath());
                    role.setCreatedTime(new Date());
                    role.setUpdatedTime(new Date());
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public List<AppRoleResource> convertRoleResource(List<RelationRoleMenuPermission> roleMenuPermissions){
        SingleFindService singleFindService = SpringContextHolder.getBean(SingleFindService.class);
        DataSynchronizeFeign feign = SpringContextHolder.getBean(DataSynchronizeFeign.class);
        List<AppRoleResource> roleResources = new ArrayList<>();
        for (RelationRoleMenuPermission  info : roleMenuPermissions){
            if (info.getRoleId() == null){
                continue;
            }
            MenuPermission menuPermission = feign.findMenuPermissionId(info.getMenuPermissionId().toString());
            if (Objects.isNull(menuPermission)){
                continue;
            }
            String tenantCode = singleFindService.findTenantCode(menuPermission.getBusinessType());

            //判定重复
            AppRoleResource roleResourceResult = singleFindService.roleResourceDetails(menuPermission.getBusinessType(),tenantCode,info.getMenuPermissionId().toString(),info.getRoleId().toString());
            if (!Objects.isNull(roleResourceResult)){
                continue;
            }

            AppRoleResource view = new AppRoleResource();
            view.setProductCode(menuPermission.getBusinessType());
            view.setProductName(menuPermission.getBusinessType());
            view.setRoleCode(info.getRoleId().toString());
            view.setResourceCode(info.getMenuPermissionId().toString());
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
}
