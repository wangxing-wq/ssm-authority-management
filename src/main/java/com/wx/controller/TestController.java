package com.wx.controller;

import com.wx.helper.ApplicationContextHelper;
import com.wx.dao.SysAclModuleMapper;
import com.wx.domain.beans.JsonData;
import com.wx.domain.entity.SysAclModule;
import com.wx.domain.vo.TestVo;
import com.wx.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author 22343
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/hello")
	public String test(){
		return "hello world";
	}
	
	@GetMapping("/vaidator.json")
	public JsonData validator(TestVo testVo){
		log.info("validate");
		ValidateUtil.check(testVo);
		SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
		if (Objects.nonNull(moduleMapper)){
			SysAclModule module = moduleMapper.selectByPrimaryKey(1);
			log.info(JsonUtil.obj2String(module));
		}
		return JsonData.success();
	}

}
