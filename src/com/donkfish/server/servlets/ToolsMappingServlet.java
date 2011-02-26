package com.donkfish.server.servlets;

import com.donkfish.core.client.constants.HtmlVariables;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ToolsMappingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();

        if(path != null)
            path = path.substring(1);

        req.getRequestDispatcher(String.format("/tools.jsp?%s=%s", HtmlVariables.KEY, path)).forward(req, resp);
    }
}
