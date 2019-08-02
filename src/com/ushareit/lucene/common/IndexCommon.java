package com.ushareit.lucene.common;

import com.ushareit.lucene.model.lucene.IKAnalyzer;
import com.ushareit.lucene.model.lucene.IKPinyinAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class IndexCommon {

	/**
	 * 索引库路径
	 */
	private static IndexWriter indexWriter;
	private static IndexReader indexReader;
	private static IndexSearcher indexSearcher;
	private static Analyzer analyzer;
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (indexWriter!=null) {
					try {
						indexWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (indexReader != null) {
					try {
						indexReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * 获得IndexWriter对象
	 * <p>Title: getIndexWriter</p>
	 * <p>Description: </p>
	 * @return
	 */
	public static IndexWriter getIndexWriter() {
		if (indexWriter != null) {
			return indexWriter;
		}
		try {
			Directory dir = FSDirectory.open( Paths.get(Config.getIndexPath()));
			//判断是否以及指定分词器，如未指定使用默认分词器
			Analyzer analyzer = getAnalyzer();
			//创建IndexWriter的配置
			IndexWriterConfig config  = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(dir, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexWriter;
	}
	
	/**
	 * 获得索引读取对象
	 * <p>Title: getIndexReader</p>
	 * <p>Description: </p>
	 * @return
	 */
	public static IndexReader getIndexReader() {
		if (indexReader != null) {
			return indexReader;
		}
		try {
			File path = new File(Config.getIndexPath());
			if (!path.exists()) {
				if (path.isDirectory()) {
					path.mkdirs();
				}
			}
			Directory dir = FSDirectory.open(Paths.get(path.getPath()));
			indexReader = DirectoryReader.open(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indexReader;
	}
	/**
	 * 索引查询对象
	 * <p>Title: getIndexSearcher</p>
	 * <p>Description: </p>
	 * @return
	 */
	public static IndexSearcher getIndexSearcher() {
		if (indexSearcher != null ) {
			return indexSearcher;
		}
		indexSearcher = new IndexSearcher(getIndexReader());
		
		return indexSearcher;

	}


	/**
	 * 指定分词器
	 * <p>Title: setAnalyzer</p>
	 * <p>Description: </p>
	 * @param analyzer
	 */
	public static void setAnalyzer(Analyzer analyzer) {
		IndexCommon.analyzer = analyzer;
		indexReader=null;
		indexWriter=null;

		//更改解析器后重新初始化IndexWriter
		/*if (indexWriter != null) {
			try {
				indexWriter.close();
				indexWriter = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//更改解析器后重新初始化IndexReader
		if (indexReader != null) {
			try {
				indexReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			indexReader = null;
		}*/
	}
	
	public static Analyzer getAnalyzer() {
		return analyzer==null ? analyzer = new IKAnalyzer():analyzer;
	}
	/**
	 * 分词器测试
	 * <p>Title: analyzerToken</p>
	 * <p>Description: </p>
	 */
	public static void analyzerToken(Analyzer analyzer, String text) {
		System.out.println("测试分词器：" + analyzer.getClass().getSimpleName());
		try {
			TokenStream tokenStream = analyzer.tokenStream("content", text);
//			tokenStream.addAttribute(CharTermAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
				System.out.println(attribute.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
