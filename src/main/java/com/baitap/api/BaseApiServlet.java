package com.baitap.api;

import com.baitap.api.dto.StorefrontDtos.ApiResponse;
import com.baitap.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

abstract class BaseApiServlet extends HttpServlet {
    protected static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (com.google.gson.JsonSerializer<LocalDateTime>) (value, type, context) ->
                            new com.google.gson.JsonPrimitive(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();

    protected void send(HttpServletResponse response, int status, Object data) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        GSON.toJson(new ApiResponse(status < 400, status < 400 ? null : "Yeu cau khong hop le", data,
                status < 400 ? null : Collections.emptyMap()), response.getWriter());
    }

    protected void ok(HttpServletResponse response, Object data) throws IOException {
        send(response, HttpServletResponse.SC_OK, data);
    }

    protected void created(HttpServletResponse response, Object data) throws IOException {
        send(response, HttpServletResponse.SC_CREATED, data);
    }

    protected void error(HttpServletResponse response, int status, String message) throws IOException {
        error(response, status, message, Collections.emptyMap());
    }

    protected void error(HttpServletResponse response, int status, String message, Map<String, String> fieldErrors) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        GSON.toJson(new ApiResponse(false, message, null, fieldErrors), response.getWriter());
    }

    protected JsonObject readBody(HttpServletRequest request) throws IOException {
        try (Reader reader = request.getReader()) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Du lieu JSON khong hop le.");
        }
    }

    protected String string(JsonObject body, String key) {
        return body.has(key) && !body.get(key).isJsonNull() ? body.get(key).getAsString().trim() : null;
    }

    protected int integer(JsonObject body, String key, int fallback) {
        try { return body.has(key) ? body.get(key).getAsInt() : fallback; }
        catch (RuntimeException exception) { return fallback; }
    }

    protected BigDecimal decimal(HttpServletRequest request, String name) {
        try {
            String value = request.getParameter(name);
            return value == null || value.isBlank() ? null : new BigDecimal(value);
        } catch (NumberFormatException exception) { return null; }
    }

    protected int integer(HttpServletRequest request, String name, int fallback) {
        try { return Integer.parseInt(request.getParameter(name)); }
        catch (Exception exception) { return fallback; }
    }

    protected User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("account");
        return value instanceof User user && user.isActive() ? user : null;
    }
}
