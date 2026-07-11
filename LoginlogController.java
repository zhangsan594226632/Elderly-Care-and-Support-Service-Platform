package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.common.DataResults;
import com.warehouse.entity.Admins;
import com.warehouse.entity.Loginlog;
import com.warehouse.service.LoginlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("loginlog")
public class LoginlogController {

    @Autowired
    LoginlogService loginlogService;

    /**
     * 查询当前登录人  用户的登录日志
     * @return
     */
    @GetMapping("getLogs")
    @ResponseBody
    public DataResults getLogs(HttpSession session){
        Admins admins= (Admins) session.getAttribute("currentusers");
        QueryWrapper<Loginlog> queryWrapper = new QueryWrapper<Loginlog>().eq("username", admins.getUsername()).orderByDesc("logintime").last("limit 0,10");
        List<Loginlog> loginlogList = loginlogService.list(queryWrapper);
        return new DataResults(200,"请求成功",loginlogList);
    }

}
