package com.yh.feign;

import com.yh.entity.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "provider", url = "http://127.0.0.1:8002/sync")
public interface DataSynchronizeFeign {

    @ApiOperation(value = "获取菜单的总条数",notes = "获取菜单的总条数")
    @GetMapping("/findCountMenus")
    List<Integer> findCountMenus();

    @ApiOperation(value = "根据条件获取id区间菜单集合",notes = "根据条件获取id区间菜单集合")
    @GetMapping("/findMenuBetweenIds")
    List<MenuInfo> findMenuBetweenIds(@RequestParam("min")Integer min,@RequestParam("max")Integer max);

    @ApiOperation(value = "根据应用编码&父级id查询菜单",notes = "根据父级id查询菜单")
    @GetMapping("/findByAppCodeAndParentId")
    List<MenuInfo> findByAppCodeAndParentId(@RequestParam("code") String code, @RequestParam("parentId") String parentId);

    @ApiOperation(value = "获取按钮的总条数",notes = "获取按钮的总条数")
    @GetMapping("/findCountPermission")
    List<Integer> findCountPermission();

    @ApiOperation(value = "根据条件获取id区间按钮集合",notes = "根据条件获取id区间按钮集合")
    @GetMapping("/menuPermissionBetweenIds")
    List<MenuPermission> menuPermissionBetweenIds(@RequestParam("min")Integer min, @RequestParam("max")Integer max);

    @ApiOperation(value = "按照operation_objective_id查找父级菜单",notes = "按照operation_objective_id查找父级菜单")
    @GetMapping("/findById")
    MenuInfo findById(@RequestParam("id") String id);

    @ApiOperation(value = "获取角色的总条数",notes = "获取角色的总条数")
    @GetMapping("/findCountRoles")
    List<String> findCountRoles();

    @ApiOperation(value = "根据条件获取id区间角色集合",notes = "根据条件获取id区间角色集合")
    @GetMapping("/findRoleBetweenIds")
    List<RoleInfo> findRoleBetweenIds(@RequestParam("min")Integer min, @RequestParam("max")Integer max);

    @ApiOperation(value = "通过角色id倒推productCode",notes = "通过角色id倒推productCode")
    @GetMapping("/findProductCodeByRoleId")
    List<String> findProductCodeByRoleId(@RequestParam("roleId") String roleId);

    @ApiOperation(value = "获取角色资源总条数",notes = "获取角色资源总条数")
    @GetMapping("/findCountRelationRoleMenuPermissions")
    List<Integer> findCountRelationRoleMenuPermissions();

    @ApiOperation(value = "根据条件获取id区间角色资源集合",notes = "根据条件获取id区间角色资源集合")
    @GetMapping("/relationRoleMenuPermissions")
    List<RelationRoleMenuPermission> relationRoleMenuPermissions(@RequestParam("min")Integer min,@RequestParam("max")Integer max);

    @ApiOperation(value = "获取用户角色总条数",notes = "获取用户角色总条数")
    @GetMapping("/findCountRelationUserRoles")
    List<Integer> findCountRelationUserRoles();

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

    //同步用户和角色
    @GetMapping("/relationUserRoles")
    List<RelationUserRole> relationUserRoles(@RequestParam("min")Integer min, @RequestParam("max")Integer max);

    @GetMapping("/findMenuPermissionId")
    MenuPermission findMenuPermissionId(@RequestParam("id")String id);

    @GetMapping("/findIds")
    List<Integer> findIds(@RequestParam("min")Integer min,@RequestParam("max")Integer max);
}
