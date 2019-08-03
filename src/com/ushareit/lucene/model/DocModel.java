package com.ushareit.lucene.model;

import java.util.Objects;

public class DocModel {
    private String fileName;
    private String fileUrl;
    private String fileContent;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocModel docModel = (DocModel) o;
        return Objects.equals(fileName, docModel.fileName) &&
                Objects.equals(fileUrl, docModel.fileUrl) &&
                Objects.equals(fileContent, docModel.fileContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileUrl, fileContent);
    }
}
