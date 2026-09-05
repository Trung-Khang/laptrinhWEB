package com.baitap.api;

import com.baitap.api.dto.StorefrontDtos.CartDto;
import com.baitap.api.dto.StorefrontDtos.CartItemDto;
import com.baitap.storefront.StorefrontRepository;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import vn.iotstar.entity.Product;

@WebServlet(urlPatterns = {"/api/storefront/cart", "/api/storefront/cart/items", "/api/storefront/cart/items/*"})
public class StorefrontCartApiController extends BaseApiServlet {
    static final String CART_KEY = "storefrontCart";
    private final StorefrontRepository repository = new StorefrontRepository();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { ok(response, cart(request)); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            JsonObject body = readBody(request); int productId = integer(body, "productId", 0); int quantity = integer(body, "quantity", 1);
            change(request, productId, quantity, false); created(response, cart(request));
        } catch (IllegalArgumentException exception) { error(response, 400, exception.getMessage()); }
        catch (RuntimeException exception) { getServletContext().log("Cart add failed", exception); error(response, 500, "Khong the cap nhat gio hang."); }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = pathProductId(request); JsonObject body = readBody(request);
            change(request, productId, integer(body, "quantity", 0), true); ok(response, cart(request));
        } catch (IllegalArgumentException exception) { error(response, 400, exception.getMessage()); }
        catch (RuntimeException exception) { getServletContext().log("Cart update failed", exception); error(response, 500, "Khong the cap nhat gio hang."); }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try { cartMap(request).remove(pathProductId(request)); ok(response, cart(request)); }
        catch (IllegalArgumentException exception) { error(response, 400, exception.getMessage()); }
    }

    private void change(HttpServletRequest request, int productId, int quantity, boolean replace) {
        if (productId <= 0 || quantity < 1) throw new IllegalArgumentException("San pham hoac so luong khong hop le.");
        Product product = repository.productsByIds(List.of(productId)).get(productId);
        if (product == null || product.getStockQuantity() < 1) throw new IllegalArgumentException("San pham hien het hang.");
        Map<Integer, Integer> cart = cartMap(request);
        int next = replace ? quantity : cart.getOrDefault(productId, 0) + quantity;
        if (next > product.getStockQuantity()) throw new IllegalArgumentException("So luong vuot qua ton kho hien co.");
        cart.put(productId, next);
    }

    CartDto cart(HttpServletRequest request) {
        Map<Integer, Integer> cart = cartMap(request); Map<Integer, Product> products = repository.productsByIds(cart.keySet());
        List<CartItemDto> items = new ArrayList<>(); BigDecimal total = BigDecimal.ZERO; int count = 0;
        for (Map.Entry<Integer, Integer> entry : new ArrayList<>(cart.entrySet())) {
            Product product = products.get(entry.getKey());
            if (product == null) { cart.remove(entry.getKey()); continue; }
            int quantity = Math.min(entry.getValue(), product.getStockQuantity());
            if (quantity < 1) { cart.remove(entry.getKey()); continue; }
            if (quantity != entry.getValue()) cart.put(entry.getKey(), quantity);
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            items.add(new CartItemDto(StorefrontMapper.product(product), quantity, subtotal)); total = total.add(subtotal); count += quantity;
        }
        return new CartDto(items, count, total);
    }

    @SuppressWarnings("unchecked")
    static Map<Integer, Integer> cartMap(HttpServletRequest request) {
        HttpSession session = request.getSession(true); Object value = session.getAttribute(CART_KEY);
        if (value instanceof Map<?, ?>) return (Map<Integer, Integer>) value;
        Map<Integer, Integer> cart = new LinkedHashMap<>(); session.setAttribute(CART_KEY, cart); return cart;
    }

    private int pathProductId(HttpServletRequest request) {
        try { return Integer.parseInt(request.getPathInfo().substring(1)); }
        catch (Exception exception) { throw new IllegalArgumentException("Ma san pham khong hop le."); }
    }
}
