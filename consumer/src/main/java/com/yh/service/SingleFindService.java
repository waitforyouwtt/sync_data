package com.yh.service;

import com.yh.entity.*;
import com.yh.view.Product;

import java.util.List;

public interface SingleFindService {

    AppProductResource resourceDetails(String productCode,String tenantCode,String resourceCode);

    AppProductRole roleDetails(String productCode,String tenantCode, String roleCode);

    AppRoleResource roleResourceDetails(String productCode,String tenantCode,String resourceCode,String roleCode);
    List<AppRoleResource> roleResources();

    List<AppProduct> findProductLists();
    List<AppProduct> findProductLists(List<String> productCodes);

    List<AppOrganizationProduct> findOrganizationProducts(List<String> productCodes);

    /**
     * 根据应用编码查询默认租户
     * @param productCode
     * @return
     */
    String findTenantCode(String productCode);
    List<AppTenantInfo> findTenantCodes(List<String> productCodes);
    List<AppTenantInfo> findTenants();



    /**
     * 根据应用编码查询资源信息
     * @param productCodes
     * @return
     */
    List<AppProductResource> findProductResources(List<String> productCodes);

    /**
     * 根据应用编码查询角色信息
     * @param productCodes
     * @return
     */
    List<AppProductRole> findProductRoles(List<String> productCodes);

    List<AppRoleResource> findRoleResources(List<String> productCodes);

    List<AppUserProduct> findUserProducts(List<String> productCodes);

    List<AppUserProduct> findUserProductList(String productCode);

    List<DaDataAuth> findDaDataAuths(List<String> productCodes);

    List<DaDataAuthRule> findDaDataAuthRules(List<String> productCodes);

    List<DaDataRule> findDaDataRule(List<String> productCodes);

    List<DaRoleDataAuth> findDaRoleDataAuths(List<String> productCodes);

    List<DaRuleDetail> findDaRuleDetails(List<String> productCodes);

    List<DaRuleProject> findDaRuleProjects(List<String> productCodes);

    UserInfo findUserInfoById(String id);

    List<String> findProductCodes(Long roleCode);

    List<AppProductRole> findProductCodes2(List<Long> roleCodes);

    AppUserRole roleUserDetails(String productCode,String tenantCode, String userCode, String roleCode);

    AppProduct findProductInfoCode(String businessType);

    List<UserBase> queryByUserId(long id);

    List<UserBase> queryByUserIds(List<Long> ids);

    List<AppUserRole> queryUserroles();

    List<AppProductRole> queryAllRoles();

    List<AppProductRole> queryAllRoles2();

    List<AppProductResource> queryAllResources();

    List<ConfigResource> findConfigResource();
}
