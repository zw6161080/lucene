package com.ushareit.lucene.service.impl;

import com.ushareit.lucene.dao.IndexDao;
import com.ushareit.lucene.dao.ProductDao;
import com.ushareit.lucene.model.ProductModel;
import com.ushareit.lucene.model.ResultModel;
import com.ushareit.lucene.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {



	/**
	 * 索引管理dao
	 */
	@Autowired
	private IndexDao indexDao;

	@Override
	public void createIndexDump() {
	/*	//查询数据库获得全部商品
		List<ProductModel> proList = productDao.selectAll();
		//创建商品的全文索引
		indexDao.createDump(proList);*/
	}

	@Override
	public ResultModel getProductList(String queryString, String catalog_name, String price, String sort, Integer page) {
		//解析价格区间
		Double priceStart = null;
		Double priceEnd = null;
		if (price != null && !"".equals(price)) {
			String[] strPrice = price.split("-");
			//0-79 80-199
			if (strPrice.length == 2) {
				priceStart = new Double(strPrice[0]);
				priceEnd = new Double(strPrice[1]);
				//200以上
			} else if (strPrice.length == 1) {
				priceStart = new Double(strPrice[0]);
				//设定一个很大的数值
				priceEnd = new Double(99999999);
			}
		}
		ResultModel resultModel = indexDao.getProductList(queryString, catalog_name, priceStart, priceEnd, page, sort);
		return resultModel;
	}
}
