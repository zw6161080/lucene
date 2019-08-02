package com.ushareit.lucene.dao;

import com.ushareit.lucene.model.DocResultModel;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Filter;

import java.io.IOException;
import java.util.List;

public interface DocsIndexDao {
    /**
     * 建立索引
     * @throws IOException
     */
    void buildIndex() throws IOException;

    /**
     * 删除所有的索引
     */
    void deleteAllIndex() throws IOException;

    /**
     *
     * @return
     */
    DocResultModel doSearchWithQueryParse(String searchContent, int limit, String[] blacklist, int page) throws ParseException, IOException;
}
