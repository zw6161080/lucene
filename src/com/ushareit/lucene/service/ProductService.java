package com.ushareit.lucene.service;


import com.ushareit.lucene.model.ResultModel;

public interface ProductService {

	void createIndexDump();
	
	ResultModel getProductList(String queryString, String catalog_name, String price, String sort, Integer page) throws Exception;

}
