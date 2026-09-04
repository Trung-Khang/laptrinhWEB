package com.baitap.controller;

import com.baitap.model.UserRole;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/admin/user/list")
public class AdminUserListController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FlashMessage.expose(req);
        try {
            Integer roleId = parseRole(req.getParameter("roleid"));
            req.setAttribute("users", userService.search(req.getParameter("keyword"), roleId, parseBoolean(req.getParameter("active"))));
        } catch (RuntimeException e) {
            getServletContext().log("Cannot load admin users", e); req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/views/admin/list-user.jsp").forward(req, resp);
    }
    private Integer parseRole(String value) { try { int role = Integer.parseInt(value); return UserRole.isValid(role) ? role : null; } catch (Exception e) { return null; } }
    private Boolean parseBoolean(String value) { return "true".equals(value) ? Boolean.TRUE : "false".equals(value) ? Boolean.FALSE : null; }
}
