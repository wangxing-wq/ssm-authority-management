package com.wx.controller;

import com.google.common.collect.Maps;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.beans.PageResult;
import com.wx.domain.entity.SysUser;
import com.wx.domain.param.UserParam;
import com.wx.service.SysRoleService;
import com.wx.service.SysTreeService;
import com.wx.service.SysUserService;
import com.wx.web.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 22343
 *
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }

    @PostMapping("/save.json")
    @ResponseBody
    public Result saveUser(UserParam param) {
        sysUserService.save(param);
        return Result.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public Result updateUser(UserParam param) {
        sysUserService.update(param);
        return Result.success();
    }
    
    @PostMapping("/delete.json")
    @ResponseBody
    public Result delete(List<Integer> idList) {
        // TODO: 2022/12/3 暂时不实现,需求,当下面存在用户的时候不能删除,超级管理员拥有一键删除功能
        sysUserService.deleteByIdList(idList);
        return Result.success();
    }

    @GetMapping("/page.json")
    @ResponseBody
    public Result page(@RequestParam("deptId") int deptId, PageQuery pageQuery) {
        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return Result.success(result);
    }

    @GetMapping("/aclList.json")
    @ResponseBody
    public Result aclList(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return Result.success(map);
    }
}
