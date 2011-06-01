<%@ page import="com.donkfish.core.client.tools.ToolsManager" %>
<%@ page import="com.donkfish.core.client.model.Tool" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: brandonturner
  Date: 2/25/11
  Time: 7:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<head>
    <title>Simple jsp page</title>
    <jsp:include page="_siteStyles.jsp"/>
    <jsp:include page="_ga.jsp"/>
</head>
<body>
<jsp:include page="_header.jsp"/>
<div style="padding: 10px;">
    <h1>Donkfish Tools a website with has simple and fast tools to get work done without hassle</h1>
    <div style="padding-left: 30px;padding-top:10px;">
    <ul class="tool_list">
        <%

            for(Map.Entry<String, Tool> tool : ToolsManager.getTools().entrySet())
            {
                out.print("<li><a href='/tools/" + tool.getKey() + "'>" + tool.getValue().getToolName() + "</a> - " + tool.getValue().getToolDescriptionHtml() + "</li>");
            }

        %>
    </ul>
        </div>
</div>
</body>
</html>