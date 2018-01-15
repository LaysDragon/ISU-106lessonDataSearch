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
    <title>網頁爬蟲索引</title>
    <link rel="stylesheet" type="text/css" href="common.css">
</head>
<body>
<div>
</div>
<div id="menu" style="border: solid;float:left;width: 15%;height: 100%;">
    <%@include file="/WEB-INF/jspf/menu.jspf"%>
</div>
<div id="content">
    <jsp:useBean id="controller" class="com.lays.bean.CrawlerBean"/>
    <c:if test="${empty param.target_url}">
    <form style="display: inline">
        <label>
            目標網址:
            <input type="text" name="target_url" ${controller.finished?"":"disabled"} style="width: 70%">
        </label>
        <input type="submit" ${controller.finished?"":"disabled"}>
    </form>
    <form  style="display: inline">
        <input type="hidden" name="stop" value="true">
        <input type="submit" value="停止" ${controller.finished?"disabled":""}>
    </form>
        <br>
    </c:if>
    <c:if test="${not empty param.target_url}">
        ${controller.start(param.target_url)}
        發送請求中... <br>
        <script type="text/javascript">
            setTimeout(()=>window.location = window.location.origin + window.location.pathname,1000);
        </script>
    </c:if>
    <c:if test="${not empty param.stop}">
        ${controller.stop()}
        發送請求中...<br>
        <script type="text/javascript">
            setTimeout(()=>window.location = window.location.origin + window.location.pathname,1000);
        </script>
    </c:if>
    <c:if test="${empty param.stop and empty param.target_url and not controller.finished}">
        <script type="text/javascript">
            setTimeout(()=>window.location.reload(),1000);
        </script>
    </c:if>
    目標網址:${controller.targetUrl}<br>
    狀態:${controller.finished?"閒置":"正在處理"}<br>
    訪問頁面:${controller.pageCount}<br>
    紀錄:<br>
    ${controller.log}<br>
</div>
</body>
</html>
