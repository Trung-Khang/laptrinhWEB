package com.baitap.profile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.baitap.controller.ProfileController;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import org.junit.jupiter.api.Test;

class ProfileControllerMappingTest {
    @Test
    void profileControllerUsesPlainPostWithoutMultipartInPhaseTwo() {
        WebServlet servlet = ProfileController.class.getAnnotation(WebServlet.class);

        assertNotNull(servlet);
        assertArrayEquals(new String[] {"/profile"}, servlet.urlPatterns());
        assertFalse(ProfileController.class.isAnnotationPresent(MultipartConfig.class));
    }
}
