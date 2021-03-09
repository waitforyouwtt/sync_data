package com.yh.service;

import com.yh.entity.*;

import java.util.List;

public interface SingleFindService {

    AppProductResource resourceDetails(String productCode,String tenantCode,String resourceCode);

    AppProductRole roleDetails(String productCode,String tenantCode, String roleCode);

    AppRoleResource roleResourceDetails(String productCode,String tenantCode,String resourceCode,String roleCode);

    List<AppProduct> findProductLists();

    List<AppOrganizationProduct> findOrganizationProducts(List<String> productCodes);

    /**
     * 根据应用编码查询默认租户
     * @param productCode
     * @return
     */
    String findTenantCode(String productCode);

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

    AppUserRole roleUserDetails(String productCode,String tenantCode, String userCode, String roleCode);
}
