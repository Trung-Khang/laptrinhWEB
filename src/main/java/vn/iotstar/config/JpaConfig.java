package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Cấu hình JPA / Hibernate.
 *
 * Tạo MỘT EntityManagerFactory dùng chung (singleton) để tránh tạo lại
 * factory (tốn tài nguyên) cho mỗi thao tác. EntityManager là lightweight
 * nên tạo mới mỗi khi cần và đóng sau khi dùng xong.
 */
public class JpaConfig {

    private static final String PERSISTENCE_UNIT = "jpa-hibernate-mysql";

    // Factory dùng chung cho toàn ứng dụng
    private static final EntityManagerFactory FACTORY;

    // Khởi tạo một lần duy nhất
    static {
        FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    private JpaConfig() {
        // private constructor: không cho khởi tạo
    }

    /** Trả về một EntityManager mới (nhớ close sau khi dùng). */
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    /** Đóng EntityManagerFactory khi ứng dụng tắt. */
    public static void close() {
        if (FACTORY != null && FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
