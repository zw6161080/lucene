<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!-- saved from url=(0047)http://list.jd.com/list.html?cat=1315,1343,1355 -->
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="utf-8" http-equiv="charset">
    <link rel="stylesheet" type="text/css"
          href="<c:url value='/resource'/>/base.css" media="all">
    <link rel="stylesheet" type="text/css"
          href="<c:url value='/resource'/>/plist20131112.css" media="all">
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
<body class="root61">
<div id="o-header-2013">
    <div class="w" id="header-2013">
        <div id="search-2013">
            <div class="i-search ld">
                <ul id="shelper" class="hide"></ul>
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
            <div id="hotwords"></div>
        </div>
    </div>
    <!--header end-->
</div>
<div class="w main">
    <div class="right-extra">
        <div id="filter">
            <div class="cls"></div>
            <div class="fore1">
                <dl class="activity">
                    <dd></dd>
                </dl>
                <div class="pagin pagin-m">
                    <span class="text"><i>${curPage }</i>/${pageCount }</span>
                    <a href="javascript:changePage(-1)" class="prev">上一页<b></b></a>
                    <a href="javascript:changePage(1)" class="next">下一页<b></b></a>
                </div>
                <div class="total">
			<span>共<strong>${recordCount }</strong>条数据
			</span>
                </div>
                <span class="clr"></span>
            </div>
        </div>
        <!--商品列表开始-->
        <div id="plist" class="m plist-n7 plist-n8 prebuy">
            <table>
                <c:forEach var="item" items="${list }">
                    <tr>
                        <td><a href=${item.fileUrl}>${item.fileName}</a></td>
                        <td>${item.fileContent}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <!--商品列表结束-->
    </div>

    <span class="clr"></span>
    <div id="Collect_Tip" class="Tip360 w260"></div>

</div><!--<div class="w main">-->

</body>
</html>