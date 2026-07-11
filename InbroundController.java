package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.DataResults;
import com.warehouse.common.PageUtils;
import com.warehouse.common.TimeUtils;
import com.warehouse.entity.Inbround;
import com.warehouse.entity.Products;
import com.warehouse.entity.Types;
import com.warehouse.service.InbroundService;
import com.warehouse.service.ProductsService;
import com.warehouse.service.TypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inbround")
public class InbroundController {

    @Autowired
    InbroundService inbroundService;

    @Autowired
    ProductsService productsService;

    @Autowired
    TypesService typesService;

    /**
     * 06-跳转到入库页面
     * @return
     */
    @GetMapping("/page")
    public String inbound(@RequestParam(defaultValue = "1") Long pageIndex,
                          @RequestParam(defaultValue = "10") Long pageSize, Model model){
        IPage<Inbround> page = inbroundService.page(new Page<Inbround>(pageIndex, pageSize), new QueryWrapper<Inbround>().eq("del", 0));
        //封装成工具类
        PageUtils pageUtils = new PageUtils(pageIndex, pageSize, page.getTotal(), page.getRecords());
        System.out.println("后台商品数据分页工具类:" + pageUtils);
        model.addAttribute("pageUtils", pageUtils);
        return "inbround";
    }


    /**
     * 商品入库
     * @param inbround
     * @param tid
     * @param pid
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public DataResults add(Inbround inbround,int tid,int pid){
        Products products = productsService.getById(pid);
        Types types = typesService.getById(tid);
        inbround.setPname(products.getPtitle());
        inbround.setPtype(types.getTname());
        inbround.setDel(0);
        inbround.setIndate(TimeUtils.now());
        //入库加库存
        products.setPcount(inbround.getIncount()+products.getPcount());
        productsService.updateById(products);
        //新增入库记录
        inbroundService.save(inbround);
        return new DataResults(200,"入库成功",null);
    }


    @DeleteMapping("delete")
    @ResponseBody
    public DataResults delete(int id){
        Inbround inbround=new Inbround();
        inbround.setId(id);
        inbround.setDel(1);
        inbroundService.updateById(inbround);
        return new DataResults(200,"删除成功",null);
    }


}
