package com.wx.service;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.wx.enums.CacheKeyEnum;
import com.wx.dao.SysAclMapper;
import com.wx.dao.SysRoleAclMapper;
import com.wx.dao.SysRoleUserMapper;
import com.wx.domain.entity.SysAcl;
import com.wx.domain.entity.SysUser;
import com.wx.helper.RequestHolder;
import com.wx.util.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysCacheService sysCacheService;

    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.findListByAclId(aclIdList);
    }

    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            return sysAclMapper.findAll();
        }
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.findListByUserIdList(userAclIdList);
    }

    public boolean isSuperAdmin() {
        // ???????????????????????????????????????????????????????????????????????????????????????????????????
        // ????????????????????????????????????????????????????????????????????????????????????
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }

    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;
        // ?????????????????????????????????????????????????????????????????????????????????
        for (SysAcl acl : aclList) {
            // ????????????????????????????????????????????????????????????
            if (acl == null || acl.getStatus() != 1) { // ???????????????
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }

    public List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyEnum.USER_ACL, String.valueOf(userId));
        if (StringUtils.isEmpty(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (!CollectionUtils.isEmpty(aclList)) {
                sysCacheService.saveCache(JsonUtil.obj2String(aclList), 600, CacheKeyEnum.USER_ACL,
                        String.valueOf(userId));
            }
            return aclList;
        }
        return JsonUtil.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}
