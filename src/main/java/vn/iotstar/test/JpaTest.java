package vn.iotstar.test;

import jakarta.persistence.EntityManager;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;

/**
 * Lớp test JPA / Hibernate theo ví dụ của giảng viên.
 *
 * Kiểm tra:
 *  - Tạo Category          (Iphone)
 *  - Tạo Video             (v01, test)
 *  - Gắn Video vào Category (Category 1 ──── * Video)
 *  - persist Category
 *  - persist Video
 *  - commit transaction
 *  - đóng EntityManager
 *
 * Chạy:  java vn.iotstar.test.JpaTest
 */
public class JpaTest {

    public static void main(String[] args) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Tạo Category: name = Iphone, images = abc.jpg, status = 1 (biểu diễn qua icon)
            Category category = new Category();
            category.setName("Iphone");
            category.setIcon("abc.jpg");
            em.persist(category);           // persist Category trước để có id


            // 2. Tạo Video: title = test (videoId để Hibernate IDENTITY tự sinh)
            Video video = new Video();
            video.setTitle("test");
            video.setDescription("Test video");
            video.setActive(true);
            video.setViews(0);
            video.setCategory(category);    // gắn Video vào Category

            em.persist(video);              // persist Video

            em.getTransaction().commit();

            System.out.println("=== JPA TEST OK ===");
            System.out.println("Category: " + category);
            System.out.println("Video [id=" + video.getVideoId()
                    + ", title=" + video.getTitle()
                    + "] category=" + (video.getCategory() != null
                        ? video.getCategory().getName() : null));

            // 3. Kiểm tra quan hệ 2 chiều
            if (category.getVideos() != null && !category.getVideos().isEmpty()) {
                System.out.println("Category.videos size = " + category.getVideos().size());
            } else {
                System.out.println("Category.videos size = 0 (video chưa nằm trong list quan hệ)");
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            JpaConfig.close();
        }
    }
}
