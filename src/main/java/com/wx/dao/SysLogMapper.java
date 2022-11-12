package com.wx.dao;

import com.wx.domain.beans.PageQuery;
import com.wx.domain.dto.SearchLogDto;
import com.wx.domain.entity.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22343
 * @date 2022/10/22 11:04
 * @version 1.0
 */
public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
    
    int countBySearchDto(@Param("dto") SearchLogDto dto);
    
    List<SysLog> getPageListBySearchDto(@Param("dto") SearchLogDto dto, @Param("page") PageQuery page);
}
