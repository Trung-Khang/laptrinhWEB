package com.baitap.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Test;

import vn.iotstar.validation.FormValidation;

class FormValidationTest {
    @Test
    void rejectsBlankAndOverlongText() {
        Map<String, String> errors = new LinkedHashMap<>();
        FormValidation.required(errors, "name", "  ", "tên danh mục");
        FormValidation.maxLength(errors, "name", "123456", 5, "Tên danh mục");

        assertEquals("Vui lòng nhập tên danh mục.", errors.get("name"));
        assertEquals(1, errors.size());
    }

    @Test
    void acceptsPositivePriceAndNonNegativeStock() {
        Map<String, String> errors = new LinkedHashMap<>();

        assertEquals("125000.50", FormValidation.positiveDecimal(errors, "price", "125000.50", "Giá").toString());
        assertEquals(0, FormValidation.nonNegativeInteger(errors, "stock", "0", "Tồn kho"));
        assertTrue(errors.isEmpty());
    }

    @Test
    void rejectsZeroPriceNegativeOrDecimalStock() {
        Map<String, String> errors = new LinkedHashMap<>();

        FormValidation.positiveDecimal(errors, "price", "0", "Giá");
        FormValidation.nonNegativeInteger(errors, "stock", "-1", "Tồn kho");
        FormValidation.nonNegativeInteger(errors, "stockDecimal", "1.5", "Tồn kho");

        assertTrue(errors.get("price").contains("lớn hơn 0"));
        assertTrue(errors.get("stock").contains("không được âm"));
        assertTrue(errors.get("stockDecimal").contains("số nguyên"));
    }

    @Test
    void validatesImageExtensionAndUrlRules() {
        Map<String, String> errors = new LinkedHashMap<>();

        assertEquals("png", FormValidation.extensionOf("C:\\fake\\photo.PNG"));
        FormValidation.validateImageUrl(errors, "imageUrl", "javascript:alert(1)");
        assertTrue(errors.get("imageUrl").contains("http://"));
    }

    @Test
    void validatesAccountFieldsAndPasswordConfirmation() {
        Map<String, String> errors = new LinkedHashMap<>();
        FormValidation.username(errors, "username", "bad name");
        FormValidation.email(errors, "email", "invalid-email");
        FormValidation.password(errors, "password", "123", 6);
        FormValidation.confirmation(errors, "confirmPassword", "654321", "123456");
        FormValidation.optionalPhone(errors, "phone", "abc");
        assertTrue(errors.keySet().containsAll(java.util.Set.of("username", "email", "password", "confirmPassword", "phone")));
    }

    @Test
    void acceptsValidAccountFieldsAndOtp() {
        Map<String, String> errors = new LinkedHashMap<>();
        FormValidation.username(errors, "username", "khang.gear");
        FormValidation.email(errors, "email", "user@example.com");
        FormValidation.password(errors, "password", "123456", 6);
        FormValidation.confirmation(errors, "confirmPassword", "123456", "123456");
        FormValidation.optionalPhone(errors, "phone", "0912345678");
        FormValidation.otp(errors, "otp", "123456", 6);
        assertTrue(errors.isEmpty());
    }

    @Test
    void rejectsAnEmailThatExceedsTheDatabaseSafeLength() {
        Map<String, String> errors = new LinkedHashMap<>();
        String email = "a".repeat(250) + "@example.com";

        FormValidation.email(errors, "email", email);

        assertTrue(errors.containsKey("email"));
    }

    @Test
    void validatesCheckoutContactFieldsWithTheSharedRules() {
        Map<String, String> errors = new LinkedHashMap<>();

        FormValidation.required(errors, "fullName", "Nguyen Van A", "ho va ten");
        FormValidation.optionalPhone(errors, "phone", "0912345678");
        FormValidation.email(errors, "email", "customer@example.com");
        FormValidation.required(errors, "address", "1 Duong So 1", "dia chi giao hang");

        assertTrue(errors.isEmpty());
    }

    @Test
    void acceptsOnlySafeAvatarFormats() {
        Map<String, String> valid = new LinkedHashMap<>();
        FormValidation.validateAvatar(valid, "avatar", new TestPart("avatar.webp", "image/webp", 1024), 2L * 1024 * 1024);
        assertTrue(valid.isEmpty());

        Map<String, String> invalid = new LinkedHashMap<>();
        FormValidation.validateAvatar(invalid, "avatar", new TestPart("avatar.gif", "image/gif", 1024), 2L * 1024 * 1024);
        assertTrue(invalid.containsKey("avatar"));
    }

    private record TestPart(String submittedFileName, String contentType, long size) implements Part {
        @Override public InputStream getInputStream() { return InputStream.nullInputStream(); }
        @Override public String getContentType() { return contentType; }
        @Override public String getName() { return "avatar"; }
        @Override public String getSubmittedFileName() { return submittedFileName; }
        @Override public long getSize() { return size; }
        @Override public void write(String fileName) { }
        @Override public void delete() { }
        @Override public String getHeader(String name) { return null; }
        @Override public Collection<String> getHeaders(String name) { return List.of(); }
        @Override public Collection<String> getHeaderNames() { return List.of(); }
    }
}
