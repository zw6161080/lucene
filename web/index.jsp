<%-- Created by IntelliJ IDEA. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="utf-8" http-equiv="charset">
    <link rel="stylesheet" type="text/css"
          href="<c:url value='/resource'/>/index.css" media="all">
    <script type="text/javascript"
            src="<c:url value='/resource'/>/jquery-1.2.6.pack.js"></script>
    <script type="text/javascript">
        function query() {
            queryList();
        }

        function queryList() {
            document.getElementById("actionForm").submit();
        }
    </script>
</head>
<body>
<div class="search bar6">
    <div id="logo">
        <img src="resource/logo-201305.png">
    </div>
    <form id="actionForm" action="docList.action" method="POST">
        <input type="text" placeholder="请输入您要搜索的内容..." name="queryString" value="${queryString }">
        <button type="button" onclick="query()"></button>
    </form>
</div>
</body>
</html>