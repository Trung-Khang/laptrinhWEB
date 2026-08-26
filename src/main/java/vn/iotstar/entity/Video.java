package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Entity Video - bản ghi video thuộc một Category.
 * Quan hệ: Category 1 ──── * Video (ManyToOne).
 *
 * Lưu ý: bảng `videos` được Hibernate tạo tự động (hbm2ddl.auto=update)
 * vì hiện database ShoppingServiceMVC chưa có bảng này.
 */
@Entity
@Table(name = "videos")
@NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videoId")
    private Integer videoId;

    private Boolean active;

    private String description;

    private String poster;

    private String title;

    private Integer views;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Video() {
    }

    public Video(String title, String description, String poster,
                 boolean active, int views, Category category) {
        this.title = title;
        this.description = description;
        this.poster = poster;
        this.active = active;
        this.views = views;
        this.category = category;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Video [videoId=" + videoId + ", active=" + active
                + ", title=" + title + ", views=" + views + "]";
    }
}
