package com.baitap.api;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.storefront.StorefrontRepository;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import vn.iotstar.entity.Order;

@WebServlet("/api/account/*")
public class AccountApiController extends BaseApiServlet {
    private final UserService userService = new UserServiceImpl();
    private final StorefrontRepository repository = new StorefrontRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = requireUser(request, response); if (user == null) return;
        try {
            String path = request.getPathInfo();
            if ("/profile".equals(path)) { ok(response, StorefrontMapper.profile(user, request.getContextPath())); return; }
            if ("/orders".equals(path)) { ok(response, repository.ordersForUser(user.getId()).stream().map(StorefrontMapper::order).toList()); return; }
            int orderId = orderId(path); Order order = repository.orderForUser(orderId, user.getId());
            if (order == null) { error(response, 404, "Khong tim thay don hang."); return; }
            ok(response, StorefrontMapper.order(order));
        } catch (IllegalArgumentException exception) { error(response, 400, exception.getMessage()); }
        catch (RuntimeException exception) { getServletContext().log("Account API failed", exception); error(response, 500, "Khong the tai thong tin tai khoan."); }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = requireUser(request, response); if (user == null) return;
        try {
            String path = request.getPathInfo();
            if ("/profile".equals(path)) {
                JsonObject body = readBody(request);
                user.setFullName(string(body, "fullName")); user.setEmail(string(body, "email")); user.setPhone(string(body, "phone"));
                userService.updateProfile(user);
                request.getSession().setAttribute("account", user);
                ok(response, StorefrontMapper.profile(user, request.getContextPath())); return;
            }
            if (path != null && path.endsWith("/cancel")) {
                int id = orderId(path.substring(0, path.length() - "/cancel".length()));
                repository.cancelOrder(id, user.getId());
                Order order = repository.orderForUser(id, user.getId()); ok(response, StorefrontMapper.order(order)); return;
            }
            error(response, 404, "Khong tim thay API tai khoan.");
        } catch (IllegalArgumentException | IllegalStateException exception) { error(response, 400, exception.getMessage()); }
        catch (RuntimeException exception) { getServletContext().log("Account update failed", exception); error(response, 500, "Khong the cap nhat thong tin."); }
    }

    private User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user == null) error(response, 401, "Vui long dang nhap de tiep tuc.");
        return user;
    }

    private int orderId(String path) {
        try {
            String[] parts = path.split("/");
            return Integer.parseInt(parts[2]);
        } catch (Exception exception) { throw new IllegalArgumentException("Ma don hang khong hop le."); }
    }
}
