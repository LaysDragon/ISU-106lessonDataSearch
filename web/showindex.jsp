<%--
  Created by IntelliJ IDEA.
  User: lays7
  Date: 2017/10/5
  Time: 下午 01:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>展示資料</title>
    <link rel="stylesheet" type="text/css" href="common.css">
</head>
<body>
<div>
</div>
<div id="menu" style="border: solid;float:left;width: 15%;height: 100%;">
    <%@include file="/WEB-INF/jspf/menu.jspf"%>
</div>
<div id="content">
    <jsp:useBean id="index" class="com.lays.bean.IndexsBean"/>

    這裡展示目前的索引狀態<br>
    <table class="border_bottom">
        <tr><td>索引類型</td><td>資料量</td></tr>
        <tr><td>Term索引</td><td>${index.indexRecords.size()} 筆</td></tr>
        <tr><td>2-gram 樹:</td><td>${index.gram2IndexDatas.size()} 筆</td></tr>
        <tr><td>Biword索引</td><td>${index.biwordIndexDatas.size()} 筆</td></tr>
        <tr><td>Permuterm 索引</td><td>${index.permutermIndex.size()} 筆</td></tr>
    </table>
    一般Term索引:
    <%--<jsp:useBean id="indexs" class="com.lays.bean.IndexsBean" scope="request"/>--%>
    <%--@elvariable id="indexs" type="TreeMap<String,Term>"--%>
    <table class="border_bottom">
        <tr><td>關鍵字</td><td>文件列表</td></tr>
        <c:forEach var="entry" items="${index.indexRecords}">
        <tr><td  valign="top">${fn:escapeXml(entry.key)}</td>
        <td  valign="top">    <c:forEach var="entry" items="${entry.value.indexRecords}">
            [文件名: <a target="_blank" href="get_doc?id=${entry.document.id}">${entry.document.name}</a> - 頻率: ${entry.frequency}-位置:
            <c:forEach var="entry" items="${entry.position}">
                ${entry.index},
            </c:forEach>
            ]
            </c:forEach></td></tr>
        </c:forEach>

    </table>
    2-gram 樹: <br>
    <table class="border_bottom">
        <tr ><td>值</td><td>關鍵字列表</td></tr>
        <c:forEach  var="entry" items="${index.gram2IndexDatas}">
        <tr>
            <td  valign="top">${entry.key}</td>
            <td  valign="top"><c:forEach var="entry" items="${entry.value}">
                ${entry.keyword},
            </c:forEach></td>
        </tr>
        </c:forEach>
    </table>
    biword index: <br>
    <table class="border_bottom">
        <tr ><td>值</td><td>關鍵字列表</td></tr>
        <c:forEach var="entry" items="${index.biwordIndexDatas}">
            <tr><td  valign="top">${fn:escapeXml(entry.key)}</td>
                <td  valign="top">    <c:forEach var="entry" items="${entry.value.indexRecords}">
                    [文件名: <a target="_blank" href="get_doc?id=${entry.document.id}">${entry.document.name}</a>-位置:
                    <c:forEach var="entry" items="${entry.position}">
                        ${entry.index},
                    </c:forEach>
                    ]
                </c:forEach></td></tr>
        </c:forEach>
    </table>
    Permuterm 索引<br>
    <table class="border_bottom">
        <tr ><td>值</td><td>關鍵字列表</td></tr>
        <c:forEach var="entry" items="${index.permutermIndex}">
            <tr><td  valign="top">${fn:escapeXml(entry.key)}</td>
                <td  valign="top">    <c:forEach var="entry" items="${entry.value.indexRecords}">
                    [文件名: <a target="_blank" href="get_doc?id=${entry.document.id}">${entry.document.name}</a>-位置:
                    <c:forEach var="entry" items="${entry.position}">
                        ${entry.index},
                    </c:forEach>
                    ]
                </c:forEach></td></tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
