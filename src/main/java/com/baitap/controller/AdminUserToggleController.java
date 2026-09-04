package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.util.FlashMessage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/admin/user/toggle")
public class AdminUserToggleController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User account = (User) req.getSession(false).getAttribute("account");
            int id = Integer.parseInt(req.getParameter("id")); boolean active = Boolean.parseBoolean(req.getParameter("active"));
            userService.changeActiveByAdmin(account, id, active);
            FlashMessage.success(req, "Cập nhật trạng thái thành công.");
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
        } catch (Exception e) {
            getServletContext().log("Toggle user failed", e);
            FlashMessage.error(req, e.getMessage() == null ? "Không thể cập nhật trạng thái." : e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
        }
    }
}
