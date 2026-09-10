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
import java.util.LinkedHashMap;
import vn.iotstar.entity.Order;
import vn.iotstar.validation.FormValidation;

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
            Map<String, String> fieldErrors = validate(checkout);
            if (!fieldErrors.isEmpty()) { error(response, 400, "Thông tin thanh toán chưa hợp lệ.", fieldErrors); return; }
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

    private Map<String, String> validate(CheckoutRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        FormValidation.required(errors, "fullName", request.fullName(), "họ và tên");
        FormValidation.maxLength(errors, "fullName", request.fullName(), 150, "Họ và tên");
        FormValidation.required(errors, "phone", request.phone(), "số điện thoại");
        FormValidation.optionalPhone(errors, "phone", request.phone());
        FormValidation.email(errors, "email", request.email());
        FormValidation.required(errors, "address", request.address(), "địa chỉ giao hàng");
        FormValidation.maxLength(errors, "address", request.address(), 500, "Địa chỉ giao hàng");
        if (!"COD".equals(request.paymentMethod()) && !"BANK_TRANSFER".equals(request.paymentMethod())) errors.put("paymentMethod", "Phương thức thanh toán không hợp lệ.");
        return errors;
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
}
