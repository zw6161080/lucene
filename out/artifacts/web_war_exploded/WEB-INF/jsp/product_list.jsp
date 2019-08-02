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
            if((curpage<=1&& p<0)||(curpage>=${pageCount}&& p>0))
            {;}
            else
            {curpage = curpage + p;}

            document.getElementById("page").value = curpage;
            queryList();
        }
    </script>
</head>
<body class="root61">
<div id="o-header-2013">
    <div class="w" id="header-2013">
        <div id="search-2013" >
            <form id="actionForm" action="docList.action" method="POST">
                <div class="i-search ld" style="float:left">

                    <div class="form">

                        <input type="text" class="text" accesskey="s" name="queryString" id="key"
                               value="${queryString }"
                               autocomplete="off" onkeydown="javascript:if(event.keyCode==13) {query()}">

                        <input type="button" value="搜索" class="button" onclick="query()">



                        <input type="hidden" name="catalog_name" id="catalog_name" value="${catalog_name }"/>
                        <input type="hidden" name="price" id="price" value="${price }"/>
                        <input type="hidden" name="page" id="page" value="${curPage }"/>
                        <input type="hidden" name="sort" id="sort" value="${sort }"/>
                    </div>
                </div>
                <div id="modelchoose">
                    <select name="model" class="select" >
                    <option value="1" selected="false" >纯净模式</option>
                    <option value="2" selected="true">无广告模式</option>
                    <option value="3" selected="false">隐私模式</option>
                </select>
                </div>
            </form>

            <div id="hotwords"></div>
        </div>
    </div>

</div>
<!--header end-->
<div class="w">
    <div id="nav-2013">
        <%--        <div id="categorys-2013" class="categorys-2014">--%>
        <%--            <div class="mt ld">--%>
        <%--                <h2><a href="http://www.jd.com/allSort.aspx">全部商品分类<b></b></a></h2>--%>
        <%--            </div>--%>
        <%--        </div>--%>
        <div id="treasure"></div>
        <ul id="navitems-2013">
            <li class="fore1" id="nav-home"><a href="#">热搜</a></li>
            <li class="fore2" id="nav-fashion"><a href="#"></a></li>
            <li class="fore3" id="nav-chaoshi"><a href="#">逛一逛</a></li>
            <li class="fore4" id="nav-tuan"><a href="#" target="_blank"></a></li>
            <li class="fore5" id="nav-auction"><a href="#">精选</a></li>
            <li class="fore6" id="nav-shan"><a href="#"></a></li>
            <li class="fore7" id="nav-jinrong"><a href="#" target="_blank">猜你喜欢</a></li>
        </ul>
    </div>
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

                    <a href="javascript:changePage(-1);" class="prev">上一页<b></b></a>
                    <a href="javascript:changePage(1);" class="next">下一页<b></b></a>
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