package com.baitap.api;

import com.baitap.api.dto.StorefrontDtos.CheckoutRequest;
import com.baitap.model.User;
import com.baitap.model.UserRole;
import com.baitap.storefront.StorefrontRepository;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import vn.iotstar.entity.Order;

@WebServlet("/api/storefront/checkout")
public class StorefrontCheckoutApiController extends BaseApiServlet {
    private final StorefrontRepository repository = new StorefrontRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user == null) { error(response, 401, "Vui long dang nhap de thanh toan."); return; }
        if (user.getRoleid() != UserRole.CUSTOMER) { error(response, 403, "Chi tai khoan khach hang moi co the dat hang."); return; }
        try {
            JsonObject body = readBody(request);
            CheckoutRequest checkout = new CheckoutRequest(string(body, "fullName"), string(body, "phone"), string(body, "email"),
                    string(body, "address"), string(body, "note"), string(body, "paymentMethod"));
            validate(checkout);
            Map<Integer, Integer> cart = StorefrontCartApiController.cartMap(request);
            Order order = repository.checkout(user, checkout, Map.copyOf(cart));
            cart.clear();
            created(response, Map.of("orderId", order.getId(), "status", order.getStatus(), "totalAmount", order.getTotalAmount()));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            error(response, 400, exception.getMessage());
        } catch (RuntimeException exception) {
            getServletContext().log("Checkout failed", exception);
            error(response, 500, "Khong the tao don hang luc nay. Gio hang cua ban van duoc giu lai.");
        }
    }

    private void validate(CheckoutRequest request) {
        if (blank(request.fullName()) || request.fullName().length() > 150) throw new IllegalArgumentException("Vui long nhap ho ten hop le.");
        if (blank(request.phone()) || !request.phone().matches("^[0-9+ -]{8,20}$")) throw new IllegalArgumentException("So dien thoai khong hop le.");
        if (blank(request.email()) || !request.email().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("Email khong hop le.");
        if (blank(request.address()) || request.address().length() > 500) throw new IllegalArgumentException("Vui long nhap dia chi giao hang.");
        if (!"COD".equals(request.paymentMethod()) && !"BANK_TRANSFER".equals(request.paymentMethod())) throw new IllegalArgumentException("Phuong thuc thanh toan khong hop le.");
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
}
