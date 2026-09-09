package com.baitap.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

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
}
