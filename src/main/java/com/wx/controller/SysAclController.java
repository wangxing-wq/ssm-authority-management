package com.wx.controller;

import com.google.common.collect.Maps;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.entity.SysRole;
import com.wx.domain.param.AclParam;
import com.wx.service.SysAclService;
import com.wx.service.SysRoleService;
import com.wx.util.ValidateUtil;
import com.wx.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 22343
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

    @Resource
    private SysAclService sysAclService;
    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("/save.json")
    @ResponseBody
    public Result saveAclModule(AclParam param) {
        ValidateUtil.check(param);
        sysAclService.save(param);
        return Result.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public Result updateAclModule(AclParam param) {
        sysAclService.update(param);
        return Result.success();
    }

    @GetMapping("/page.json")
    @ResponseBody
    public Result list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return Result.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }

    @GetMapping("aclList.json")
    @ResponseBody
    public Result acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return Result.success(map);
    }
}
