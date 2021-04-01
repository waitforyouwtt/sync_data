package com.yh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yh.dao.*;
import com.yh.entity.*;
import com.yh.service.SingleFindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SingleFindServiceImpl implements SingleFindService {

    @Resource
    AppProductResourceDao resourceDao;
    @Resource
    AppRoleResourceDao roleResourceDao;
    @Resource
    AppProductDao productDao;
    @Resource
    AppOrganizationProductDao organizationProductDao;
    @Resource
    AppTenantInfoDao tenantInfoDao;
    @Resource
    AppProductResourceDao productResourceDao;
    @Resource
    AppProductRoleDao productRoleDao;
    @Resource
    AppUserProductDao userProductDao;
    @Resource
    DaDataAuthDao daDataAuthDao;
    @Resource
    DaDataAuthRuleDao daDataAuthRuleDao;
    @Resource
    DaDataRuleDao daDataRuleDao;
    @Resource
    DaRoleDataAuthDao daRoleDataAuthDao;
    @Resource
    DaRuleDetailDao daRuleDetailDao;
    @Resource
    DaRuleProjectDao daRuleProjectDao;
    @Resource
    UserInfoDao userInfoDao;
    @Resource
    AppUserRoleDao userRoleDao;
    @Autowired
    UserBaseDao userBaseDao;

    @Override
    public AppProductResource resourceDetails(String productCode,String tenantCode, String resourceCode) {
        log.info("查询参数：{},{},{}",productCode,resourceCode,tenantCode);
        QueryWrapper<AppProductResource> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("tenant_code",tenantCode);
        wrapper.eq("resource_code",resourceCode);
        wrapper.eq("is_delete",0);
        List<AppProductResource> productResources = resourceDao.selectOne2(productCode, tenantCode, resourceCode);
        if (!CollectionUtils.isEmpty(productResources)){
            return productResources.get(0);
        }
        return null;
    }

    @Override
    public AppProductRole roleDetails(String productCode,String tenantCode, String roleCode) {
        QueryWrapper<AppProductRole> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("tenant_code", tenantCode);
        wrapper.eq("role_code",roleCode);
        wrapper.eq("is_delete",0);
        return productRoleDao.selectOne(wrapper);
    }

    @Override
    public AppRoleResource roleResourceDetails(String productCode,String tenantCode, String resourceCode, String roleCode) {
        QueryWrapper<AppRoleResource> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("tenant_code",tenantCode);
        wrapper.eq("resource_code",resourceCode);
        wrapper.eq("role_code",roleCode);
        wrapper.eq("is_delete",0);
        return roleResourceDao.selectOne(wrapper);
    }

    @Override
    public List<AppProduct> findProductLists() {
        QueryWrapper<AppProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("protocol",2);
        wrapper.ge("id",243);
        wrapper.le("id",999);
        return productDao.selectList(wrapper);
    }

    @Override
    public List<AppOrganizationProduct> findOrganizationProducts(List<String> productCodes) {
        QueryWrapper<AppOrganizationProduct> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return organizationProductDao.selectList(wrapper);
    }

    @Override
    public String findTenantCode(String productCode) {
        QueryWrapper<AppTenantInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("is_delete",0);
        AppTenantInfo result  = tenantInfoDao.selectOne(wrapper);
        if (Objects.isNull(result)){
            return "default";
        }
        return result.getCode();
    }

    public List<AppTenantInfo> findTenantCodes(List<String> productCodes){
        QueryWrapper<AppTenantInfo> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        List<AppTenantInfo> results  = tenantInfoDao.selectList(wrapper);
        return results;
    }

    @Override
    public List<AppProductResource> findProductResources(List<String> productCodes) {
        QueryWrapper<AppProductResource> wrapper = new QueryWrapper<>();
        wrapper.in("product_code","103");
        wrapper.eq("tenant_code","xxx");
        wrapper.eq("is_delete",0);
        return productResourceDao.selectList(wrapper);
    }

    @Override
    public List<AppProductRole> findProductRoles(List<String> productCodes) {
        QueryWrapper<AppProductRole> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("tenant_code","xxx");
        wrapper.eq("is_delete",0);
        return productRoleDao.selectList(wrapper);
    }

    @Override
    public List<AppRoleResource> findRoleResources(List<String> productCodes) {
        QueryWrapper<AppRoleResource> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("tenant_code","xxx");
        wrapper.eq("is_delete",0);
        return roleResourceDao.selectList(wrapper);
    }

    @Override
    public List<AppUserProduct> findUserProducts(List<String> productCodes) {
        QueryWrapper<AppUserProduct> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return userProductDao.selectList(wrapper);
    }

    @Override
    public List<AppUserProduct> findUserProductList(String productCode){
        QueryWrapper<AppUserProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("is_delete",0);
        return userProductDao.selectList(wrapper);
    }

    @Override
    public List<DaDataAuth> findDaDataAuths(List<String> productCodes) {
        QueryWrapper<DaDataAuth> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return daDataAuthDao.selectList(wrapper);
    }

    @Override
    public List<DaDataAuthRule> findDaDataAuthRules(List<String> productCodes) {
        QueryWrapper<DaDataAuthRule> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return daDataAuthRuleDao.selectList(wrapper);
    }

    @Override
    public List<DaDataRule> findDaDataRule(List<String> productCodes) {
        QueryWrapper<DaDataRule> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return daDataRuleDao.selectList(wrapper);
    }

    @Override
    public List<DaRoleDataAuth> findDaRoleDataAuths(List<String> productCodes) {
        QueryWrapper<DaRoleDataAuth> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return daRoleDataAuthDao.selectList(wrapper);
    }

    @Override
    public List<DaRuleDetail> findDaRuleDetails(List<String> productCodes) {
        QueryWrapper<DaRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return daRuleDetailDao.selectList(wrapper);
    }

    @Override
    public List<DaRuleProject> findDaRuleProjects(List<String> productCodes) {
        QueryWrapper<DaRuleProject> wrapper = new QueryWrapper<>();
        wrapper.in("product_code",productCodes);
        wrapper.eq("is_delete",0);
        return daRuleProjectDao.selectList(wrapper);
    }

    @Override
    public UserInfo findUserInfoById(String id) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        wrapper.eq("is_delete",0);
        return userInfoDao.selectOne(wrapper);
    }

    @Override
    public List<String> findProductCodes(Long roleCode) {
        QueryWrapper<AppProductRole> wrapper = new QueryWrapper();
        wrapper.eq("role_code",roleCode);
        wrapper.eq("is_delete",0);
        List<AppProductRole> roles = productRoleDao.selectList(wrapper);
        if (CollectionUtils.isEmpty(roles)){
            return new ArrayList<>();
        }
        List<String> productCodes = roles.stream().map(AppProductRole::getProductCode).distinct().collect(Collectors.toList());
        return productCodes;
    }

    @Override
    public AppUserRole roleUserDetails(String productCode,String tenantCode, String userCode, String roleCode) {
        QueryWrapper<AppUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("tenant_code",tenantCode);
        wrapper.eq("user_code",userCode);
        wrapper.eq("is_delete",0);
        return userRoleDao.selectOne(wrapper);
    }

    @Override
    public AppProduct findProductInfoCode(String businessType){
        QueryWrapper<AppProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("product_code",businessType);
        wrapper.eq("is_delete",0);
        return productDao.selectOne(wrapper);
    }

    @Override
    public List<UserBase> queryByUserId(long id){
        QueryWrapper<UserBase> wrapper = new QueryWrapper<>();
        wrapper.eq("user_info_id",id);
        return userBaseDao.selectList(wrapper);
    }
}
