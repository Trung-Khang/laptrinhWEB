package vn.iotstar.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity Category - ánh xạ vào bảng `Category` THỰC TẾ đang dùng
 * trong database ShoppingServiceMVC (cate_id, cate_name, icons).
 *
 * Tên thuộc tính (id/name/icon) được giữ như bean property tương đương
 * với categoryId/categoryname/images của bài tập, đồng thời tương thích
 * với JSP/controller hiện tại (${cate.id}, ${cate.name}, ${cate.icon}).
 *
 * Quan hệ: Category 1 ──── * Video (OneToMany).
 */
@Entity
@Table(name = "Category")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private Integer id;          // tương đương categoryId, map cate_id

    @Column(name = "cate_name")
    private String name;         // tương đương categoryname, map cate_name

    @Column(name = "icons")
    private String icon;         // tương đương images, map icons

    @OneToMany(mappedBy = "category",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    /** Helper: thêm một Video vào danh mục (2 chiều). */
    public void addVideo(Video video) {
        if (video != null) {
            videos.add(video);
            video.setCategory(this);
        }
    }

    /** Helper: gỡ một Video khỏi danh mục (2 chiều). */
    public void removeVideo(Video video) {
        if (video != null) {
            videos.remove(video);
            video.setCategory(null);
        }
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", icon=" + icon + "]";
    }
}
