package com.ushareit.lucene.service.impl;
import	java.awt.TextField;

import com.ushareit.lucene.dao.DocsIndexDao;
import com.ushareit.lucene.dao.IndexDao;
import com.ushareit.lucene.dao.ProductDao;
import com.ushareit.lucene.dao.impl.DocsIndexDaoImpl;
import com.ushareit.lucene.model.DocResultModel;
import com.ushareit.lucene.model.ResultModel;
import com.ushareit.lucene.service.DocService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocServiceImpl implements DocService {
    DocsIndexDao docsIndexDao = new DocsIndexDaoImpl();
    @Override
    public DocResultModel getProductList(String searchString, int limit, String[] blackList, int page) throws Exception {
        return docsIndexDao.doSearchWithQueryParse(searchString,limit, blackList,page);
    }
    @Test
    public void testService() throws Exception {
        DocServiceImpl s = new DocServiceImpl();
        DocResultModel model = s.getProductList("尤文图斯",1000,null,1);
        System.out.println(model.getProductList().get(2).getFileContent());
    }
}
