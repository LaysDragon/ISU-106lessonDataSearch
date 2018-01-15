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
    <title>搜尋文件</title>
    <link rel="stylesheet" type="text/css" href="common.css">
</head>
<body>
<div>
</div>
<div id="menu" style="border: solid;float:left;width: 15%;height: 100%;">
    <%@include file="/WEB-INF/jspf/menu.jspf" %>
</div>
<div id="content">
    <jsp:useBean id="search" class="com.lays.bean.SearchBean"/>
    <jsp:useBean id="info" class="com.lays.bean.SearchHeighlightBean"/>

    <c:choose>
        <c:when test="${not empty param.keyword}">
            <c:set var="keyword" value="${param.keyword}"/>
        </c:when>
        <c:when test="${not empty param.boolKeywords1 and not empty param.boolKeywords2}">
            <c:set var="keyword"
                   value="${param.boolKeywords1} ${param.bool1} ${param.boolKeywords2} ${param.bool2=='---'?'':param.bool2} ${param.bool2=='---'?'':param.boolKeywords3}"/>
        </c:when>
        <c:when test="${not empty param.sentenceKeywords}">
            <c:set var="keyword" value="\"${param.sentenceKeywords}\""/>
        </c:when>
        <c:when test="${not empty param.biwordKeywords}">
            <c:set var="keyword" value="@\"${param.biwordKeywords}\""/>
        </c:when>
    </c:choose>


    <form action="search.jsp">
        <table>
            <tr>
                <td><label for="universalKeywords">混合搜索:</label></td>
                <td><input type="text" id="universalKeywords" name="keyword" value="${fn:escapeXml(keyword)}"
                           style="width: 70%"></td>
            </tr>

            <%--<input type="submit" value="搜尋">--%>
            <%--</form>--%>
            <%--<form action="search.jsp">--%>
            <tr>
                <td><label for="boolKeywords1" style="margin-right: 6px;">布林查詢:</label></td>
                <td><input type="text" id="boolKeywords1" name="boolKeywords1"
                           value="${fn:escapeXml(param.boolKeywords1)}" style="width: 19%">
                    <select name="bool1">
                        <option value="and" ${param.bool1 == "and"?"selected":""}>AND</option>
                        <option value="or"  ${param.bool1 == "or"?"selected":""}>OR</option>
                        <option value="not"  ${param.bool1 == "not"?"selected":""}>NOT</option>
                    </select>
                    <input type="text" id="boolKeywords2" name="boolKeywords2"
                           value="${fn:escapeXml(param.boolKeywords2)}" style="width: 19%">
                    <select name="bool2">
                        <option value="---">---</option>
                        <option value="and" ${param.bool2 == "and"?"selected":""}>AND</option>
                        <option value="or"  ${param.bool2 == "or"?"selected":""}>OR</option>
                        <option value="not"  ${param.bool2 == "not"?"selected":""}>NOT</option>
                    </select>
                    <input type="text" id="boolKeywords3" name="boolKeywords3"
                           value="${fn:escapeXml(param.boolKeywords3)}" style="width: 19%"></td>
            </tr>
            <%--<input type="submit" value="搜尋">--%>
            <%--</form>--%>
            <%--<form action="search.jsp">--%>
            <tr>
                <td><label for="sentenceKeywords" style="margin-right: 8px;">Position 句子查詢:</label></td>
                <td><input type="text" id="sentenceKeywords" name="sentenceKeywords"
                           value="${fn:escapeXml(param.sentenceKeywords)}" style="width: 68%"></td>
            </tr>
            <%--<input type="submit" value="搜尋">--%>
            <tr>
                <td colspan="2">格式:(&lt;keyword&gt; &lt;interval default=2&gt;:&lt;keyword&gt;...)</td>
            </tr>
            <%--</form>--%>
            <%--<form action="search.jsp">--%>
            <tr>
                <td><label for="biwordKeywords" style="margin-right: 6px;">Biword句子查詢:</label></td>
                <td><input type="text" id="biwordKeywords" name="biwordKeywords"
                           value="${fn:escapeXml(param.biwordKeywords)}" style="width: 64%"></td>
            </tr>
            <tr>
                <td><input type="submit" value="搜尋"></td>
                <td><input id="wildcard" type="checkbox"
                           name="wildcard"
                           value="permuterm" ${param.wildcard == "permuterm"?"checked":""}>
                    <label for="wildcard">Permuterm query(僅支持兩個萬用符號查詢)</label>
                    <input type="checkbox" name="skipmode" id="skipmode"
                           value="skip" ${param.skipmode == "skip"?"checked":(param.skipmode != "nonskip" ?"checked":"")}><label for="skipmode">AND操作使用skipmode</label>
                    <input type="hidden" value="nonskip" name="skipmode"></td>
            </tr>
        </table>
    </form>


    <c:if test="${not empty keyword}">
        <%--@elvariable id="results" type="List<com.lays.indexer.Document>"--%>
        <jsp:setProperty name="search" property="wildCardMode" value="${param.wildcard != \"permuterm\"}"/>
        <jsp:setProperty name="search" property="skipMode" value="${param.skipmode == \"skip\"}"/>
        <jsp:setProperty name="search" property="query" value="${keyword}"/>
        查詢${keyword}的結果，共計 ${search.result.size()} 筆資料，AND 操作花費了 ${search.andOpCounter} 次迴圈。<br>
        <c:if test="${not empty search.result}">
            <ul>
                <c:forEach var="entry" items="${search.result}">
                    <li><a target="_blank" href="get_doc?id=${entry.document.id}">${entry.document.name}</a></li>
                    <jsp:setProperty name="info" property="indexRecord" value="${entry}"/>

                    <div style="background-color: cornsilk;width: 50%;word-wrap: break-word;">${info.htmlContent}</div>
                    <br>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${empty search.result}">
            查無資料，您是不是指 ${search.correctionQuerry} ?
        </c:if>
    </c:if>


</div>
</body>
</html>
