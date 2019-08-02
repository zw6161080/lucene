package com.ushareit.lucene.service;

import com.mysql.cj.xdevapi.DocResult;
import com.ushareit.lucene.model.DocResultModel;
import com.ushareit.lucene.model.ResultModel;
import org.springframework.stereotype.Service;

public interface DocService {

    DocResultModel getProductList(String searchString, int limit, String[] blackList, int page) throws Exception;
}
