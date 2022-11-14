package com.wx.controller;

import com.google.common.collect.Maps;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.beans.PageResult;
import com.wx.domain.entity.SysUser;
import com.wx.domain.param.UserParam;
import com.wx.service.SysRoleService;
import com.wx.service.SysTreeService;
import com.wx.service.SysUserService;
import com.wx.domain.beans.JsonData;
import com.wx.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
    public JsonData saveUser(UserParam param) {
        ValidateUtil.check(param);
        sysUserService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam param) {
        sysUserService.update(param);
        return JsonData.success();
    }

    @GetMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery) {
        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }

    @GetMapping("/acls.json")
    @ResponseBody
    public JsonData aclList(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
