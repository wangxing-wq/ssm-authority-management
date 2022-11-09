package com.wx.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 22343
 * @version 1.0
 */
public class StringUtil {
	public static List<Integer> splitToListInt(String str) {
		List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
		return strList.stream().map(Integer::parseInt).collect(Collectors.toList());
	}
}
