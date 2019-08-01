package com.ushareit.lucene.model;

import org.springframework.ui.Model;

import java.util.List;

public class DocResultModel {



    private List<DocModel> docList;

    private Long recordCount;

    private int pageCount;

    private int curPage;
    public List<DocModel> getProductList() {
        return docList;
    }

    public List<DocModel> getDocList() {
        return docList;
    }

    public void setDocList(List<DocModel> docList) {
        this.docList = docList;
    }

    public Long getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getCurPage() {
        return curPage;
    }
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }
}
