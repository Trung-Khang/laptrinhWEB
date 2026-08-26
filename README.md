# 🛒 Báo Cáo Đồ Án: Hệ Thống Quản Trị Cửa Hàng (Java Web)

**Sinh viên thực hiện:** Nguyễn Trung Khang
**MSSV:** 24133028
**Học phần:** Lập trình Web (Bài tập 01 - JPA API & Servlet MVC)

---

## 1. Giới thiệu Dự án & Kết quả đạt được
Đây là một hệ thống Web Quản trị (Admin Dashboard) được xây dựng trên nền tảng Java Web. Dự án cung cấp luồng xác thực người dùng (Login/Logout) bảo mật và hệ thống quản lý danh mục sản phẩm (CRUD Category) với giao diện hiện đại, thân thiện.

###  Những thành quả nổi bật đã đạt được:
1. **Kiến trúc Backend chuẩn MVC & JPA:** Chuyển đổi thành công toàn bộ module Quản lý Danh mục từ JDBC truyền thống sang công nghệ **JPA/Hibernate ORM**.
2. **Quản lý tài nguyên tối ưu:** Áp dụng mô hình `try-with-resources` để đóng kết nối database, xử lý triệt để lỗi nghẽn cổ chai (Connection Leak) và độ trễ đăng nhập bằng cách ép kết nối qua IPv4 (`127.0.0.1`).
3. **Upload File chuẩn Jakarta:** Loại bỏ hoàn toàn sự phụ thuộc vào thư viện ngoài (`commons-fileupload2`), thay vào đó sử dụng tính năng **Native `@MultipartConfig`** của Servlet 3.0+ để xử lý upload hình ảnh mượt mà trên Tomcat 11.
4. **Giao diện Premium (UI/UX):** "Lột xác" hoàn toàn trang quản trị bằng **Bootstrap 5, FontAwesome 6 và Google Fonts (Poppins)**. Xây dựng bố cục Dashboard chuyên nghiệp với Sidebar Gradient, Card đổ bóng và các nút thao tác trực quan.

---

## 2. Báo cáo Quy trình Nâng cấp Hệ thống
Dự án đã trải qua một quá trình nâng cấp toàn diện từ thiết kế cơ bản ban đầu:

### Giai đoạn 1: Tối ưu UI/UX Giao diện Quản trị
* **Nâng cấp Layout:** Chuyển đổi từ file HTML thô sơ sang cấu trúc Flexbox/Grid hiện đại gồm Sidebar cố định và Main Content động.
* **Hoàn thiện Frontend:** Tích hợp Bootstrap 5 để làm Form nhập liệu, Bảng dữ liệu (Table), và thông báo báo lỗi trở nên chuyên nghiệp và tương thích với mọi thiết bị.

### Giai đoạn 2: Nâng cấp Công nghệ lõi (JPA / Hibernate API)
* **Cấu hình Persistence:** Tích hợp `hibernate-core 6.6.1.Final` và thiết lập `persistence.xml` chuẩn cho SQL Server.
* **Ánh xạ Thực thể (Entity Mapping):**
  * Chuyển POJO `Category` thành `@Entity`.
  * Khởi tạo thực thể `@Entity Video` mới để thiết lập mối quan hệ **1-Nhiều (@OneToMany)** giữa `Category` và `Video`.
* **Auto-DDL:** Sử dụng tính năng `hbm2ddl.auto=update` của Hibernate, hệ thống đã **tự động khởi tạo bảng `videos`** trong CSDL SQL Server cùng các khóa ngoại (`categoryId`) mà không cần can thiệp thủ công.
* **Refactor Logic:** Thay thế toàn bộ mã SQL thuần túy bằng các phương thức của `EntityManager` (`persist`, `merge`, `remove`) và truy vấn `JPQL` (`NamedQuery`).

---

## 3. Công nghệ & Cấu trúc Database

* **Ngôn ngữ & Framework:** Java 21, Jakarta EE (Servlet/JSP/JSTL), Maven.
* **ORM:** JPA (Hibernate 6.6.1).
* **Database:** SQL Server (JDBC Driver).
* **Server:** Apache Tomcat 11.
* **Cơ sở dữ liệu (Nằm trong thư mục `/sql`):**
  1. `DB_LapTrinhWeb.sql`: Chứa bảng User (Dùng cho chức năng Đăng nhập - tài khoản mẫu: `admin` / `123`).
  2. `ShoppingServiceMVC.sql`: Chứa bảng Category và Video (Dùng cho module Quản lý JPA).

---

## 4. Hướng dẫn Chạy dự án (Deployment)

**Yêu cầu hệ thống trước khi chạy:**
* JDK 21+ và Maven đã được cài đặt, cấu hình biến môi trường đầy đủ.
* Đã cài đặt Apache Tomcat 11.
* SQL Server đang chạy và đã **import 2 file SQL** trong thư mục `/sql`.
* *Lưu ý Port:* Dự án đang dùng port DB `127.0.0.1:52282`. Vui lòng sửa lại port trong file `DBConnection.java` và `persistence.xml` (thường là `1433`) cho khớp với máy của bạn.

### Cách 1: Chạy bằng Eclipse (Khuyến nghị cho Giảng viên chấm bài)
1. Mở Eclipse, chọn **File** -> **Import** -> **Maven** -> **Existing Maven Projects**.
2. Trỏ đường dẫn đến thư mục chứa file `pom.xml` của project và nhấn Finish.
3. Chờ Maven tải xong thư viện. Ở tab *Servers* phía dưới, đảm bảo đã add Tomcat 11.
4. Click chuột phải vào Project -> Chọn **Run As** -> **Run on Server**.
5. Chọn Tomcat 11, nhấn Finish. Trình duyệt nội bộ sẽ tự động mở trang web.

### Cách 2: Chạy bằng VS Code
1. Mở VS Code, chọn **File** -> **Open Folder** và trỏ đến thư mục chứa file `pom.xml`.
2. Mở Terminal (`Ctrl + ~`) và gõ lệnh build mã nguồn:
   ```bash
   mvn clean package -DskipTests
