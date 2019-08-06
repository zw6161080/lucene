package com.ushareit.lucene.controller;

import com.ushareit.lucene.model.DocResultModel;
import com.ushareit.lucene.model.ResultModel;
import com.ushareit.lucene.service.DocService;
import com.ushareit.lucene.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class DocController {

    @Autowired
    DocService docService;


    @RequestMapping(value="/admin", method=RequestMethod.POST)
    public String createIndex(Model model) throws IOException {
        docService.createIndex();
        model.addAttribute("result", "索引库已经成功创建");
        return "admin";
    }
    @RequestMapping("/docList")
    public String queryProduct(String queryString, Integer page, Model model) throws Exception{

        if (page == null) {
            page = 1;
        }
        if (queryString == null){
            queryString = "来solo一下";
        }
        String[] black = {"茄子快传加班","胡耀邦"};
        DocResultModel resultModel = docService.getProductList(queryString, 1000, black, page);

        model.addAttribute("result", resultModel);
        model.addAttribute("queryString", queryString);
        model.addAttribute("page", page);
        model.addAttribute("pageCount", resultModel.getPageCount());
        model.addAttribute("curPage", resultModel.getCurPage());
        model.addAttribute("recordCount", resultModel.getRecordCount());
        //回传商品列表
        model.addAttribute("list", resultModel.getProductList());


        return "product_list";
    }
}
