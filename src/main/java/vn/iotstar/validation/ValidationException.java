package vn.iotstar.validation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Exception chứa lỗi validation theo từng field của form. */
public class ValidationException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Dữ liệu biểu mẫu không hợp lệ.");
        this.errors = Collections.unmodifiableMap(new LinkedHashMap<>(errors));
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
