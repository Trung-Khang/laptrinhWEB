BÀI TẬP MÔN LẬP TRÌNH WEB: Hệ Thống Quản Trị & Cửa Hàng KhangGear (Java Web)

**Sinh viên thực hiện:** Nguyễn Trung Khang  
**MSSV:** 24133028  
**Học phần:** Lập trình Web  

---

Tài khoản đăng nhập quyền quản trị viên/quản lý test thử: admin|pass: 123 hoặc manager|pass: 123
Tài khoản User tự tạo và phải điền email thật để hệ thống gửi otp kích hoạt tài khoản

---

## Mục Lục
1. [Tổng quan Kiến trúc Hệ thống](#1-tổng-quan-kiến-trúc-hệ-thống)
2. [Công nghệ Frontend & Vị trí Thể hiện trên Giao diện (UI/View)](#2-công-nghệ-frontend--vị-trí-thể-hiện-trên-giao-diện-uiview)
3. [Báo cáo Quy trình Phát triển Hệ thống](#3-báo-cáo-quy-trình-phát-triển-hệ-thống)
4. [Công nghệ & Cấu trúc Cơ sở Dữ liệu](#4-công-nghệ--cấu-trúc-cơ-sở-dữ-liệu)
5. [Hướng dẫn Chạy & Triển khai Dự án (Deployment Guide)](#5-hướng-dẫn-chạy--triển-khai-dự-án-deployment-guide)
6. [Nhật ký Cập nhật & Tinh chỉnh](#6-nhật-ký-cập-nhật--tinh-chỉnh)

---

## 1. Tổng quan Kiến trúc Hệ thống

Dự án là một hệ thống Web Thương mại Điện tử & Quản trị toàn diện được xây dựng theo mô hình kết hợp hiện đại:
* **Backend:** Nền tảng Java Web chuẩn kiến trúc MVC (Model - View - Controller), sử dụng **Jakarta EE Servlet**, **JPA / Hibernate ORM** kết nối **SQL Server**, chạy trên **Apache Tomcat 11**.
* **Admin Dashboard:** Xây dựng bằng **JSP/JSTL, Bootstrap 5, FontAwesome 6** phục vụ các tác vụ quản lý sản phẩm, danh mục, đơn hàng và thống kê.
* **Customer Storefront (KhangGear):** Ứng dụng Single Page Application (SPA) hiện đại được xây dựng bằng **React + Vite**, cung cấp trải nghiệm mua sắm mượt mà, đồng bộ và giao diện chủ đề công nghệ cao cấp.

```
                   +-----------------------------------------------+
                   |           Client Browser (Người dùng)         |
                   +-----------------------+-----------------------+
                                           |
                    +----------------------+---------------------+
                    |                                            |
           (Storefront SPA)                                 (Admin UI)
        React 18 + Vite + CSS3                         JSP + Bootstrap 5 + JSTL
                    |                                            |
                    +----------------------+---------------------+
                                           | HTTP Requests (Session Cookie)
                                           v
                   +-----------------------------------------------+
                   |       Apache Tomcat 11 (Context: /dangnhap)   |
                   +-----------------------------------------------+
                    | [Filter Layer]                                |
                    |  - CharacterEncodingFilter (UTF-8)            |
                    |  - AdminAuthFilter (Phân quyền Admin/Manager) |
                    |  - StorefrontCorsFilter                       |
                    |  - ConfigurableSiteMeshFilter (SiteMesh 3)    |
                    |                                               |
                    | [Controller Layer]                            |
                    |  - RESTful API Servlets (/api/storefront/*,   |
                    |    /api/account/*)                            |
                    |  - ProfileController (/profile - Multipart)   |
                    |  - Admin MVC Servlets (/admin/*)              |
                    |  - Auth Controllers (/login, /logout, OTP)    |
                    +-----------------------------------------------+
                                            |
                                            v
                    +-----------------------------------------------+
                    | [Service & DAO Layer]                         |
                    |  - JPA / Hibernate 6.6.1 ORM (EntityManager)  |
                    |  - JpaProfileRepository (JPA Profile Update)  |
                    |  - Native JDBC DAO (User Authentication)      |
                    |  - StorefrontRepository & DTO Mappers         |
                    +-----------------------------------------------+
                                            |
                                            v
                    +-----------------------------------------------+
                    |             Microsoft SQL Server              |
                    | (DB_LapTrinhWeb & ShoppingServiceMVC)         |
                    |                                               |
                    | Scripts (sql/):                               |
                    | - DB_LapTrinhWeb.sql                          |
                    | - ShoppingServiceMVC.sql                      |
                    | - 02-normalize-category-encoding.sql          |
                    | - 03-storefront-checkout-migration.sql        |
                    | - 04-fix-vietnamese-question-marks.sql        |
                    | - 05-product-pagination-and-account-otp.sql   |
                    | - 06-user-profile-jpa-migration.sql           |
                    +-----------------------------------------------+
```

---

## 2. Công nghệ Frontend & Vị trí Thể hiện trên Giao diện (UI/View)

Toàn bộ trải nghiệm mua sắm của khách hàng tại Cửa hàng KhangGear được thiết kế theo phong cách công nghệ cao tông màu lạnh. Dưới đây là bảng tổng hợp các công nghệ Frontend cùng vị trí biểu hiện trực quan cụ thể trên màn hình mà người dùng nhìn thấy:

### 2.1. Bảng đối chiếu Công nghệ và Vị trí hiển thị trên View

| Công nghệ | Vai trò kỹ thuật | Vị trí Thể hiện Trực quan trên Giao diện (View người nhìn thấy) |
| :--- | :--- | :--- |
| **React 18** | Nền tảng Single Page Application (SPA), cập nhật dữ liệu động theo trạng thái. | **Toàn bộ trải nghiệm tương tác:** Chuyển đổi giữa các trang (Trang chủ, Chi tiết sản phẩm, Thanh toán, Đơn hàng) diễn ra tức thì, không bị chớp hay tải lại trang web; **Giỏ hàng trượt (Drawer)** lướt êm từ bên phải màn hình khi bấm vào icon giỏ hàng; danh sách sản phẩm tự động lọc theo thời gian thực khi bấm chọn danh mục hoặc khoảng giá. |
| **Vite 6** | Bộ công cụ đóng gói và biên dịch tối ưu hiệu năng cao. | **Tốc độ hiển thị:** Giúp toàn bộ giao diện, hình ảnh và hiệu ứng đồ họa tải lên gần như lập tức khi người dùng truy cập trang web, tối ưu dung lượng tải trang mượt mà trên trình duyệt. |
| **React Router DOM** | Quản lý điều hướng mượt mà phía Client. | **Thanh địa chỉ URL & Điều hướng:** Thay đổi đường dẫn trực tiếp trên thanh địa chỉ của trình duyệt (`/home`, `/checkout`, `/orders`...) theo từng thao tác bấm của người dùng mà không cần gửi request reload lại toàn bộ trang từ máy chủ. |
| **Custom SVG Vector Graphics** | Đồ họa vector vi mạch thiết kế riêng theo chủ đề công nghệ. | **1. Biểu tượng danh mục:** Hiển thị trọn bộ icon công nghệ độc quyền cho từng danh mục sản phẩm (Hub Type-C 7-in-1, Tay cầm gaming, Bàn phím cơ RGB, Chuột công thái học, Màn hình, Tai nghe, Laptop, Cáp sạc...) đặt trong huy hiệu tròn phát sáng.<br>**2. Khung ảnh sản phẩm (Product Showcase):** Với các sản phẩm chưa có ảnh chụp thực tế hoặc ảnh placeholder, hiển thị khung tranh đồ họa công nghệ vi mạch với huy hiệu `TECH SPEC` tinh xảo thay vì ô trắng đơn điệu. |
| **Modern CSS3 (Tech Theme)** | Thiết kế bố cục, màu sắc và hiệu ứng thị giác tông màu lạnh. | **1. Nền trang (Background):** Họa tiết lưới kỹ thuật số **Blueprint Grid** phối cùng các vệt sáng phát quang Ambient Glow (Xanh Neon & Cyan) tạo chiều sâu công nghệ.<br>**2. Banner chính (Hero):** Căn chỉnh bố cục chữ *"Thiết bị tốt cho mọi setup."* và từ ghép *"tận hưởng"* nằm liền mạch không bị ngắt dòng rơi chữ; viền phát sáng bao quanh banner.<br>**3. Thẻ sản phẩm & Danh mục:** Hiệu ứng kính mờ (Glassmorphism), viền phản quang và chuyển động nổi 3D mượt mà khi rê chuột. |
| **Lucide React** | Bộ icon thao tác giao diện hiện đại, tối giản. | **Thanh điều hướng & Chân trang:** Kính lúp ở ô tìm kiếm, icon Giỏ hàng có **huy hiệu đỏ đếm số lượng** sản phẩm đang chọn, icon Tài khoản cá nhân; các huy hiệu cam kết dưới chân trang (Xe giao hàng, Khiên bảo hành chính hãng, Tai nghe hỗ trợ 24/7). |
| **Text Sanitizer Engine** | Bộ xử lý làm sạch và chuẩn hóa chuỗi dữ liệu tiếng Việt. | **Văn bản hiển thị trên toàn màn hình:** Khử sạch hoàn toàn các lỗi font dấu hỏi "?" khó chịu, giúp mọi tên sản phẩm và danh mục hiển thị tròn vành rõ chữ (ví dụ: hiển thị chuẩn *"Tay cầm Xbox"*, *"tạo thử test"*, *"bàn phím cơ"* thay vì bị lỗi dấu hỏi). |

---

### 2.2. Chi tiết các vị trí hiển thị trên Màn hình Người dùng (Storefront View)

#### 1. Khu vực Nền toàn trang (Background Canvas)
* **Vị trí nhìn thấy:** Trải rộng khắp toàn bộ không gian phía sau nội dung trang web từ trên xuống dưới.
* **Cách thể hiện:**
  - Họa tiết lưới kỹ thuật số **Tech Blueprint Grid** (ô vuông 40px $\times$ 40px) mang phong cách phòng thí nghiệm công nghệ.
  - Các quầng sáng phát quang đa điểm màu lạnh (Cyan Neon `#38bdf8`, Electric Blue `#0284c7`, Deep Navy `#061d36`) tạo cảm giác không gian đa chiều, hiện đại và cao cấp, không bị đơn điệu như nền trắng hoặc xám phẳng truyền thống.

#### 2. Khu vực Banner chính (Hero Section)
* **Vị trí nhìn thấy:** Nằm ngay đầu trang chủ, là điểm nhấn thị giác đầu tiên khi khách hàng vừa truy cập.
* **Cách thể hiện:**
  - **Bố cục chữ liền mạch:** Câu khẩu hiệu chính *"Thiết bị tốt cho mọi setup."* được giữ trọn vẹn trên 1 dòng duy nhất, không bị rớt chữ "setup" lẻ loi xuống dòng dưới.
  - **Dòng mô tả tự nhiên:** Từ ghép tiếng Việt *"tận hưởng"* được gắn kết liền khối, không bị tách rời chữ "tận" ở cuối dòng và "hưởng" ở đầu dòng.
  - **Khung viền công nghệ:** Khối banner được bao bọc bởi dải viền phát quang ánh xanh cyan và hiệu ứng bóng đổ sâu.

#### 3. Khu vực Danh mục Sản phẩm (Categories Grid)
* **Vị trí nhìn thấy:** Dãy ô chọn danh mục nằm ngay dưới Banner chính trên Trang chủ.
* **Cách thể hiện:**
  - Ứng dụng công nghệ **Custom SVG Vector** để hiển thị trọn bộ icon mô phỏng thiết bị công nghệ chân thực:
    - *Hub Type-C 7-in-1:* Vỏ nhôm nguyên khối, dây bọc dù, đầu cắm Type-C, cổng USB 3.0 xanh SuperSpeed và đèn LED báo nguồn.
    - *Tay cầm chơi game:* Form công thái học phong cách Xbox/PlayStation với dải LED phát sáng, cần analog đôi và nút bấm ABXY.
    - *Bàn phím cơ:* Layout 75% phím gõ nổi bật với keycap cyber và dải đèn nền RGB.
    - *Chuột gaming:* Thân chuột vát góc khí động học, con lăn phát quang và nút chỉnh DPI.
    - *Màn hình cong, Laptop mỏng nhẹ, Tai nghe chụp tai, Pin sạc dự phòng, Ghế gaming, Cáp sạc nhanh...*
  - Mỗi icon được đặt trong huy hiệu hình tròn với hiệu ứng ánh sáng tỏa tròn (`radial-gradient`), phóng to nhẹ nhàng khi khách hàng rê chuột vào.

#### 4. Khu vực Thẻ sản phẩm & Khung trưng bày (Product Showcase)
* **Vị trí nhìn thấy:** Lưới danh sách sản phẩm trên Trang chủ và trang Danh mục hàng hóa.
* **Cách thể hiện:**
  - **Khung trưng bày Tech Showcase:** Đối với các mặt hàng mới nhập chưa kịp chụp ảnh thật hoặc sản phẩm đang dùng ảnh mẫu placeholder, hệ thống tự động hiển thị khung minh họa thiết bị vector công nghệ cao tương ứng với tên sản phẩm, đi kèm họa tiết vi mạch chìm và huy hiệu `TECH SPEC` góc trên. Người dùng sẽ luôn thấy khung hình chỉn chu, chuyên nghiệp thay vì một ô trống trơn hay ảnh gãy.
  - **Thẻ sản phẩm (Product Card):** Ứng dụng hiệu ứng kính mờ (Glassmorphism), viền phản quang và tự động nâng thẻ đổ bóng khi rê chuột, tạo cảm giác chạm nổi sống động.

#### 5. Thanh Điều hướng (Header) & Khung Giỏ hàng Trượt (Cart Drawer)
* **Vị trí nhìn thấy:** Cố định ở đầu màn hình và trượt ra từ mép phải khi thao tác.
* **Cách thể hiện:**
  - Icon giỏ hàng hiển thị huy hiệu tròn màu đỏ tự động nhảy số lượng khi khách hàng bấm "Thêm vào giỏ".
  - Bấm vào giỏ hàng sẽ kích hoạt **Khung trượt Drawer (React SPA)** từ mép phải màn hình: Khách hàng có thể tăng giảm số lượng, xem tổng tiền và chuyển sang trang Thanh toán ngay mà không cần tải lại trang.

#### 6. Khối Cam kết Dịch vụ & Chân trang (Footer)
* **Vị trí nhìn thấy:** Nằm ở phần đáy của trang web.
* **Cách thể hiện:**
  - Hàng huy hiệu cam kết dịch vụ với các icon tối giản từ Lucide: Giao hàng hỏa tốc, Bảo hành chính hãng 100%, Đổi trả trong 7 ngày, Tư vấn kỹ thuật 24/7.
  - Khối chân trang thiết kế theo tông xanh đêm sâu thẳm (Deep Navy `#07192f`) với đường chỉ viền cyan thanh lịch, cung cấp thông tin liên hệ và chính sách cửa hàng.

---

## 3. Báo cáo Quy trình Phát triển Hệ thống

Dự án đã trải qua một quá trình nâng cấp toàn diện từ thiết kế cơ bản ban đầu:

### Giai đoạn 1: Tối ưu UI/UX Giao diện Quản trị
* **Nâng cấp Layout:** Chuyển đổi từ file HTML thô sơ sang cấu trúc Flexbox/Grid hiện đại gồm Sidebar cố định và Main Content động.
* **Hoàn thiện Frontend:** Tích hợp Bootstrap 5 để làm Form nhập liệu, Bảng dữ liệu (Table), và thông báo lỗi trở nên chuyên nghiệp và tương thích với mọi thiết bị.

### Giai đoạn 2: Nâng cấp Công nghệ lõi (JPA / Hibernate API)
* **Cấu hình Persistence:** Tích hợp `hibernate-core 6.6.1.Final` và thiết lập `persistence.xml` chuẩn cho SQL Server.
* **Ánh xạ Thực thể (Entity Mapping):**
  * Chuyển POJO `Category` thành `@Entity`.
  * Khởi tạo thực thể `@Entity Video` mới để thiết lập mối quan hệ **1-Nhiều (@OneToMany)** giữa `Category` và `Video`.
* **Auto-DDL:** Sử dụng tính năng `hbm2ddl.auto=update` của Hibernate, hệ thống đã **tự động khởi tạo bảng `videos`** trong CSDL SQL Server cùng các khóa ngoại (`categoryId`) mà không cần can thiệp thủ công.
* **Refactor Logic:** Thay thế toàn bộ mã SQL thuần túy bằng các phương thức của `EntityManager` (`persist`, `merge`, `remove`) và truy vấn `JPQL` (`NamedQuery`).

### Giai đoạn 3: Phát triển Storefront Khách hàng Hiện đại (React + Vite)
* Xây dựng giao diện thương mại điện tử Single Page Application (SPA) với đầy đủ tính năng: Xem danh mục, danh sách sản phẩm, bộ lọc đa tiêu chí, giỏ hàng Drawer, trang thanh toán và lịch sử đơn hàng.
* Tinh chỉnh bố cục văn bản, hiện đại hóa bộ icon thiết bị công nghệ vector và chuẩn hóa toàn diện giao diện tông màu lạnh.

---

## 4. Công nghệ & Cấu trúc Cơ sở Dữ liệu

### 4.1. Ngăn xếp Công nghệ (Tech Stack)
* **Backend:** Java 17, Jakarta EE (Servlet 6.0, JSP, JSTL), Maven.
* **UI Decorator & Layout Management:** SiteMesh 3.2.1 (`org.sitemesh:sitemesh:3.2.1` tương thích Jakarta EE 10 / Tomcat 11).
* **Multipart File Upload:** Jakarta Servlet 6.0 `@MultipartConfig` & `jakarta.servlet.http.Part` (giới hạn dung lượng, MIME whitelist, extension whitelist, chống Path Traversal).
* **ORM & Persistence:** JPA 3.1 / Hibernate ORM 6.6.1.Final (`EntityManager`, `EntityTransaction`, `@Entity User` mapping schema `dbo.[User]`).
* **Database:** Microsoft SQL Server 2022 / Express (JDBC Driver 12.4.2).
* **Application Server:** Apache Tomcat 11.0.25 (hỗ trợ Jakarta Servlet 6.0).
* **Bảo mật & Mã hóa:** BCrypt Password Hashing (`org.mindrot:jbcrypt:0.4`), SecureRandom SHA-256 OTP tokens, Admin Role Auth Filter, Whitelist MIME validation.
* **Email & Thông báo:** Jakarta Mail / Angus Mail (gửi OTP kích hoạt tài khoản và quên mật khẩu).
* **Frontend:** React 18, Vite 6, React Router DOM v6, Lucide React, Modern CSS3 Tech Theme.
* **REST APIs:** `/api/storefront/*` (danh mục, sản phẩm, giỏ hàng, đặt hàng), `/api/account/*` (hồ sơ, đơn hàng cá nhân).

### 4.2. Danh mục File Cơ sở Dữ liệu (Thư mục `/sql`)
1. **`DB_LapTrinhWeb.sql`:** Script database cũ (chứa bảng users phục vụ kiểm thử tương thích ngược).
2. **`ShoppingServiceMVC.sql`:** Database chính của hệ thống, chứa cấu trúc bảng `dbo.[User]`, `Category`, `videos`, `products`, `orders`, `order_items` và dữ liệu mẫu của cửa hàng.
3. **`02-normalize-category-encoding.sql`:** Script xử lý hợp nhất các danh mục bị lỗi mã hóa font tiếng Việt ban đầu.
4. **`03-storefront-checkout-migration.sql`:** Script bổ sung các cột cần thiết phục vụ quy trình đặt hàng và thanh toán (`email`, `payment_method`, `payment_status`).
5. **`04-fix-vietnamese-question-marks.sql`:** Script chuẩn hóa trực tiếp các bản ghi bị dấu hỏi "?" trong cơ sở dữ liệu (`Tay cầm Xbox Wireless`, `tạo thử test`...).
6. **`05-product-pagination-and-account-otp.sql`:** Script tạo bảng `account_otps`, bổ sung cột `email_verified` cho `User`, backfill `created_at` cho `products` và tạo index sắp xếp mới nhất.
7. **`06-user-profile-jpa-migration.sql`:** Script bổ sung idempotent các cột `phone NVARCHAR(30)`, `avatar NVARCHAR(500)` và chuẩn hóa cột `fullname NVARCHAR(255)` trong bảng `dbo.[User]` để hiển thị tiếng Việt có dấu chuẩn Unicode.

---

## 5. Hướng dẫn Chạy & Triển khai Dự án (Deployment Guide)

### 5.1. Yêu cầu hệ thống trước khi chạy
* JDK 21+ và Apache Maven đã được cấu hình biến môi trường đầy đủ (`JAVA_HOME`, `M2_HOME`).
* Node.js v18+ và npm (dành cho phát triển frontend).
* Đã cài đặt Apache Tomcat 11.
* SQL Server đang hoạt động và đã chạy các file SQL trong thư mục `/sql`.
* *Lưu ý Port DB:* Dự án đang cấu hình port `127.0.0.1:52282`. Vui lòng chỉnh lại port trong file [DBConnection.java]và [persistence.xml](thường là `1433`)cho khớp với cấu hình máy của bạn.

---

### 5.2. Hướng dẫn Chạy Storefront Frontend (Dành cho Lập trình viên)
Khi muốn phát triển giao diện phía client độc lập với live-reload cực nhanh:

```powershell
# Di chuyển vào thư mục frontend và cài đặt thư viện
cd frontend
npm install

# Khởi chạy Vite dev server (cổng 5173)
npm run dev
```

* Mở trình duyệt tại địa chỉ: `http://localhost:5173/home`.
* Frontend tự động kết nối API về Tomcat tại `http://localhost:8080/dangnhap` và sử dụng session cookie hiện có.

Khi muốn đóng gói (build) giao diện vào ứng dụng Java Web:
```powershell
cd frontend
npm run build
```
> **Cơ chế hoạt động:** Lệnh `npm run build` sẽ tự động biên dịch toàn bộ mã nguồn React sang HTML/CSS/JS thuần và xuất thẳng vào thư mục `src/main/webapp/storefront/`. Tomcat sẽ phục vụ trực tiếp bundle này.

---

### 5.3. Hướng dẫn Triển khai Toàn bộ Hệ thống (Backend & Frontend)

#### Cách 1: Chạy bằng Eclipse (Khuyến nghị)
1. Mở Eclipse, chọn **File** -> **Import** -> **Maven** -> **Existing Maven Projects**.
2. Trỏ đường dẫn đến thư mục chứa file `pom.xml` của project và nhấn Finish.
3. Chờ Maven tải xong thư viện. Ở tab *Servers* phía dưới, đảm bảo đã add Tomcat 11.
4. Click chuột phải vào Project -> Chọn **Run As** -> **Run on Server**.
5. Chọn Tomcat 11, nhấn Finish. Trình duyệt nội bộ sẽ tự động mở trang web.

#### Cách 2: Chạy bằng VS Code
1. Mở VS Code, chọn **File** -> **Open Folder** và trỏ đến thư mục chứa file `pom.xml`.
2. Mở Terminal (`Ctrl + ~`) và gõ lệnh build mã nguồn:
   ```bash
   mvn clean package -DskipTests
   ```
3. Mở extension **Community Server Connector** -> Add Tomcat v11 -> Deploy file: `target/dangnhap.war`.
4. Sang mục **Run and Debug**, chọn cấu hình chạy trong file `launch.json`: Chọn **Mo trang Dang nhap** (hoặc **Debug Tomcat**) -> Nhấn Run.
5. Truy cập ứng dụng:
   - **Giao diện Cửa hàng (Storefront):** `http://localhost:8080/dangnhap/home`
   - **Trang Đăng nhập:** `http://localhost:8080/dangnhap/login`
   - **Trang Quản trị Admin:** `http://localhost:8080/dangnhap/admin/statistics`

> **Mẹo:** Khi cập nhật giao diện frontend, sau khi chạy lại Tomcat, hãy nhấn **`Ctrl + F5`** (hoặc `Ctrl + Shift + R`) trên trình duyệt để xóa cache cũ và hiển thị phiên bản mới nhất ngay lập tức.

---

## 6. Nhật ký Cập nhật & Tinh chỉnh

### Cập nhật Giao diện Quản trị & Dữ liệu Admin (05-09-2026)
* Chạy migration `sql/02-normalize-category-encoding.sql` bằng `sqlcmd -f 65001` để hợp nhất danh mục bị lỗi mã hóa và danh mục đúng.
* Sau khi build WAR, truy cập `/login` để xem giao diện đăng nhập/đăng ký mới; admin truy cập `/admin/statistics` để xem thống kê.
* Tài nguyên ảnh của giao diện quản trị nằm trong `src/main/webapp/assets/images/` và được đóng gói cùng WAR.

### Cập nhật KhangGear Storefront & Nâng cấp Trải nghiệm Người dùng
* **Bố cục chữ Hero Banner:** Tối ưu hóa container và typography, giữ nguyên câu *"Thiết bị tốt cho mọi setup."* trên 1 dòng; cố định cụm từ *"tận hưởng"* không bị tách rời.
* **Nền đồ họa công nghệ (Tông màu lạnh):** Bổ sung họa tiết lưới kỹ thuật **Tech Blueprint Grid** kết hợp vệt sáng phát quang Neon Cyan và Electric Blue.
* **Bộ Icon công nghệ cao cấp:** Xây dựng component [TechGadgetVisual.jsx] với 11 thiết bị vector chi tiết cho danh mục và khung trưng bày **Tech Showcase** sản phẩm.
* **Khử lỗi dấu hỏi "?" tiếng Việt:** Tích hợp bộ lọc làm sạch chuỗi đa tầng tại `api.js` và cung cấp script `sql/04-fix-vietnamese-question-marks.sql`.


## Đối chiếu các chức năng theo yêu cầu bài tập

| Chức năng | Trạng thái | URL hoặc API | Cách hoạt động và mã chính | Kết quả kiểm thử |
| --- | --- | --- | --- | --- |
| Đăng nhập và phân quyền | Hoàn thành | /login | LoginController, UserServiceImpl, AdminAuthFilter; hỗ trợ ADMIN, MANAGER, CUSTOMER. | mvn clean test thành công; tài khoản cũ vẫn được so khớp tương thích. |
| Đăng ký và kích hoạt email | Đã triển khai | /register, /verify-email | Tài khoản công khai được tạo với role CUSTOMER, active=0, email_verified=0; OtpService gửi/kiểm mã 6 số. | Migration đã chạy; cần gửi thử sau khi Tomcat nhận biến SMTP. |
| Quên mật khẩu qua OTP | Đã triển khai | /forgot-password, /forgot-password/verify, /reset-password | Chỉ session đã xác minh OTP mới được gọi UserService.resetPassword; mật khẩu mới được BCrypt hash. | Unit test BCrypt và legacy password thành công; chưa gửi email thật vì thiếu SMTP. |
| Hồ sơ User & Upload Avatar (SiteMesh & JPA) | Hoàn thành | /profile, PUT /api/account/profile | ProfileController (@MultipartConfig, Part), profile-layout.jsp (SiteMesh 3 decorator), profile.jsp, JpaProfileRepository & UserService.updateProfile (JPA transaction), DownloadImageController (/image?fname=avatar/...). Hỗ trợ cập nhật đồng thời fullname, phone, avatar; validate MIME (JPG/PNG/WEBP), size <= 2MB, chống path traversal. | mvn clean test thành công (ProfileControllerMappingTest, UserJpaMappingTest); upload avatar và render an toàn; đồng bộ session account tức thì. |
| Product và Category 1-n | Hoàn thành | products, Category | JPA Product liên kết ManyToOne Category; migration thêm/backfill thời gian tạo/cập nhật an toàn. | SQL Server xác nhận cột thời gian không còn giá trị null. |
| CRUD Product ADMIN/MANAGER | Hoàn thành | /admin/product/list | Product controllers và ProductDao; thao tác thêm/sửa/xóa dùng PRG. | Maven test thành công; cần kiểm tra lại UI sau khi Tomcat nhận WAR mới. |
| 10 sản phẩm mới nhất tại trang chủ | Hoàn thành | /home, GET /api/storefront/products/latest?limit=10 | StorefrontRepository.latest sắp createdAt DESC, id DESC, giới hạn tối đa 10 và React tải từ API. | Frontend lint/build thành công. |
| Danh sách sản phẩm khách hàng | Hoàn thành | /product và alias /products | API phân trang database theo page, size=6; React giữ bộ lọc khi đổi trang. | Frontend lint/build thành công. |
| Danh sách Product quản trị | Hoàn thành | /admin/product/list | Truy vấn JPQL phân trang 6 dòng/trang, mặc định createdAt DESC, id DESC; STT liên tục. | Maven test thành công; cần kiểm tra lại UI sau khi redeploy. |
| Chi tiết sản phẩm | Hoàn thành | /product/:id, alias /products/:id | Product card/tên sản phẩm dẫn tới API chi tiết hiện có. | Frontend lint/build thành công. |

### Cấu hình SMTP cho OTP

Ứng dụng dùng Jakarta Mail (Angus Mail). Không đưa secret vào source hay README. Thiết lập các biến môi trường cho tiến trình Tomcat: SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD, SMTP_FROM, SMTP_STARTTLS. Với Gmail phải dùng App Password, không dùng mật khẩu Gmail chính. Sau khi cấu hình, khởi động lại Tomcat để biến môi trường được nhận.

Trên Windows, tạo hoặc cập nhật `%CATALINA_BASE%\bin\setenv.bat` theo mẫu sau. Thay các giá trị trong dấu ngoặc bằng thông tin riêng của bạn; không commit file này vào Git.

```bat
set "SMTP_HOST=smtp.gmail.com"
set "SMTP_PORT=587"
set "SMTP_USERNAME=<gmail-gui>"
set "SMTP_PASSWORD=<gmail-app-password>"
set "SMTP_FROM=<gmail-gui>"
set "SMTP_STARTTLS=true"
```

Sau đó dừng và khởi động Tomcat qua `bin\shutdown.bat` và `bin\startup.bat` (hoặc bảo đảm extension Server Connector khởi động JVM với sáu biến trên). `setenv.bat` chỉ có hiệu lực khi tiến trình Tomcat thực sự được tạo sau khi file đã tồn tại. Ứng dụng gửi HTML UTF-8 với From `KhangGear <SMTP_FROM>`; Gmail chỉ được dùng đúng địa chỉ đã xác thực, không giả mạo người gửi.

Migration cần chạy: sql/05-product-pagination-and-account-otp.sql. Migration tạo bảng account_otps, thêm email_verified cho bảng User, backfill dữ liệu Product cũ và tạo index phục vụ danh sách mới nhất. OTP dùng SecureRandom, hash SHA-256, hiệu lực 5 phút, tối đa 5 lần sai, chỉ dùng một lần và chờ 60 giây mới gửi lại.

### Nghiệp vụ đăng ký khi SMTP không sẵn sàng

Đăng ký hợp lệ luôn lưu User trước: role CUSTOMER, active=0, email_verified=0. Nếu SMTP gửi thành công, ứng dụng chuyển tới /verify-email với thông báo Mã xác nhận đã được gửi đến email của bạn. Nếu SMTP lỗi hoặc chưa cấu hình, User không bị xóa/rollback, không thể đăng nhập và trang xác minh hiển thị: Tài khoản đã được tạo nhưng chưa thể gửi email xác nhận. Vui lòng thử gửi lại OTP sau. Khi nhập đúng OTP, transaction cập nhật email_verified=1 và active=1.

Đăng ký lại bằng email đã có nhưng chưa xác minh không tạo thêm User. Hệ thống mở lại trang xác minh và cho gửi lại OTP sau 60 giây; OTP mới làm OTP cũ hết hiệu lực. Email được gửi dạng HTML UTF-8 với From là KhangGear <SMTP_FROM>, subject [KHANGGEAR] Mã xác nhận tạo tài khoản hoặc [KHANGGEAR] Mã xác nhận đặt lại mật khẩu.

### Cập nhật 07-09-2026: User Profile Giai đoạn 1

Đã bổ sung nền tảng JPA cho chức năng hồ sơ người dùng. Model `com.baitap.model.User` hiện được ánh xạ thành JPA entity tương ứng bảng `dbo.[User]`; riêng cập nhật hồ sơ dùng repository JPA transaction để lưu `fullname`, `phone`, `avatar` và giữ tương thích email hiện có của storefront. Luồng đăng nhập, OTP, admin user vẫn tiếp tục dùng JDBC ở các phần cũ để tránh thay đổi nghiệp vụ ngoài phạm vi.

Công nghệ bổ sung/được dùng trong giai đoạn này: Jakarta Persistence annotations trên `User`, Hibernate EntityManager qua persistence unit hiện có, repository `JpaProfileRepository`, migration `sql/06-user-profile-jpa-migration.sql` để bổ sung idempotent cột `phone` và `avatar` cho database cũ.

### Cập nhật 07-09-2026: User Profile Giai đoạn 2 (SiteMesh & Giao diện Profile)

Đã tích hợp SiteMesh 3 (`org.sitemesh:sitemesh:3.2.1` tương thích Jakarta Servlet 6.0/Tomcat 11) để quản lý layout và decorator cho giao diện người dùng:
- Route `/profile`: GET hiển thị hồ sơ người dùng (`fullname`, `phone`, `avatar` hiện có), POST cập nhật họ tên và số điện thoại qua JPA transaction.
- Giao diện JSP và Decorator: `WEB-INF/decorators/profile-layout.jsp` quản lý layout dùng chung (header, menu có link Hồ sơ, thông tin đăng nhập, đăng xuất); nội dung form đặt tại `WEB-INF/views/profile.jsp`.
- Đồng bộ dữ liệu session `account` sau cập nhật, áp dụng mô hình Post/Redirect/Get (PRG) và flash message.
- Công nghệ bổ sung: SiteMesh 3.2.1 (Jakarta EE compatible), `WEB-INF/sitemesh3.xml`, JSP/JSTL, CSS giao diện hồ sơ.

### Cập nhật 07-09-2026: User Profile Giai đoạn 3 (Upload ảnh đại diện bằng Multipart)

Đã hoàn thiện chức năng cập nhật ảnh đại diện người dùng sử dụng chuẩn Multipart Form trong Jakarta Servlet 6.0:
- Form profile JSP: Bổ sung thuộc tính `enctype="multipart/form-data"` và input `<input type="file" name="avatar" accept="image/jpeg,image/png,image/webp">`.
- Xử lý Controller: `ProfileController` khai báo `@MultipartConfig` với cấu hình an toàn (fileSizeThreshold 1MB, maxFileSize 2MB, maxRequestSize 5MB).
- Cập nhật đồng thời: Cho phép cập nhật `fullname`, `phone` và `avatar` trong cùng một form. Nếu người dùng không chọn ảnh mới, avatar cũ được giữ nguyên.
- Kiểm tra & Bảo mật file upload:
  - Giới hạn dung lượng tối đa 2MB (trả thông báo thân thiện nếu vượt quá giới hạn).
  - Kiểm tra MIME type hợp lệ (`image/jpeg`, `image/png`, `image/webp`) và phần mở rộng an toàn (`.jpg`, `.jpeg`, `.png`, `.webp`).
  - Đặt tên file an toàn bằng UUID ngẫu nhiên gắn với User ID (`avatar_{id}_{uuid}.ext`), ngăn ngừa xung đột và tuyệt đối không dùng tên file gốc từ client.
  - Chặn tấn công đường dẫn Path Traversal bằng chuẩn hóa đường dẫn `Path.normalize()`.
- Lưu trữ & Hiển thị:
  - Lưu vào thư mục upload chuyên biệt: `Constant.DIR + "/avatar"`.
  - Hiển thị trực tiếp trên giao diện qua servlet phục vụ ảnh `/image?fname=avatar/...`.
- Đồng bộ dữ liệu: Cập nhật `avatar` vào database qua JPA `updateProfile`, đồng bộ tức thì vào session `account`, áp dụng Post/Redirect/Get (PRG) cùng FlashMessage tiếng Việt.
### Cập nhật 07-09-2026: Sửa lỗi Unicode tiếng Việt & Đồng bộ Điều hướng Profile toàn hệ thống

1. **Khắc phục triệt để lỗi font tiếng Việt (`Qu?n tr? viên`, `Nguy?n Trung Khang`):**
   - **Nguyên nhân:** Cột `fullname` trong bảng `dbo.[User]` trước đây là `VARCHAR(150)` nên SQL Server tự động chuyển đổi các ký tự Unicode có dấu tiếng Việt thành dấu `?`.
   - **Xử lý:** Chuyển đổi cột `fullname` sang `NVARCHAR(255)`, cập nhật lại toàn bộ dữ liệu người dùng (`Quản trị viên`, `Quản lý`, `Nguyễn Trung Khang`), cập nhật script migration `sql/06-user-profile-jpa-migration.sql` để đảm bảo tính nhất quán trên mọi môi trường.

2. **Đồng bộ điều hướng truy cập trang Hồ sơ cá nhân (SiteMesh & React):**
   - **Header & Menu Storefront (React):** Click vào tên tài khoản người dùng ở góc trên bên phải hoặc mục menu "Hồ sơ" trên thanh điều hướng sẽ mở trực tiếp trang Profile SiteMesh chuẩn JSP (`/profile`).
   - **Trang `/account/profile` (React):** Bổ sung nút nổi bật "Mở Hồ sơ SiteMesh & Upload Avatar", đồng thời hiển thị avatar đã upload của người dùng từ API `/api/account/profile`.
   - **Giao diện Quản trị Admin & Manager:** Bổ sung mục menu "Hồ sơ cá nhân" trên Sidebar, liên kết tên tài khoản và nút "Hồ sơ" trên Header để admin/manager dễ dàng cập nhật thông tin và avatar.
   - **Layout SiteMesh (`profile-layout.jsp`):** Bổ sung nút "Trang Quản trị" dành cho tài khoản có quyền Admin hoặc Manager để chuyển đổi qua lại thuận tiện.

   
### Cập nhật 09-09-2026: SiteMesh Decorator 3 cho Bài tập 03

- Sử dụng `org.sitemesh:sitemesh:3.3.0-RC1`, tương thích Jakarta Servlet/Tomcat 11.
- SiteMesh chỉ map nhóm `/admin/category/*` của Bài tập 03; React storefront, API, login, OTP và Profile không bị decorator can thiệp.
- Template Bootstrap dùng chung: `WEB-INF/decorators/exercise03-bootstrap.jsp`; cấu hình tại `WEB-INF/sitemesh3.xml`.

- Bài tập 03 đã áp dụng decorator cho các route `/admin/category/list`, `/admin/category/add` và `/admin/category/edit`; các JSP tương ứng chỉ còn phần nội dung, không lặp lại `html/head/body`.
- Các commit SiteMesh: đã tích hợp Bài tập 03.
-
### Cập nhật: Validation Category và Product

### Cập nhật: Validation Account và Profile

- Bổ sung validation server-side dùng chung cho đăng ký, đăng nhập, OTP/quên mật khẩu, đặt lại mật khẩu, Profile JSP và API `/api/account/profile`.
- Lỗi được trả theo field, giữ dữ liệu nhập hợp lệ; avatar chỉ được lưu sau khi validation thành công.

- Bổ sung server-side validation dùng chung tại `vn.iotstar.validation`, áp dụng cho tên, số, category, URL ảnh và multipart image.
- Form Category và Product giữ dữ liệu nhập lại, hiển thị lỗi tiếng Việt theo field và chỉ lưu upload sau khi toàn bộ dữ liệu hợp lệ.
