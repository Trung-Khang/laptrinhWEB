package vn.iotstar.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.http.Part;
import vn.iotstar.util.Constant;

/** Stores validated uploads outside the deployable WAR. */
public final class ImageUploadUtil {
    private ImageUploadUtil() {
    }

    public static String save(Part part, String folder) throws IOException {
        if (part == null || part.getSize() <= 0) {
            throw new IOException("Không có file ảnh để lưu.");
        }
        String submittedName = part.getSubmittedFileName();
        String original = submittedName == null ? "" : Paths.get(submittedName).getFileName().toString();
        String extension = FormValidation.extensionOf(original);
        if (extension.isEmpty()) {
            throw new IOException("Tên file ảnh không hợp lệ.");
        }

        File root = new File(Constant.DIR).getCanonicalFile();
        File directory = new File(root, folder).getCanonicalFile();
        if (!directory.toPath().startsWith(root.toPath())) {
            throw new IOException("Thư mục lưu ảnh không hợp lệ.");
        }
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Không thể tạo thư mục lưu ảnh.");
        }

        File destination = new File(directory, UUID.randomUUID() + "." + extension).getCanonicalFile();
        if (!destination.toPath().startsWith(directory.toPath())) {
            throw new IOException("Đường dẫn lưu ảnh không hợp lệ.");
        }
        part.write(destination.getAbsolutePath());
        return folder + "/" + destination.getName();
    }

    public static void deleteQuietly(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        try {
            File root = new File(Constant.DIR).getCanonicalFile();
            File file = new File(root, relativePath.replace('\\', '/')).getCanonicalFile();
            if (file.toPath().startsWith(root.toPath())) {
                Files.deleteIfExists(file.toPath());
            }
        } catch (IOException ignored) {
            // Preserve the original application exception; this only removes an orphan.
        }
    }
}
