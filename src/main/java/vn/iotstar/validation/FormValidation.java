package vn.iotstar.validation;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.Part;

/** Các quy tắc validation dùng chung cho form Servlet/JSP. */
public final class FormValidation {
    public static final long DEFAULT_IMAGE_MAX_BYTES = 5L * 1024 * 1024;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> IMAGE_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    private FormValidation() {
    }

    public static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    public static void required(Map<String, String> errors, String field, String value, String label) {
        if (trim(value).isEmpty()) {
            errors.putIfAbsent(field, "Vui lòng nhập " + label + ".");
        }
    }

    public static void maxLength(Map<String, String> errors, String field, String value, int max, String label) {
        if (value != null && value.trim().length() > max) {
            errors.putIfAbsent(field, label + " không được vượt quá " + max + " ký tự.");
        }
    }

    public static BigDecimal positiveDecimal(Map<String, String> errors, String field, String value, String label) {
        String normalized = trim(value);
        if (normalized.isEmpty()) {
            errors.putIfAbsent(field, "Vui lòng nhập " + label + ".");
            return null;
        }
        try {
            BigDecimal number = new BigDecimal(normalized);
            if (number.compareTo(BigDecimal.ZERO) <= 0) {
                errors.putIfAbsent(field, label + " phải lớn hơn 0.");
                return null;
            }
            return number;
        } catch (NumberFormatException ex) {
            errors.putIfAbsent(field, label + " phải là số hợp lệ.");
            return null;
        }
    }

    public static Integer nonNegativeInteger(Map<String, String> errors, String field, String value, String label) {
        String normalized = trim(value);
        if (normalized.isEmpty()) {
            errors.putIfAbsent(field, "Vui lòng nhập " + label + ".");
            return null;
        }
        try {
            if (normalized.contains(".") || normalized.contains(",")) {
                throw new NumberFormatException();
            }
            int number = Integer.parseInt(normalized);
            if (number < 0) {
                errors.putIfAbsent(field, label + " không được âm.");
                return null;
            }
            return number;
        } catch (NumberFormatException ex) {
            errors.putIfAbsent(field, label + " phải là số nguyên không âm.");
            return null;
        }
    }

    public static void validateImage(Map<String, String> errors, String field, Part part, long maxBytes) {
        if (part == null || part.getSize() == 0) {
            return;
        }
        if (part.getSize() > maxBytes) {
            errors.putIfAbsent(field, "Ảnh không được lớn hơn " + (maxBytes / (1024 * 1024)) + " MB.");
            return;
        }
        String submittedName = part.getSubmittedFileName();
        String extension = extensionOf(submittedName);
        String contentType = part.getContentType() == null ? "" : part.getContentType().toLowerCase();
        if (!IMAGE_EXTENSIONS.contains(extension) || !IMAGE_CONTENT_TYPES.contains(contentType)) {
            errors.putIfAbsent(field, "Ảnh phải có định dạng JPG, PNG, GIF hoặc WEBP hợp lệ.");
        }
    }

    public static void validateImageUrl(Map<String, String> errors, String field, String value) {
        String url = trim(value);
        if (url.isEmpty()) return;
        if (url.length() > 500 || !(url.startsWith("http://") || url.startsWith("https://"))) {
            errors.putIfAbsent(field, "URL ảnh phải bắt đầu bằng http:// hoặc https:// và không quá 500 ký tự.");
        }
    }

    public static String extensionOf(String fileName) {
        if (fileName == null) return "";
        String safeName = fileName.replace('\\', '/');
        int slash = safeName.lastIndexOf('/');
        int dot = safeName.lastIndexOf('.');
        if (dot <= slash || dot == safeName.length() - 1) return "";
        return safeName.substring(dot + 1).toLowerCase();
    }
}
