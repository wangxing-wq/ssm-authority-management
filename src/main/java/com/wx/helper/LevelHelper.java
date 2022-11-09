package com.wx.helper;


import com.google.common.base.Joiner;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author 22343
 * @version 1.0
 */
public class LevelHelper {

	public static final String DELIMITER = ".";
	public static final String ROOT = "0";
	
	public static String calculateLevel(String parentLevel, Integer parentId){
		if (ObjectUtils.isEmpty(parentId)){
			return ROOT;
		}
		return Joiner.on(DELIMITER).join(parentLevel,parentId);
	}
	
}
