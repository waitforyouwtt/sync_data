package com.yh.service;

import java.util.List;

public interface SingleUpdateService {

    void updateOrganizationProduct(List<Integer> ids,String tenantCode);

    void updateResources(List<Integer> ids, String tenantCode);

    void updateRoles(List<Integer> ids, String tenantCode);

    void updateRoleResources(List<Integer> ids, String tenantCode);

    void updateUserProducts(List<Integer> ids, String tenantCode);

    void updateDaDataAuthRules(List<Long> ids, String tenantCode);

    void updateDaDataAuths(List<Long> ids, String tenantCode);

    void updateDaDataRule(List<Long> ids, String tenantCode);

    void updateDaRoleDataAuth(List<Long> ids, String tenantCode);

    void updateDaRuleDetail(List<Long> ids, String tenantCode);

    void updateDaRuleProject(List<Long> ids, String tenantCode);
}
