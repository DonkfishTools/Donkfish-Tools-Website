<%@ page import="com.donkfish.core.client.constants.HtmlVariables" %>
<%@ page import="com.donkfish.core.client.model.Tool" %>
<%@ page import="com.donkfish.core.client.tools.ToolsManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Tool tool = ToolsManager.getTool(request.getParameter(HtmlVariables.KEY));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<head>
    <title><% out.write(tool.getToolName()); %></title>
    <jsp:include page="ZeroClipboard/_zc.jsp" />
    <script src='/t/t.nocache.js'></script>
    <jsp:include page="_siteStyles.jsp"/>
    <jsp:include page="_ga.jsp"/>

</head>
<body>
<jsp:include page="_header.jsp"/>
<div id="desc">

    <h1><a href="/tools/<% out.write(request.getParameter(HtmlVariables.KEY)); %>"><% out.write(tool.getToolName()); %></a></h1>
    <p>
        <% out.write(tool.getToolDescriptionHtml()); %>
    </p>
    <h2>Related Tools</h2>

    <ul>
        <li><a href="">Some Tool</a></li>
        <li><a href="">Some Tool</a></li>
        <li><a href="">Some Tool</a></li>
        <li><a href="">Some Tool</a></li>
    </ul>
</div>
<input type="hidden" name="<% out.write(HtmlVariables.KEY); %>" id="<% out.write(HtmlVariables.KEY); %>" value="<% out.write(request.getParameter(HtmlVariables.KEY)); %>" />
<div class="bd" id="<% out.write(HtmlVariables.ROOT_PANEL_MAIN); %>">

</div>

</body>
</html>