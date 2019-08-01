package com.ushareit.lucene.dao.impl;

import com.ushareit.lucene.common.DBManager;
import com.ushareit.lucene.dao.ProductDao;
import com.ushareit.lucene.model.ProductModel;
import org.springframework.stereotype.Repository;

import javax.sql.RowSet;
import java.util.ArrayList;
import java.util.List;

//dao层作用就是通过搜索取得数据库中的商品内容
@Repository
public class ProductDaoImpl implements ProductDao {

	@Override
	public List<ProductModel> selectAll() {
		String sql = "select * from products";
		//获得数据库管理对象
		DBManager dbManager = DBManager.getInstance();
		//创建商品对象列表
		List<ProductModel> resultList = new ArrayList<ProductModel>();
		try {
			//查询商品列表
			RowSet result = dbManager.query(sql);
			//便利商品列表
			while(result.next()) {
				//创建商品对象
				ProductModel product = new ProductModel();
				//商品编号
				product.setPid(result.getInt("pid"));
				//商品名称
				product.setName(result.getString("name"));
				//商品分类
				product.setCatalog(result.getInt("catalog"));
				//商品分类名称
				product.setCatalog_name(result.getString("catalog_name"));
				//价格
				product.setPrice(result.getDouble("price"));
				//数量
				product.setNumber(result.getInt("number"));
				//商品描述
				product.setDescription(result.getString("description"));
				//图片名称
				product.setPicture(result.getString("picture"));
				//上架时间
				product.setRelease_time(result.getDate("release_time"));

				//添加到商品列表
				resultList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
