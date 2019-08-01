package com.ushareit.lucene.dao.impl;
import java.io.*;
import	java.io.ObjectInputStream.GetField;

import com.ushareit.lucene.common.Config;
import com.ushareit.lucene.common.IndexCommon;
import com.ushareit.lucene.dao.DocsIndexDao;
import com.ushareit.lucene.model.DocModel;
import com.ushareit.lucene.model.DocResultModel;
import com.ushareit.lucene.model.ProductModel;
import com.ushareit.lucene.model.ResultModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.filefilter.WildcardFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.ui.Model;

import javax.print.Doc;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

public class DocsIndexDaoImpl implements DocsIndexDao {
    /**
     * 建立索引
     * @throws IOException
     */
    @Test
    @Override
    public void buildIndex() throws IOException {

        IndexWriter indexWriter = IndexCommon.getIndexWriter();
        File file = new File(Config.getDocsPath());
        File[] files = file.listFiles();
        //建立索引
        for (File f : files){

            Document document = new Document();
            String fileName = f.getName().split(".txt")[0];
            Field fileNameField = new TextField("fileName",fileName.split(".txt")[0], Field.Store.YES);
            String fileUrl = getFileUrl(f);
            Field fileUrlField = new TextField("fileUrl",fileUrl, Field.Store.YES);
            long fileSize = f.length();
            Field fileSizeField = new LongField("fileSize",fileSize, Field.Store.YES);
            String filePath = f.getPath();
            Field filePathField = new StoredField("filePath",filePath);
            String fileContent = FileUtils.readFileToString(f);
            Field fileContentField = new TextField("fileContent",fileContent, Field.Store.YES);
            document.add(fileNameField);
            document.add(fileUrlField);
            document.add(fileSizeField);
            document.add(fileContentField);
            document.add(filePathField);
            indexWriter.addDocument(document);
        }
        indexWriter.close();

    }

    /**
     * 删除所有的索引
     */
    @Test
    @Override
    public void deleteAllIndex()  {
        try {
            IndexCommon.getIndexWriter().deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            IndexCommon.getIndexWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param searchContent
     * @param limit
     * @param blacklist
     * @return
     */
    @Override
    public DocResultModel doSearchWithQueryParse(String searchContent, int limit, String[] blacklist,int page) throws ParseException, IOException {
        IndexSearcher indexSearcher = IndexCommon.getIndexSearcher();
        BooleanQuery booleanQuery = new BooleanQuery();

        QueryParser parser = new QueryParser("fileContent",IndexCommon.getAnalyzer());
        Query keyWordQuery = parser.parse(searchContent);
        booleanQuery.add(parser.parse(searchContent), BooleanClause.Occur.MUST);
        if(blacklist != null && blacklist.length != 0){
           for(String blackItem:blacklist){
               if(!blackItem.equals("")){
               QueryParser queryParser = new QueryParser("fileContent",IndexCommon.getAnalyzer());
               booleanQuery.add(queryParser.parse(blackItem), BooleanClause.Occur.MUST_NOT);}
           }
        }
        DocResultModel resultList = queryIndex(booleanQuery,limit, page);
        //如果是根据关键字查询的结果，需要高亮显示关键字
        if (keyWordQuery != null) {
            QueryScorer queryScorer = new QueryScorer(keyWordQuery);
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"color:red\">", "</span>");
            Highlighter highlighter = new Highlighter(formatter, queryScorer);
            if (null != resultList.getProductList()) {
                for (DocModel model:resultList.getProductList()) {
                    try {
                        //高亮显示商品名称中的关键字
                        String name = highlighter.getBestFragment(IndexCommon.getAnalyzer(), "fileName", model.getFileName());
                        String content = highlighter.getBestFragment(IndexCommon.getAnalyzer(),"fileContent",model.getFileContent());
                        System.out.println(model.getFileName() + "=>" + name);
                        //如果商品名称中没有关键字则返回null，此时不更新。
                        if (null != name) {
                            model.setFileName(name);
                        }if(content != null)model.setFileContent(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return resultList;
    }
    @Test
    public void testQuery() throws IOException, ParseException {
        DocResultModel resultModel = doSearchWithQueryParse("尤文图斯",1000,new String[]{"尤文图斯"},1);
        System.out.println(resultModel.getDocList().get(0).getFileContent());
    }
    /**
     * 通过query得到查询的结果
     * @param query
     * @param limit
     * @param indexSearcher
     * @return
     * @throws IOException
     */
    private static List<Document> getResult(Query query, int limit, IndexSearcher indexSearcher) throws IOException {
        TopDocs docs = indexSearcher.search(query,limit);

        ScoreDoc[] scoreDocs = docs.scoreDocs;
        List<Document> list = new ArrayList<> (scoreDocs.length);
        for (ScoreDoc s: scoreDocs) {
            int doc = s.doc;
            list.add(indexSearcher.doc(doc));
        }
        return list;
    }

    /**
     * 通用查询方法，根据指定的Query对象查询索引库
     * <p>Title: queryIndex</p>
     * <p>Description: </p>
     * @param query
     * @param page
     * @return
     */
    private DocResultModel queryIndex(Query query,  int limit ,int page) {
        //创建结果对象
        //ResultModel resultModel = new ResultModel();
        DocResultModel resultModel = new DocResultModel();

        //获得IndexSearcher对象
        IndexSearcher searcher = IndexCommon.getIndexSearcher();
        List<DocModel> docModelList = new ArrayList<>();

        //List<ProductModel> productList = new ArrayList<>();
        try {
            ScoreDoc[] scoreDocs = searcher.search(query,limit).scoreDocs;
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
                DocModel model = new DocModel();
                model.setFileName(document.get("fileName"));
                model.setFileContent(document.get("fileContent"));
                model.setFileUrl(document.get("fileUrl"));
                //将商品对象添加到列表中
                docModelList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultModel.setDocList(docModelList);
        return resultModel;
    }


    private static String getFileUrl(File file) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(file));
        String url = r.readLine();
        r.close();
        if(url.contains("http"))return url;
        else return null;
    }






}
