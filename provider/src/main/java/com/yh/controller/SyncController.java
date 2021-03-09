package com.yh.controller;

import com.yh.entity.*;
import com.yh.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    SingleQueryService singleQueryService;

    @Autowired
    MenuInfoService menuInfoService;

    @Autowired
    MenuPermissionService menuPermissionService;

    @Autowired
    RoleInfoService roleInfoService;

    @Autowired
    RelationRoleMenuPermissionService relationRoleMenuPermissionService;

    @Autowired
    RelationUserRoleService relationUserRoleService;

    @ApiOperation(value = "获取菜单的总条数",notes = "获取菜单的总条数",tags = {"SyncController"})
    @GetMapping("/findCountMenus")
    public List<Integer> findCountMenus(){
        return menuInfoService.findCountMenus();
    }

    @ApiOperation(value = "获取按钮的总条数",notes = "获取按钮的总条数",tags = {"SyncController"})
    @GetMapping("/findCountPermission")
    List<Integer> findCountPermission(){
       return menuPermissionService.findCountPermission();
    }

    @ApiOperation(value = "获取角色的总条数",notes = "获取角色的总条数",tags = {"SyncController"})
    @GetMapping("/findCountRoles")
    List<String> findCountRoles(){
        return roleInfoService.findCountRoles();
    }

    @ApiOperation(value = "根据条件获取id区间菜单集合",notes = "根据条件获取id区间菜单集合",tags = {"SyncController"})
    @GetMapping("/findMenuBetweenIds")
    List<MenuInfo> findMenuBetweenIds(@RequestParam("min")Integer min,@RequestParam("max")Integer max){
        return menuInfoService.findMenuBetweenIds(min,max);
    }

    @ApiOperation(value = "根据条件分页获取菜单集合",notes = "根据条件分页获取菜单集合",tags = {"SyncController"})
    @GetMapping("/findMenuPage")
    List<MenuInfo> findMenuPage(@RequestParam("offset")Integer offset,@RequestParam("num")Integer num){
        return menuInfoService.findMenuPage(offset,num);
    }

    //菜单
    @GetMapping("/list")
    public List<MenuInfo> findMenus(){
        return menuInfoService.findMenus();
    }

    //菜单单条记录
    @GetMapping("/findByAppCodeAndParentId")
    public MenuInfo findByAppCodeAndParentId(@RequestParam("code") String code,@RequestParam("parentId") String parentId){
        return singleQueryService.findByAppCodeAndParentId(code,parentId);
    }

    //按钮
    @GetMapping("/menuPermissions")
    public List<MenuPermission> menuPermissions(){
       return menuPermissionService.findMenuPermissions();
    }

    //查询按钮区间
    @GetMapping("/menuPermissionBetweenIds")
    List<MenuPermission> menuPermissionBetweenIds(@RequestParam("min")Integer min, @RequestParam("max")Integer max){
        return menuPermissionService.menuPermissionBetweenIds(min,max);
    }

    //根据MenuPermission的operation_objective_id{menu 的主键哦} 查找menu 信息
    @GetMapping("/findById")
    public MenuInfo findById(@RequestParam("id") String id){
        return singleQueryService.findById(id);
    }

    //角色
    @GetMapping("/roleInfos")
    public List<RoleInfo> roleInfos(){
        return roleInfoService.findRoleInfos();
    }

    //通过角色id倒推productCode
    @GetMapping("/findProductCodeByRoleId")
    public List<String> findProductCodeByRoleId(@RequestParam("roleId") String roleId){
        return relationRoleMenuPermissionService.findProductCodeByRoleId(roleId);
    }

    //角色资源同步
    @GetMapping("/relationRoleMenuPermissions")
    public List<RelationRoleMenuPermission> relationRoleMenuPermissions(@RequestParam("min")Integer min,@RequestParam("max")Integer max){
        return relationRoleMenuPermissionService.findRelationRoleMenuPermissions(min,max);
    }
    //获取角色资源总条数
    @GetMapping("/findCountRelationRoleMenuPermissions")
    public List<Integer> findCountRelationRoleMenuPermissions(){
        return relationRoleMenuPermissionService.findCountRelationRoleMenuPermissions();
    }

    //同步用户和角色
    @GetMapping("/relationUserRoles")
    public List<RelationUserRole> relationUserRoles(@RequestParam("min")Integer min,@RequestParam("max")Integer max){
        return relationUserRoleService.findRelationUserRoles(min,max);
    }

    //同步用户和角色
    @GetMapping("/findIds")
    public List<Integer> findIds(@RequestParam("min")Integer min,@RequestParam("max")Integer max){
        return relationUserRoleService.findIds(min,max);
    }

    //根据id 查询按钮信息
    @GetMapping("/findMenuPermissionId")
    public MenuPermission findMenuPermissionId(@RequestParam("id")String id){
      return singleQueryService.findByMenuPermissionId(id);
    }

    @GetMapping("/findRoleBetweenIds")
    List<RoleInfo> findRoleBetweenIds(@RequestParam("min")Integer min, @RequestParam("max")Integer max){
        return roleInfoService.findRoleBetweenIds(min,max);
    }

    @GetMapping("/findCountRelationUserRoles")
    List<Integer> findCountRelationUserRoles(){
        return relationUserRoleService.findCountRelationUserRoles();
    }


}
