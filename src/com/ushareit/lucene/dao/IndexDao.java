package com.ushareit.lucene.dao;

import com.ushareit.lucene.model.ProductModel;
import com.ushareit.lucene.model.ResultModel;

import java.util.List;

public interface IndexDao {

	void createDump(List<ProductModel> list);

	ResultModel getProductList(String queryString,
									  String catalog_name, Double priceStart,
									  Double priceEnd, Integer page, String sort);
}
