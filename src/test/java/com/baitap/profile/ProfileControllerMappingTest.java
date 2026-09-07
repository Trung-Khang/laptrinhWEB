package com.baitap.profile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.baitap.controller.ProfileController;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import org.junit.jupiter.api.Test;

class ProfileControllerMappingTest {
    @Test
    void profileControllerUsesMultipartConfigInPhaseThree() {
        WebServlet servlet = ProfileController.class.getAnnotation(WebServlet.class);

        assertNotNull(servlet);
        assertArrayEquals(new String[] {"/profile"}, servlet.urlPatterns());
        assertTrue(ProfileController.class.isAnnotationPresent(MultipartConfig.class));

        MultipartConfig config = ProfileController.class.getAnnotation(MultipartConfig.class);
        assertEquals(1024 * 1024, config.fileSizeThreshold());
        assertEquals(2 * 1024 * 1024, config.maxFileSize());
        assertEquals(5 * 1024 * 1024, config.maxRequestSize());
    }
}
