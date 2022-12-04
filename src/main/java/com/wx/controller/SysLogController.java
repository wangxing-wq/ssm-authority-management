package com.wx.controller;

import com.wx.domain.beans.PageQuery;
import com.wx.domain.param.SearchLogParam;
import com.wx.service.SysLogService;
import com.wx.web.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author 22343
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public Result recover(@RequestParam("id") int id) {
        sysLogService.recover(id);
        return Result.success();
    }

    @GetMapping("/page.json")
    @ResponseBody
    public Result searchPage(SearchLogParam param, PageQuery page) {
        return Result.success(sysLogService.searchPageList(param, page));
    }
}
