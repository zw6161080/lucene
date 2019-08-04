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
import org.apache.lucene.store.Lock;
import org.apache.lucene.util.Version;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class IndexCommon {

	/**
	 * 索引库路径
	 */
	private static IndexWriter indexWriter0;
	private static IndexWriter indexWriter1;
	private static IndexReader indexReader0;
	private static IndexReader indexReader1;
	private static IndexSearcher indexSearcher0;
	private static IndexSearcher indexSearcher1;
	private static Analyzer analyzer;

	static{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (indexWriter0!=null) {
					try {
						indexWriter0.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (indexWriter1!=null) {
					try {
						indexWriter1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (indexReader0 != null) {
					try {
						indexReader0.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (indexReader1 != null) {
					try {
						indexReader1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static IndexWriter getIndex0Writer() {
		if (indexWriter0 != null) {
			return indexWriter0;
		}
		try {
			Directory dir = FSDirectory.open( Paths.get(Config.getIndex0Path()));
			//判断是否以及指定分词器，如未指定使用默认分词器
			Analyzer analyzer = getAnalyzer();
			//创建IndexWriter的配置
			IndexWriterConfig config  = new IndexWriterConfig(analyzer);
			indexWriter0 = new IndexWriter(dir, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexWriter0;
	}

	public static IndexWriter getIndex1Writer() {
		if (indexWriter1 != null) {
			return indexWriter1;
		}
		try {
			Directory dir = FSDirectory.open( Paths.get(Config.getIndex1Path()));
			//判断是否以及指定分词器，如未指定使用默认分词器
			Analyzer analyzer = getAnalyzer();
			//创建IndexWriter的配置
			IndexWriterConfig config  = new IndexWriterConfig(analyzer);
			indexWriter1 = new IndexWriter(dir, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexWriter1;
	}
	
	/**
	 * 获得索引读取对象
	 * <p>Title: getIndexReader</p>
	 * <p>Description: </p>
	 * @return
	 */
	public static IndexReader getIndex0Reader() {
		if (indexReader0 != null) {
			return indexReader0;
		}
		try {
			File path = new File(Config.getIndex0Path());
			if (!path.exists()) {
				if (path.isDirectory()) {
					path.mkdirs();
				}
			}
			Directory dir = FSDirectory.open(Paths.get(path.getPath()));
			indexReader0 = DirectoryReader.open(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indexReader0;
	}

	public static IndexReader getIndex1Reader() {
		if (indexReader1 != null) {
			return indexReader1;
		}
		try {
			File path = new File(Config.getIndex1Path());
			if (!path.exists()) {
				if (path.isDirectory()) {
					path.mkdirs();
				}
			}
			Directory dir = FSDirectory.open(Paths.get(path.getPath()));
			indexReader1 = DirectoryReader.open(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indexReader1;
	}

	/**
	 * 索引查询对象
	 * <p>Title: getIndexSearcher</p>
	 * <p>Description: </p>
	 * @return
	 */
	public static IndexSearcher getIndex0Searcher() {
		if (indexSearcher0 != null ) {
			return indexSearcher0;
		}
		indexSearcher0 = new IndexSearcher(getIndex0Reader());
		
		return indexSearcher0;

	}

	public static IndexSearcher getIndex1Searcher() {
		if (indexSearcher1 != null ) {
			return indexSearcher1;
		}
		indexSearcher1 = new IndexSearcher(getIndex1Reader());

		return indexSearcher1;

	}


	/**
	 * 指定分词器
	 * <p>Title: setAnalyzer</p>
	 * <p>Description: </p>
	 * @param analyzer
	 */
	public static void setAnalyzer(Analyzer analyzer) {
		IndexCommon.analyzer = analyzer;
//		indexReader=null;
//		indexWriter=null;
		indexWriter0 = null;
		indexWriter1 = null;
		indexReader0 = null;
		indexReader1 = null;

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
