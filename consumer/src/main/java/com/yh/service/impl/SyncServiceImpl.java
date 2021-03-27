package com.yh.service.impl;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yh.dao.*;
import com.yh.entity.*;
import com.yh.feign.DataSynchronizeFeign;
import com.yh.service.SingleFindService;
import com.yh.service.SingleUpdateService;
import com.yh.service.SyncService;
import com.yh.service.ThreadService;
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

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SyncServiceImpl implements SyncService {

    @Autowired
    DataSynchronizeFeign productResourceFeign;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private SingleFindService singleFindService;

    @Resource
    AppProductResourceDao resourceDao;
    @Resource
    AppProductRoleDao productRoleDao;
    @Resource
    AppRoleResourceDao roleResourceDao;
    @Resource
    SingleUpdateService singleUpdateService;
    @Resource
    DaDataRuleDao daDataRuleDao;
    @Resource
    DaRoleDataAuthDao daRoleDataAuthDao;
    @Resource
    DaRuleDetailDao daRuleDetailDao;
    @Resource
    DaRuleProjectDao daRuleProjectDao;

    @Resource
    AppUserRoleDao appUserRoleDao;

    @Autowired
    ThreadService threadService;

    @Override
    public String menuSyncResource() {
        List<MenuInfo> menus = productResourceFeign.findMenus();
        log.info("需要同步菜单的总体条数:{}",menus.size());
       // SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
       // AppProductResourceDao resourceDao = session.getMapper(AppProductResourceDao.class);
        List<AppProductResource> menuInfos = convertMenuToResource(menus);

        int result = 0;
        for (int i = 0; i < menuInfos.size(); i++) {
            AppProductResource convert  = menuInfos.get(i);
            AppProductResource resource = singleFindService.resourceDetails(convert.getProductCode(),convert.getTenantCode(),convert.getResourceCode());
            if (!Objects.isNull(resource)){
                continue;
            }
            resourceDao.insert(menuInfos.get(i));
            result ++;
            /*if(i%1000==999){//每1000条提交一次防止内存溢出
                session.commit();
                session.clearCache();
            }*/
        }
       /* session.commit();
        session.clearCache();*/
        log.info("同步的结果条数：{}",result);
        return result > 1 ? "成功":"失败";
    }

    @Override
    public String menuPermissionSyncResource() {
        int result = 0;
        List<MenuPermission> menuPermissions = productResourceFeign.menuPermissions();
        log.info("需要同步菜单按钮的总体条数:{}",menuPermissions);
        List<List<MenuPermission>> data = Lists.partition(menuPermissions,100);
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);

        for (List<MenuPermission> entities : data){
            List<AppProductResource> resources = convertMenuPermissionToResource(entities);
            for (int i = 0; i < resources.size(); i++) {
                AppProductResource convert  = resources.get(i);
                AppProductResource resource = singleFindService.resourceDetails(convert.getProductCode(),convert.getTenantCode(),convert.getResourceCode());
                if (!Objects.isNull(resource)){
                    continue;
                }
                resourceDao.insert(resources.get(i));
                result ++;
                if(result == 100){//每100条提交一次防止内存溢出
                    session.commit();
                    session.clearCache();
                }
            }
        }
        session.commit();
        session.clearCache();
        log.info("同步的结果条数：{}",result);
        return result > 1 ? "成功":"失败";
    }

    @Override
    public String roleSync() {
        int result = 0;
        List<RoleInfo> roleInfos = productResourceFeign.roleInfos();
        log.info("需要同步角色的总体条数:{}",roleInfos);
        List<List<RoleInfo>> data = Lists.partition(roleInfos,100);
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);

        for (List<RoleInfo> entities : data){
            List<AppProductRole> roles = convertRole(entities);
            for (int i = 0; i < roles.size(); i++) {
                AppProductRole convert  = roles.get(i);
                String tenantCode = singleFindService.findTenantCode(convert.getProductCode());
                AppProductRole role = singleFindService.roleDetails(convert.getProductCode(),tenantCode,convert.getRoleCode());
                if (!Objects.isNull(role)){
                    continue;
                }
                log.info("插入角色的参数：{}",JSONUtil.toJsonStr(convert));
                List<AppProductRole> params = Arrays.asList(convert);
                productRoleDao.insertBatch(params);
                result ++;
                if(result == 100){//每100条提交一次防止内存溢出
                    session.commit();
                    session.clearCache();
                }
            }
        }

        session.commit();
        session.clearCache();
        log.info("同步的结果条数：{}",result);
        return result > 1 ? "成功":"失败";
    }

    @Override
    public String roleResource(Integer min,Integer max) {
        int result = 0;
        List<RelationRoleMenuPermission> relationRoleMenuPermissions = productResourceFeign.relationRoleMenuPermissions(min,max);
        log.info("需要同步角色资源的总体条数:{}",relationRoleMenuPermissions);
        List<List<RelationRoleMenuPermission>> data = Lists.partition(relationRoleMenuPermissions,100);
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);

        for (List<RelationRoleMenuPermission> entities : data){
            List<AppRoleResource> roleResources = convertRoleResource(entities);
            for (int i = 0; i < roleResources.size(); i++) {
                AppRoleResource convert  = roleResources.get(i);
                String tenantCode = singleFindService.findTenantCode(convert.getProductCode());
                AppRoleResource info = singleFindService.roleResourceDetails(convert.getProductCode(),tenantCode,convert.getResourceCode(),convert.getRoleCode());
                if (!Objects.isNull(info)){
                    continue;
                }
                List<AppRoleResource> params = Arrays.asList(convert);
                roleResourceDao.insertBatch(params);
                result ++;
                if(result == 100){//每100条提交一次防止内存溢出
                    session.commit();
                    session.clearCache();
                }
            }
        }

        session.commit();
        session.clearCache();
        log.info("同步的结果条数：{}",result);
        return result > 1 ? "成功":"失败";
    }

    @Override
    public String syncRelationUserRoles(Integer min, Integer max) {
        int result = 0;
        List<RelationUserRole> relationUserRoles = productResourceFeign.relationUserRoles(min, max);
        if (CollectionUtils.isEmpty(relationUserRoles)){
            return "此区间没有数据";
        }
        log.info("需要同步用户角色的总体条数:{}",relationUserRoles);
        List<List<RelationUserRole>> data = Lists.partition(relationUserRoles,100);
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);

        for (List<RelationUserRole> entities : data){
            List<AppUserRole> userRoles = convertRoleUser(entities);
            for (int i = 0; i < userRoles.size(); i++) {
                AppUserRole convert  = userRoles.get(i);
                AppUserRole info = singleFindService.roleUserDetails(convert.getProductCode(),convert.getTenantCode(),convert.getUserCode(),convert.getRoleCode());
                if (!Objects.isNull(info)){
                    continue;
                }
                List<AppUserRole> params = Arrays.asList(convert);
                appUserRoleDao.insertBatch(params);
                result ++;
                if(result == 100){//每100条提交一次防止内存溢出
                    session.commit();
                    session.clearCache();
                }
            }
        }

        session.commit();
        session.clearCache();
        log.info("同步的结果条数：{}",result);
        return result > 1 ? "成功":"失败";
    }

    @Async
    @Override
    public String syncRelationUserRoles3(Integer min, Integer max) {

        List<Integer> integers = productResourceFeign.findIds(min, max);
        if (CollectionUtils.isEmpty(integers)){
            return "此区间没有数据";
        }
        log.info("需要同步用户角色的总体条数:{}",integers);
        List<List<Integer>> partition = Lists.partition(integers, 1000);

        for (List<Integer> lists : partition){
            Integer start = Collections.min(lists);
            Integer end = Collections.max(lists);
            log.info("每段的开始值&结束值：{},{}",start,end);
            List<RelationUserRole> relationUserRoles = productResourceFeign.relationUserRoles(start, end);

            if (CollectionUtils.isEmpty(relationUserRoles)){
                return "此区间没有数据";
            }
            log.info("需要同步用户角色的总体条数:{}",relationUserRoles);
            List<List<RelationUserRole>> data = Lists.partition(relationUserRoles,100);
            SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
            int result =0;
            for (List<RelationUserRole> entities : data){
                List<AppUserRole> userRoles = convertRoleUser(entities);
                for (int i = 0; i < userRoles.size(); i++) {
                    AppUserRole convert  = userRoles.get(i);
                    AppUserRole info = singleFindService.roleUserDetails(convert.getProductCode(),convert.getTenantCode(),convert.getUserCode(),convert.getRoleCode());
                    if (!Objects.isNull(info)){
                        continue;
                    }
                    List<AppUserRole> params = Arrays.asList(convert);
                    appUserRoleDao.insertBatch(params);
                    result ++;
                    if(result == 100){//每100条提交一次防止内存溢出
                        session.commit();
                        session.clearCache();
                    }
                }
            }

            session.commit();
            session.clearCache();
            log.info("同步的结果条数：{}",result);
        }
        return "成功";
    }



    @Override
    public String syncTenant() {
        List<AppProduct> productLists = singleFindService.findProductLists();
        log.info("得到的需要同步的应用集合是：{}", JSONUtil.toJsonStr(productLists));
        if (CollectionUtils.isEmpty(productLists)){
            return null;
        }
        List<String> productCodes = productLists.stream().map(AppProduct::getProductCode).collect(Collectors.toList());
        /*List<Product> products = new ArrayList<>();
        productLists.stream().forEach(p->{
         Product info = new Product();
            BeanCopier beanCopier = BeanCopier.create(AppProduct.class,Product.class,false);
            beanCopier.copy(p,info,null);
            products.add(info);
        });*/

        //同步组织应用租户信息
        List<AppOrganizationProduct> organizationProducts = singleFindService.findOrganizationProducts(productCodes);
        if (!CollectionUtils.isEmpty(organizationProducts)){
            this.batchUpdateOrganizationProduct(organizationProducts);
        }
        //同步资源租户信息
        List<AppProductResource> productResources = singleFindService.findProductResources(productCodes);
        if (!CollectionUtils.isEmpty(productResources)){
            this.batchUpdateResource(productResources);
        }
        //同步角色租户信息
        List<AppProductRole> roles = singleFindService.findProductRoles(productCodes);
        if (!CollectionUtils.isEmpty(roles)){
            this.batchUpdateRole(roles);
        }
        //同步角色资源信息
        List<AppRoleResource> roleResources = singleFindService.findRoleResources(productCodes);
        if (!CollectionUtils.isEmpty(roleResources)){
            this.batchUpdateRoleResource(roleResources);
        }

        //同步用户应用信息[数据量太大，需要分开处理]
        syncUserProduct(productLists);
        //同步data
        List<DaDataAuth> dataAuths = singleFindService.findDaDataAuths(productCodes);
        if (!CollectionUtils.isEmpty(dataAuths)){
            this.updateDaDataAuth(dataAuths);
        }
        List<DaDataAuthRule> dataAuthRules = singleFindService.findDaDataAuthRules(productCodes);
        if (!CollectionUtils.isEmpty(dataAuthRules)){
            this.batchUpdateDaDataAuthRule(dataAuthRules);
        }
        List<DaDataRule> daDataRules = singleFindService.findDaDataRule(productCodes);
        if (!CollectionUtils.isEmpty(daDataRules)){
          this.batchUpdateDaDataRule(daDataRules);
        }
        List<DaRoleDataAuth> daRoleDataAuths = singleFindService.findDaRoleDataAuths(productCodes);
        if (!CollectionUtils.isEmpty(daRoleDataAuths)){
            this.batchUpdateDaRoleDataAuth(daRoleDataAuths);
        }
        List<DaRuleDetail> daRuleDetails = singleFindService.findDaRuleDetails(productCodes);
        if (!CollectionUtils.isEmpty(daRuleDetails)){
            this.batchUpdateDaRuleDetail(daRuleDetails);
        }
        List<DaRuleProject> daRuleProjects = singleFindService.findDaRuleProjects(productCodes);
        if (!CollectionUtils.isEmpty(daRuleProjects)){
            this.batchUpdateDaRuleProject(daRuleProjects);
        }

        return null;
    }

    private void batchUpdateDaRuleProject(List<DaRuleProject> daRuleProjects) {
        List<String> codes = null;
        Map<String, List<DaRuleProject>> groupMap = daRuleProjects.stream().collect(Collectors.groupingBy(DaRuleProject::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Long> ids = v.stream().map(DaRuleProject::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateDaRuleProject(ids,tenantCode);
            }
        });
    }

    private void batchUpdateDaRuleDetail(List<DaRuleDetail> daRuleDetails) {
        List<String> codes = null;
        Map<String, List<DaRuleDetail>> groupMap = daRuleDetails.stream().collect(Collectors.groupingBy(DaRuleDetail::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Long> ids = v.stream().map(DaRuleDetail::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateDaRuleDetail(ids,tenantCode);
            }
        });
    }

    private void batchUpdateDaRoleDataAuth(List<DaRoleDataAuth> daRoleDataAuths) {
        List<String> codes = null;
        Map<String, List<DaRoleDataAuth>> groupMap = daRoleDataAuths.stream().collect(Collectors.groupingBy(DaRoleDataAuth::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Long> ids = v.stream().map(DaRoleDataAuth::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateDaRoleDataAuth(ids,tenantCode);
            }
        });
    }

    private void batchUpdateDaDataRule(List<DaDataRule> daDataRules) {
        List<String> codes = null;
        Map<String, List<DaDataRule>> groupMap = daDataRules.stream().collect(Collectors.groupingBy(DaDataRule::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Long> ids = v.stream().map(DaDataRule::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateDaDataRule(ids,tenantCode);
            }
        });
    }

    private void updateDaDataAuth(List<DaDataAuth> dataAuths) {
        List<String> codes = null;
        Map<String, List<DaDataAuth>> groupMap = dataAuths.stream().collect(Collectors.groupingBy(DaDataAuth::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Long> ids = v.stream().map(DaDataAuth::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateDaDataAuths(ids,tenantCode);
            }
        });
    }

    private void batchUpdateDaDataAuthRule(List<DaDataAuthRule> dataAuthRules) {
        List<String> codes = null;
        Map<String, List<DaDataAuthRule>> groupMap = dataAuthRules.stream().collect(Collectors.groupingBy(DaDataAuthRule::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Long> ids = v.stream().map(DaDataAuthRule::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateDaDataAuthRules(ids,tenantCode);
            }
        });
    }

    private void syncUserProduct(List<AppProduct> productLists){
        Map<String, List<AppProduct>> groupMap = productLists.stream().collect(Collectors.groupingBy(AppProduct::getProductCode));
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<AppUserProduct> userProducts = singleFindService.findUserProductList(k);
            log.info("需要处理的数据的size:{}",userProducts.size());
            if (CollectionUtils.isEmpty(userProducts)){
                log.info("暂无数据执行");
            }else{
                List<List<AppUserProduct>> partitions = Lists.partition(userProducts,100);
                for (List<AppUserProduct> entities : partitions){
                    log.info("分批处理的size:{}",entities.size());
                    this.batchUpdateUserProducts(entities);
                }
            }
        });
    }

    private void batchUpdateUserProducts(List<AppUserProduct> userProducts) {
        Map<String, List<AppUserProduct>> groupMap = userProducts.stream().collect(Collectors.groupingBy(AppUserProduct::getProductCode));
        log.info("分组后的数据:{}",groupMap);
        List<String> codes = new ArrayList<>();
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Integer> ids = v.stream().map(AppUserProduct::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateUserProducts(ids,tenantCode);
            }
        });
        log.info("未同步的应用：{}",codes);
    }

    private void batchUpdateRoleResource(List<AppRoleResource> roleResources) {
        Map<String, List<AppRoleResource>> groupMap = roleResources.stream().collect(Collectors.groupingBy(AppRoleResource::getProductCode));
        log.info("分组后的数据:{}",groupMap);
        List<String> codes = new ArrayList<>();
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Integer> ids = v.stream().map(AppRoleResource::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateRoleResources(ids,tenantCode);
            }
        });
        log.info("未同步的应用：{}",codes);
    }

    private void batchUpdateRole(List<AppProductRole> roles) {
        Map<String, List<AppProductRole>> groupMap = roles.stream().collect(Collectors.groupingBy(AppProductRole::getProductCode));
        log.info("分组后的数据：{}",groupMap);
        List<String> codes = new ArrayList<>();
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Integer> ids = v.stream().map(AppProductRole::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateRoles(ids,tenantCode);
            }
        });
        log.info("未同步的应用：{}",codes);
    }

    private void batchUpdateResource(List<AppProductResource> resources) {
        Map<String, List<AppProductResource>> groupMap = resources.stream().collect(Collectors.groupingBy(AppProductResource::getProductCode));
        log.info("分组后的数据:{}",groupMap);
        List<String> codes = new ArrayList<>();
        groupMap.forEach((k,v)->{
            log.info("得到的key:{}",k);
            List<Integer> ids = v.stream().map(AppProductResource::getId).collect(Collectors.toList());
            log.info("得到的value:{}",ids);
            String tenantCode = singleFindService.findTenantCode(k);
            if (StringUtils.isBlank(tenantCode)){
                codes.add(k);
            }else{
                singleUpdateService.updateResources(ids,tenantCode);
            }
        });
        log.info("未同步的应用：{}",codes);
    }

    private void batchUpdateOrganizationProduct(List<AppOrganizationProduct> organizationProducts) {
        Map<String,List<AppOrganizationProduct>> groupMap = organizationProducts.stream().
                collect(Collectors.groupingBy(AppOrganizationProduct::getProductCode));
        log.info("分组后的数据：{}" , groupMap);
        List<String> codes = new ArrayList<>();
        groupMap.forEach((k,v)->{
           log.info("得到的key:{}",k);
           List<Integer> ids = v.stream().map(AppOrganizationProduct::getId).collect(Collectors.toList());
           log.info("得到的value:{}",ids);
           String tenantCode = singleFindService.findTenantCode(k);
           if (StringUtils.isBlank(tenantCode)){
               codes.add(k);
           }else{
               singleUpdateService.updateOrganizationProduct(ids,tenantCode);
           }
        });
        log.info("未同步的应用：{}",codes);
    }


    private List<AppProductResource> convertMenuToResource(List<MenuInfo> menus){
        List<AppProductResource> resources = new ArrayList<>();
        for (MenuInfo info: menus){
            AppProductResource resource = new AppProductResource();
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setId(100000+info.getId());
            resource.setProductCode(info.getBusinessType());
            resource.setTenantCode(singleFindService.findTenantCode(info.getBusinessType()));

            /**
             * select code from xxx where parendid = ‘aaa’
             *     Code —> parentCode
             */
           /* List<MenuInfo> queryMenus = productResourceFeign.findByAppCodeAndParentId(info.getBusinessType(), info.getParentId() + "");
            if (Objects.isNull(queryMenus)){
                resource.setParentCode("0");
            }else{
                resource.setParentCode(queryMenu.getId().toString());
            }*/
            if (info.getParentId() == null || info.getParentId() ==0){
                resource.setParentCode("0");
            }else{
                resource.setParentCode(String.valueOf(info.getParentId()));
            }
            resource.setResourceCode(info.getCode());
            resource.setResourceName(info.getName());
            resource.setPath(info.getDisplayUrl());
            //固定为菜单
            resource.setType("1");
            resource.setOrderNum(info.getRank());
            resource.setCreatedTime(new Date());
            resource.setUpdatedTime(new Date());
            resource.setCreatedBy(info.getCreatedBy());
            resource.setUpdatedBy(info.getUpdatedBy());
            if (info.getStatus() == 0){
                resource.setStatus("Y");
            }else{
                resource.setStatus("N");
            }
            resource.setIsDelete(info.getIsDelete());
            resource.setPlatform("purchase");
            resources.add(resource);
        }
        return resources;
    }

    private List<AppProductResource> convertMenuPermissionToResource(List<MenuPermission> permissions){
        List<AppProductResource> resources = new ArrayList<>();
        for (MenuPermission info: permissions){
            AppProductResource resource = new AppProductResource();
            resource.setUniqueCode(StringCustomizedUtils.uniqueCode());
            resource.setId(100000+info.getId());
            resource.setProductCode(info.getBusinessType());
            resource.setTenantCode(singleFindService.findTenantCode(info.getBusinessType()));
            /**
             * operation_objective_id
             * select * from menu_info where id = '26';
             */
            MenuInfo menu = productResourceFeign.findById(info.getOperationObjectiveId().toString());
            if (Objects.isNull(menu)){
                resource.setParentCode("0");
            }else{
                resource.setParentCode(menu.getId().toString());
            }
            resource.setResourceCode(info.getPermissionCode());
            resource.setResourceName(info.getPermissionName());
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
            resource.setStatus("Y");
            resource.setIsDelete(info.getIsDelete());
            resource.setPlatform("purchase");
            resources.add(resource);
        }
        return resources;
    }

    public List<AppProductRole> convertRole(List<RoleInfo> roleInfos){
        List<AppProductRole> roles = new ArrayList<>();
        for (RoleInfo  info : roleInfos){
            List<String> productCodes = productResourceFeign.findProductCodeByRoleId(info.getId().toString());
            if (!CollectionUtils.isEmpty(productCodes)){
                for (String code : productCodes){
                    AppProductRole role = new AppProductRole();
                    role.setRoleCode(info.getId().toString());
                    role.setDescription(info.getRoleTypeCode());
                    role.setOutRoleCode(info.getRoleCode());
                    role.setRoleName(info.getRoleName());
                    role.setUniqueCode(StringCustomizedUtils.uniqueCode());
                    role.setProductCode(code);
                    role.setTenantCode(singleFindService.findTenantCode(info.getBusinessType()));
                    role.setRoleStatus("Y");
                    role.setPlatform("purchase");
                    role.setIsDelete(info.getIsDelete());
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
        List<AppRoleResource> roleResources = new ArrayList<>();
        for (RelationRoleMenuPermission  info : roleMenuPermissions){
            AppRoleResource view = new AppRoleResource();
            view.setRoleCode(info.getRoleId().toString());
            MenuPermission menuPermission = productResourceFeign.findMenuPermissionId(info.getMenuPermissionId().toString());
            if (Objects.isNull(menuPermission)){
                continue;
            }
            view.setProductCode(menuPermission.getBusinessType());
            view.setProductName(menuPermission.getBusinessType());
            view.setResourceCode(menuPermission.getPermissionCode());
            view.setResourceName(menuPermission.getPermissionName());
            view.setTenantCode(singleFindService.findTenantCode(menuPermission.getBusinessType()));
            view.setPlatform("purchase");
            view.setIsDelete(info.getIsDelete());
            view.setCreatedBy("admin");
            view.setUpdatedBy("admin");
            view.setCreatedTime(new Date());
            view.setUpdatedTime(new Date());
            roleResources.add(view);
        }
        return roleResources;
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
                view.setCreatedBy("admin");
                view.setUpdatedBy("admin");
                view.setCreatedTime(new Date());
                view.setUpdatedTime(new Date());
                roleResources.add(view);
            }
        }
        return roleResources;
    }
}
