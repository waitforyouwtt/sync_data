package com.yh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yh.dao.*;
import com.yh.entity.*;
import com.yh.service.SingleUpdateService;
import com.yh.utils.StringCustomizedUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SingleUpdateServiceImpl implements SingleUpdateService {

    @Resource
    AppOrganizationProductDao organizationProductDao;
    @Resource
    AppProductResourceDao productResourceDao;
    @Resource
    AppProductRoleDao productRoleDao;
    @Resource
    AppRoleResourceDao roleResourceDao;
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

    @Override
    public void updateOrganizationProduct(List<Integer> ids,String tenantCode) {
        log.info("updateOrganizationProduct批量修改租户编码:{},{}",ids,tenantCode);
        QueryWrapper<AppOrganizationProduct> wrapper = new QueryWrapper<>();
        wrapper.in("id",ids);
        AppOrganizationProduct info = new AppOrganizationProduct();
        info.setTenantCode(tenantCode);
        info.setUpdatedTime(new Date());
        organizationProductDao.update(info,wrapper);
    }

    @Override
    public void updateResources(List<Integer> ids, String tenantCode) {
        log.info("updateResources批量修改租户编码:{},{}",ids,tenantCode);
        /*QueryWrapper<AppProductResource>  wrapper = new QueryWrapper<>();
        wrapper.in("id",ids);*/
        for (Integer id: ids){
            AppProductResource info = new AppProductResource();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            productResourceDao.updateById(info);
        }


    }

    @Override
    public void updateRoles(List<Integer> ids, String tenantCode) {
        log.info("updateRoles批量修改租户编码：{},{}",ids,tenantCode);
        for (Integer id :ids){
            AppProductRole info = new AppProductRole();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUniqueCode(StringCustomizedUtils.uniqueCode());
            info.setUpdatedTime(new Date());
            productRoleDao.updateById(info);
        }
    }

    @Override
    public void updateRoleResources(List<Integer> ids, String tenantCode) {
        log.info("updateRoleResources批量修改租户编码：{},{}",ids,tenantCode);
        for (Integer id : ids){
            AppRoleResource info = new AppRoleResource();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            roleResourceDao.updateById(info);
        }
    }

    @Override
    public void updateUserProducts(List<Integer> ids, String tenantCode) {
        log.info("updateUserProducts批量修改租户编码：{},{}",ids,tenantCode);
        QueryWrapper<AppUserProduct> wrapper = new QueryWrapper<>();
        wrapper.in("id",ids);
        AppUserProduct info = new AppUserProduct();
        info.setTenantCode(tenantCode);
        info.setUpdatedTime(new Date());
        userProductDao.update(info,wrapper);
    }

    @Override
    public void updateDaDataAuthRules(List<Long> ids, String tenantCode) {
        log.info("updateDaDataAuthRules批量修改租户编码：{},{}",ids,tenantCode);
        for (Long id : ids){
            DaDataAuthRule info = new DaDataAuthRule();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            daDataAuthRuleDao.updateById(info);
        }
    }

    @Override
    public void updateDaDataAuths(List<Long> ids, String tenantCode) {
        log.info("updateDaDataAuths批量修改租户编码：{},{}",ids,tenantCode);
        for (Long id: ids){
            DaDataAuth info = new DaDataAuth();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            daDataAuthDao.updateById(info);
        }
    }

    @Override
    public void updateDaDataRule(List<Long> ids, String tenantCode) {
        log.info("updateDaDataRule批量修改租户编码：{},{}",ids,tenantCode);
        for (Long id: ids){
            DaDataRule info = new DaDataRule();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            daDataRuleDao.updateById(info);
        }
    }

    @Override
    public void updateDaRoleDataAuth(List<Long> ids, String tenantCode) {
        log.info("updateDaRoleDataAuth批量修改租户编码：{},{}",ids,tenantCode);
        for (Long id: ids){
            DaRoleDataAuth info = new DaRoleDataAuth();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            daRoleDataAuthDao.updateById(info);
        }
    }

    @Override
    public void updateDaRuleDetail(List<Long> ids, String tenantCode) {
        log.info("updateDaRuleDetail批量修改租户编码：{},{}",ids,tenantCode);
        for (Long id: ids){
            DaRuleDetail info = new DaRuleDetail();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            daRuleDetailDao.updateById(info);
        }
    }

    @Override
    public void updateDaRuleProject(List<Long> ids, String tenantCode) {
        log.info("updateDaRuleProject批量修改租户编码：{},{}",ids,tenantCode);
        for (Long id: ids){
            DaRuleProject info = new DaRuleProject();
            info.setId(id);
            info.setTenantCode(tenantCode);
            info.setUpdatedTime(new Date());
            daRuleProjectDao.updateById(info);
        }
    }
}
