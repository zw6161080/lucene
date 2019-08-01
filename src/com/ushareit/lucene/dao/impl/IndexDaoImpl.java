package com.ushareit.lucene.dao.impl;

import com.ushareit.lucene.common.Config;
import com.ushareit.lucene.common.IndexCommon;
import com.ushareit.lucene.dao.IndexDao;
import com.ushareit.lucene.model.ProductModel;
import com.ushareit.lucene.model.ResultModel;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class IndexDaoImpl implements IndexDao {

	/**
	 * 创建索引库
	 * <p>Title: createDump</p>
	 * <p>Description: </p>
	 * @param list
	 */
	@Override
	public void createDump(List<ProductModel> list ) {
		//获得IndexWriter对象
		IndexWriter writer = IndexCommon.getIndexWriter();
		//创建索引前先清空
		try {
			writer.deleteAll();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//重新建立索引
		for (ProductModel model:list) {
			//创建文档对象
			Document document = new Document();
			//创建索引域
			Field id = new IntField("pid", model.getPid(), Store.YES);
			Field name = new TextField("name", model.getName(), Store.YES);
			Field catalog = new IntField("catalog", model.getCatalog(), Store.YES);
			Field catalog_name = new StringField("catalog_name", model.getCatalog_name(), Store.YES);
			Field price = new DoubleField("price", model.getPrice(), Store.YES);
			Field number = new IntField("number", model.getNumber(), Store.YES);
			Field description = new TextField("discription", model.getDescription(), Store.NO);
			Field releaseTime = new LongField("release_time", model.getRelease_time().getTime(), Store.YES);
			Field picture = new StoredField("picture", model.getPicture());
			//将域添加到文档对象中
			document.add(id);
			document.add(name);
			document.add(catalog);
			document.add(catalog_name);
			document.add(price);
			document.add(number);
			document.add(description);
			document.add(releaseTime);
			document.add(picture);
			try {
				//写索引库
				writer.addDocument(document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			//提交
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 更加分类名称查询商品分类列表
	 * <p>Title: getProductList</p>
	 * <p>Description: </p>
	 * @param catalog_name
	 * @param page
	 * @return
	 */
	@Override
	public ResultModel getProductList(String queryString,
									  String catalog_name, Double priceStart,
									  Double priceEnd, Integer page, String sort) {
		
		// 根据分类名称查询索引
		//查询的字段
		BooleanQuery query = new BooleanQuery();
		Query keyWordQuery = null;
		if (queryString != null && !"".equals(queryString)) {
			//查询域列表
			String [] fields = {"name","discription"};
			//创建查询分析器
			QueryParser queryParser = new MultiFieldQueryParser(fields, IndexCommon.getAnalyzer());
			//根据搜索关键词创建查询
			try {
				keyWordQuery = queryParser.parse(queryString);
				query.add(keyWordQuery, Occur.MUST);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
			query.add(matchAllDocsQuery, Occur.MUST);
		}
		//根据商品分类名称查询
		if (catalog_name != null && !"".equals(catalog_name)) {
			Query catalogQuery = new TermQuery(new Term("catalog_name",catalog_name));
			query.add(catalogQuery, Occur.MUST);
		}
		
		//根据价格区间过滤
		Filter filter = null;
		if (priceStart != null) {
			filter = NumericRangeFilter.newDoubleRange("price", priceStart , priceEnd, true, true);
		}
		//排序，默认根据价格排序
		Sort sSort = new Sort();
		if ("1".equals(sort)) { 
			SortField sortField = new SortField("price", SortField.Type.DOUBLE);
			sSort.setSort(sortField);
		} else {
			SortField sortField = new SortField("price", SortField.Type.DOUBLE, true);
			sSort.setSort(sortField);
		}
		ResultModel resultList =queryIndex(query, filter, sSort, page);
		//如果是根据关键字查询的结果，需要高亮显示关键字
		if (keyWordQuery != null) {
			QueryScorer queryScorer = new QueryScorer(keyWordQuery);
			SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"color:red\">", "</span>");
			Highlighter highlighter = new Highlighter(formatter, queryScorer);
			if (null != resultList.getProductList()) {
				for (ProductModel model:resultList.getProductList()) {
					try {
						//高亮显示商品名称中的关键字
						String name = highlighter.getBestFragment(IndexCommon.getAnalyzer(), "name", model.getName());
						System.out.println(model.getName() + "=>" + name);
						//如果商品名称中没有关键字则返回null，此时不更新。
						if (null != name) {
							model.setName(name);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
					
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 通用查询方法，根据指定的Query对象查询索引库
	 * <p>Title: queryIndex</p>
	 * <p>Description: </p>
	 * @param query
	 * @param page
	 * @return
	 */
	private ResultModel queryIndex(Query query, Filter filter, Sort sort, int page) {
		//创建结果对象
		ResultModel resultModel = new ResultModel();
		
		//获得IndexSearcher对象
		IndexSearcher searcher = IndexCommon.getIndexSearcher();
		//商品列表
		List<ProductModel> productList = new ArrayList<>();
		try {
			ScoreDoc[] scoreDocs = searcher.search(query, filter, Config.getSearchMax(), sort).scoreDocs;
			//商品总数
			resultModel.setRecordCount((long)scoreDocs.length);
			//计算总页数
			int pageCount = scoreDocs.length / Config.getPageSize();
			if ((scoreDocs.length % Config.getPageSize()) > 0) {
				pageCount ++;
			}
			resultModel.setPageCount(pageCount);
			//当前页
			resultModel.setCurPage(page);
			if (page > pageCount) {
				return resultModel;
			}
			//计算起始和结束下标
			int start = (page - 1) * Config.getPageSize();
			int end = page * Config.getPageSize();
			
			//起始下标大于记录总数
			if (start < 0 || start > scoreDocs.length) {
				return resultModel;
			}
			//结束下标大于记录总数时
			if (end > scoreDocs.length) {
				end = scoreDocs.length;
			}
			
			//遍历查询索引结果
			for (int i = start; i < end; i++) {
				Document document = searcher.doc(scoreDocs[i].doc);
				//商品对象
				ProductModel model = new ProductModel();
				model.setPid(Integer.parseInt(document.get("pid")));
				model.setName(document.get("name"));
				model.setPrice(Double.parseDouble(document.get("price")));
				model.setPicture(document.get("picture"));
				//将商品对象添加到列表中
				productList.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultModel.setProductList(productList);
		return resultModel;
	}

}
