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
            document.getElementById("catalog_name").value = "";
            document.getElementById("price").value = "";
            document.getElementById("page").value = "";
            queryList();
        }

        function queryList() {
            document.getElementById("actionForm").submit();
        }

        function filter(key, value) {
            document.getElementById(key).value = value;
            queryList();
        }

        function sort() {
            var s = document.getElementById("sort").value;
            if (s != "1") {
                s = "1";
            } else {
                s = "0";
            }
            document.getElementById("sort").value = s;
            queryList();
        }

        function changePage(p) {
            var curpage = Number(document.getElementById("page").value);
            curpage = curpage + p;
            document.getElementById("page").value = curpage;
            queryList();
        }
    </script>
</head>
<body>
<div id="o-header-2013">
    <div id="header-2013" style="position: absolute; left: 50%; top: 50%; margin-left: -50px; margin-top: -16px;text-align: center;">
        <div id="search-2013">
            <div class="i-search ld">
                <form id="actionForm" action="docList.action" method="POST">
                    <div class="form">
                        <input type="text" class="text" accesskey="s" name="queryString" id="key"
                               value="${queryString }"
                               autocomplete="off" onkeydown="javascript:if(event.keyCode==13) {query()}">
                        <input type="button" value="搜索" class="button" onclick="query()">
                    </div>
                    <input type="hidden" name="catalog_name" id="catalog_name" value="${catalog_name }"/>
                    <input type="hidden" name="price" id="price" value="${price }"/>
                    <input type="hidden" name="page" id="page" value="${curPage }"/>
                    <input type="hidden" name="sort" id="sort" value="${sort }"/>
                </form>
            </div>
        </div>
    </div>
    <!--header end-->
</div>
<div class="search bar6">
    <form>
        <input type="text" placeholder="请输入您要搜索的内容...">
        <button type="submit"></button>
    </form>
</div>
</body>
</html>