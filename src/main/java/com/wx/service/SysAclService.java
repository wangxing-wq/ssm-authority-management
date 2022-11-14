package com.wx.service;

import javax.annotation.Resource;

import com.wx.domain.beans.PageQuery;
import com.wx.domain.beans.PageResult;
import com.wx.domain.entity.SysAcl;
import com.wx.dao.SysAclMapper;
import com.wx.helper.RequestHolder;
import com.wx.domain.param.AclParam;
import com.wx.util.IpUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
@Service
public class SysAclService{

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;
    

    public SysAcl selectByPrimaryKey(Integer id) {
        return sysAclMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(SysAcl record) {
        return sysAclMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(SysAcl record) {
        return sysAclMapper.updateByPrimaryKey(record);
    }
    
    public void save(AclParam param) {
        // TODO: 2022/10/29 name,url and aclModuleId,
        SysAcl build = getSysAcl(param);
        // build.setCode(
        build.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        build.setOperateTime(new Date());
        build.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclMapper.insertSelective(build);
        sysLogService.saveAclLog(null, build);
    }
    

    public void update(AclParam param) {
        SysAcl oldAcl = sysAclMapper.selectByPrimaryKey(param.getId());
        // TODO: 2022/10/29 name,url and aclModuleId,
        // sysLogService.saveAclLog(before, after);
    }
    
    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId,PageQuery pageQuery) {
        List<SysAcl> aclList = sysAclMapper.findListByAclModuleId(aclModuleId, pageQuery);
        return PageResult.<SysAcl>builder().data(aclList).total(aclList.size()).build();
    }
    
    private static SysAcl getSysAcl(AclParam param) {
        return SysAcl.builder().name(param.getName()).remark(param.getRemark()).aclModuleId(param.getAclModuleId()).status(param.getStatus()).url(param.getUrl()).type(param.getType()).seq(param.getSeq()).build();
    }
    
    
}
