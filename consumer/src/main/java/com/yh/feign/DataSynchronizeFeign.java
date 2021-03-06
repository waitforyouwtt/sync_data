package com.yh.feign;

import com.yh.entity.*;
import com.yh.view.ProductRoleVO;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;

@FeignClient(name = "provider", url = "http://127.0.0.1:8002/sync")
public interface DataSynchronizeFeign {

    @ApiOperation(value = "获取菜单的总条数",notes = "获取菜单的总条数")
    @GetMapping("/findCountMenus")
    List<Long> findCountMenus();

    @ApiOperation(value = "根据条件获取id区间菜单集合",notes = "根据条件获取id区间菜单集合")
    @GetMapping("/findMenuBetweenIds")
    List<MenuInfo> findMenuBetweenIds(@RequestParam("min")Long min,@RequestParam("max")Long max);

    @ApiOperation(value = "根据应用编码&父级id查询菜单",notes = "根据父级id查询菜单")
    @GetMapping("/findByAppCodeAndParentId")
    List<MenuInfo> findByAppCodeAndParentId(@RequestParam("code") String code, @RequestParam("parentId") String parentId);

    @ApiOperation(value = "获取按钮的总条数",notes = "获取按钮的总条数")
    @GetMapping("/findCountPermission")
    List<Long> findCountPermission();

    @ApiOperation(value = "根据条件获取id区间按钮集合",notes = "根据条件获取id区间按钮集合")
    @GetMapping("/menuPermissionBetweenIds")
    List<MenuPermission> menuPermissionBetweenIds(@RequestParam("min")Long min, @RequestParam("max")Long max);

    @ApiOperation(value = "按照operation_objective_id查找父级菜单",notes = "按照operation_objective_id查找父级菜单")
    @GetMapping("/findById")
    MenuInfo findById(@RequestParam("id") String id);

    @ApiOperation(value = "按照operation_objective_id查找父级菜单",notes = "按照operation_objective_id查找父级菜单")
    @GetMapping("/findMenuByIds")
    List<MenuInfo> findMenuByIds(@RequestParam("ids") String ids);

    @ApiOperation(value = "获取角色的总条数",notes = "获取角色的总条数")
    @GetMapping("/findCountRoles")
    List<Long> findCountRoles();

    @ApiOperation(value = "获取角色按应用拆分后的数据",notes = "获取角色按应用拆分后的数据")
    @GetMapping("/findRoleSplitByApplications")
    List<RoleSplitByApplication> findRoleSplitByApplications();

    @ApiOperation(value = "同步丢弃但已经绑定用户的角色",notes = "同步丢弃但已经绑定用户的角色")
    @GetMapping("/syncAbandonList")
    List<Role> syncAbandonList();

    @ApiOperation(value = "根据条件获取id区间角色集合",notes = "根据条件获取id区间角色集合")
    @GetMapping("/findRoleBetweenIds")
    List<RoleInfo> findRoleBetweenIds(@RequestParam("min")Long min, @RequestParam("max")Long max);

    @ApiOperation(value = "通过角色id倒推productCode",notes = "通过角色id倒推productCode")
    @GetMapping("/findProductCodeByRoleId")
    List<String> findProductCodeByRoleId(@RequestParam("roleId") String roleId);

    @ApiOperation(value = "通过角色id倒推productCode",notes = "通过角色id倒推productCode")
    @GetMapping("/findProductCodeByRoleIds")
    List<ProductRoleVO> findProductCodeByRoleIds(@RequestParam("roleIds") String roleIds);

    @ApiOperation(value = "获取角色资源总条数",notes = "获取角色资源总条数")
    @GetMapping("/findCountRelationRoleMenuPermissions")
    List<Long> findCountRelationRoleMenuPermissions();

    @ApiOperation(value = "根据条件获取id区间角色资源集合",notes = "根据条件获取id区间角色资源集合")
    @GetMapping("/relationRoleMenuPermissions")
    List<RelationRoleMenuPermission> relationRoleMenuPermissions(@RequestParam("min")Long min,@RequestParam("max")Long max);

    @ApiOperation(value = "根据条件获取id区间角色资源集合",notes = "根据条件获取id区间角色资源集合")
    @GetMapping("/relationRoleMenuPermissions2")
    List<RelationRoleMenuPermission> relationRoleMenuPermissions2();

    @ApiOperation(value = "根据permissionId获取菜单信息",notes = "根据permissionId获取菜单信息")
    @GetMapping("/findMenuPermissionId")
    MenuPermission findMenuPermissionId(@RequestParam("id")String id);

    @ApiOperation(value = "根据permissionIds获取菜单信息",notes = "根据permissionIds获取菜单信息")
    @GetMapping("/findMenuPermissionIds")
    List<MenuPermission> findMenuPermissionIds(@RequestParam("ids")String ids);

    @GetMapping("/queryByRoleId")
    List<RelationRoleMenuPermission> queryByRoleId(@RequestParam("roleId") Integer roleId);


    @ApiOperation(value = "获取用户角色总条数",notes = "获取用户角色总条数")
    @GetMapping("/findCountRelationUserRoles")
    List<Long> findCountRelationUserRoles();

    @ApiOperation(value = "根据条件获取id区间用户角色集合",notes = "根据条件获取id区间用户角色集合")
    @GetMapping("/relationUserRoles")
    List<RelationUserRole> relationUserRoles(@RequestParam("min")Long min, @RequestParam("max")Long max);

    //根据条件分页获取菜单集合
    @GetMapping("/findMenuPage")
    List<MenuInfo> findMenuPage(@RequestParam("offset")Integer offset,@RequestParam("num")Integer num);

    //菜单
    @GetMapping("/list")
    List<MenuInfo> findMenus();

    //按钮
    @GetMapping("/menuPermissions")
    List<MenuPermission> menuPermissions();

    //角色
    @GetMapping("/roleInfos")
    List<RoleInfo> roleInfos();

    @GetMapping("/findIds")
    List<Long> findIds(@RequestParam("min")Long min,@RequestParam("max")Long max);

    @GetMapping("/queryByRoleAndBy")
    public List<RoleBusinessType> queryByRoleAndBy();
}
