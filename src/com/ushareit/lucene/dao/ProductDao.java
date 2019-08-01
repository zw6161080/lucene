package com.ushareit.lucene.dao;


import com.ushareit.lucene.model.ProductModel;

import java.util.List;

public interface ProductDao {

	List<ProductModel> selectAll();

}
