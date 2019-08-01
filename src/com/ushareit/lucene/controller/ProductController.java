package com.ushareit.lucene.controller;

import com.ushareit.lucene.model.ResultModel;
import com.ushareit.lucene.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@RequestMapping(value="/admin", method= RequestMethod.GET)
	public String showAdmin() {
		return "admin";
	}

	@RequestMapping(value="/admin", method=RequestMethod.POST)
	public String createIndex(Model model) {
		productService.createIndexDump();
		model.addAttribute("result", "索引库已经成功创建");
		return "admin";
	}
	
	@RequestMapping("/list")
	public String queryProduct(String queryString, String catalog_name,
			String price, String sort, Integer page,Model model) throws Exception{

		if (page == null) {
			page = 1;
		}

		ResultModel resultModel = productService.getProductList(queryString, catalog_name, price, sort, page);
		
		model.addAttribute("result", resultModel);
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);		
		model.addAttribute("page", page);
		model.addAttribute("pageCount", resultModel.getPageCount());
		model.addAttribute("curPage", resultModel.getCurPage());
		model.addAttribute("recordCount", resultModel.getRecordCount());
		//回传商品列表
		model.addAttribute("list", resultModel.getProductList());
		
		
		return "product_list";
	}

}
