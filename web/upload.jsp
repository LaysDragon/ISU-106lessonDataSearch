<%--
  Created by IntelliJ IDEA.
  User: lays7
  Date: 2017/10/5
  Time: 下午 02:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上傳檔案</title>
</head>
<body>
<div>
</div>
<div id="menu" style="border: solid;float:left;width: 15%;height: 100%;">
    <%@include file="/WEB-INF/jspf/menu.jspf"%>
</div>
<div id="content" style="border: solid;float:right;width: 80%;height: 100%;padding: 20px">
    <label for="docs">選擇要上傳的檔案</label><br>
    <form action="/docs_upload" method="post" enctype="multipart/form-data">
        <input type="file" name="docs" id="docs" multiple="true" ><br>
        <input type="submit">
    </form>
</div>
</body>
</html>

