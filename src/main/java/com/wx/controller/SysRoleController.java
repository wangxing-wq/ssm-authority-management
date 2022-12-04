package com.wx.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wx.domain.entity.SysUser;
import com.wx.domain.param.RoleParam;
import com.wx.service.*;
import com.wx.util.StringUtil;
import com.wx.util.ValidateUtil;
import com.wx.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 22343
 */
@Slf4j
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleAclService sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService sysUserService;

    @GetMapping("role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    @PostMapping("/save.json")
    @ResponseBody
    public Result saveRole(RoleParam param) {
        ValidateUtil.check(param);
        sysRoleService.save(param);
        return Result.success();
    }

    @RequestMapping("/update.json")
    @PostMapping
    public Result updateRole(RoleParam param) {
        sysRoleService.update(param);
        return Result.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public Result list() {
        return Result.success(sysRoleService.getAll());
    }

    @GetMapping("/roleTree.json")
    @ResponseBody
    public Result roleTree(@RequestParam("roleId") int roleId) {
        return Result.success(sysTreeService.roleTree(roleId));
    }

    @GetMapping("/changeAcls.json")
    @ResponseBody
    public Result changeAclList(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false,
            defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAclList(roleId, aclIdList);
        return Result.success();
    }

    @GetMapping("/changeUsers.json")
    @ResponseBody
    public Result changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return Result.success();
    }

    @GetMapping("/users.json")
    @ResponseBody
    public Result users(@RequestParam("roleId") int roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.findUserList();
        List<SysUser> unselectedUserList = Lists.newArrayList();
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
        for(SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return Result.success(map);
    }
}
