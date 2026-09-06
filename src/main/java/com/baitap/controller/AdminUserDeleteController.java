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

@WebServlet(urlPatterns = "/admin/user/delete")
public class AdminUserDeleteController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            User actor = (User) request.getSession(false).getAttribute("account");
            userService.deleteByAdmin(actor, Integer.parseInt(request.getParameter("id")));
            FlashMessage.success(request, "Đã xóa người dùng.");
        } catch (Exception exception) {
            getServletContext().log("Delete user failed", exception);
            FlashMessage.error(request, exception.getMessage() == null ? "Không thể xóa người dùng." : exception.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/user/list");
    }
}
