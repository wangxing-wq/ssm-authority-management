package com.wx.service;

import javax.annotation.Resource;
import com.wx.dao.SysLogMapper;
import com.wx.domain.beans.PageQuery;
import com.wx.domain.entity.SysLog;
import com.wx.param.SearchLogParam;
import org.springframework.stereotype.Service;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
@Service
public class SysLogService{

    @Resource
    private SysLogMapper sysLogMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return sysLogMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(SysLog record) {
        return sysLogMapper.insert(record);
    }

    
    public int insertSelective(SysLog record) {
        return sysLogMapper.insertSelective(record);
    }

    
    public SysLog selectByPrimaryKey(Integer id) {
        return sysLogMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(SysLog record) {
        return sysLogMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(SysLog record) {
        return sysLogMapper.updateByPrimaryKey(record);
    }
    
    public void recover(int id) {
    
    }
    
    public Object searchPageList(SearchLogParam param,PageQuery page) {
        return null;
    }
}
