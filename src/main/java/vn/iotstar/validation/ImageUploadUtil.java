package vn.iotstar.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import jakarta.servlet.http.Part;
import vn.iotstar.util.Constant;

/** Chỉ lưu upload sau khi FormValidation đã hoàn tất. */
public final class ImageUploadUtil {
    private ImageUploadUtil() {
    }

    public static String save(Part part, String folder) throws IOException {
        String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String extension = FormValidation.extensionOf(original);
        String newName = System.currentTimeMillis() + "_" + Integer.toHexString(original.hashCode()) + "." + extension;
        File directory = new File(Constant.DIR, folder);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Không thể tạo thư mục lưu ảnh.");
        }
        part.write(new File(directory, newName).getAbsolutePath());
        return folder + "/" + newName;
    }
}
