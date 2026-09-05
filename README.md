# Báo Cáo Đồ Án: Hệ Thống Quản Trị & Cửa Hàng KhangGear (Java Web)

**Sinh viên thực hiện:** Nguyễn Trung Khang  
**MSSV:** 24133028  
**Học phần:** Lập trình Web (Bài tập 02 - JPA API & Servlet MVC)  

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
                   |                                               |
                   | [Controller Layer]                            |
                   |  - RESTful API Servlets (/api/storefront/*)   |
                   |  - Admin MVC Servlets (/admin/*)              |
                   |  - Auth Controllers (/login, /logout)         |
                   +-----------------------------------------------+
                                           |
                                           v
                   +-----------------------------------------------+
                   | [Service & DAO Layer]                         |
                   |  - JPA / Hibernate 6.6.1 ORM (EntityManager)  |
                   |  - Native JDBC DAO (User Authentication)      |
                   |  - StorefrontRepository & DTO Mappers         |
                   +-----------------------------------------------+
                                           |
                                           v
                   +-----------------------------------------------+
                   |             Microsoft SQL Server              |
                   |   (DB_LapTrinhWeb & ShoppingServiceMVC )       |
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
* **Backend:** Java 21, Jakarta EE (Servlet 6.0, JSP, JSTL), Maven.
* **ORM:** JPA (Hibernate 6.6.1.Final).
* **Database:** Microsoft SQL Server (JDBC Driver).
* **Application Server:** Apache Tomcat 11.
* **Frontend:** React 18, Vite 6, React Router DOM v6, Lucide React, Modern CSS3.

### 4.2. Danh mục File Cơ sở Dữ liệu (Thư mục `/sql`)
1. **`DB_LapTrinhWeb.sql`:** Chứa bảng `users` (dùng cho chức năng Đăng nhập - tài khoản mẫu: `admin` / `123`).
2. **`ShoppingServiceMVC.sql`:** Chứa cấu trúc bảng `Category`, `videos`, `products`, `orders`, `order_items` và dữ liệu mẫu của cửa hàng.
3. **`02-normalize-category-encoding.sql`:** Script xử lý hợp nhất các danh mục bị lỗi mã hóa font tiếng Việt ban đầu.
4. **`03-storefront-checkout-migration.sql`:** Script bổ sung các cột cần thiết phục vụ quy trình đặt hàng và thanh toán.
5. **`04-fix-vietnamese-question-marks.sql`:** Script chuẩn hóa trực tiếp các bản ghi bị dấu hỏi "?" trong cơ sở dữ liệu (`Tay cầm Xbox Wireless`, `tạo thử test`...).

---

## 5. Hướng dẫn Chạy & Triển khai Dự án (Deployment Guide)

### 5.1. Yêu cầu hệ thống trước khi chạy
* JDK 21+ và Apache Maven đã được cấu hình biến môi trường đầy đủ (`JAVA_HOME`, `M2_HOME`).
* Node.js v18+ và npm (dành cho phát triển frontend).
* Đã cài đặt Apache Tomcat 11.
* SQL Server đang hoạt động và đã chạy các file SQL trong thư mục `/sql`.
* *Lưu ý Port DB:* Dự án đang cấu hình port `127.0.0.1:52282`. Vui lòng chỉnh lại port trong file [DBConnection.java](file:///d:/Trung%20Khang/Documents/L%E1%BA%ADp%20tr%C3%ACnh%20WEB/laptrinhWEB_baitap/src/main/java/com/baitap/connection/DBConnection.java) và [persistence.xml](file:///d:/Trung%20Khang/Documents/L%E1%BA%ADp%20tr%C3%ACnh%20WEB/laptrinhWEB_baitap/src/main/resources/META-INF/persistence.xml) (thường là `1433`) cho khớp với cấu hình máy của bạn.

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
* **Bộ Icon công nghệ cao cấp:** Xây dựng component [TechGadgetVisual.jsx](file:///d:/Trung%20Khang/Documents/L%E1%BA%ADp%20tr%C3%ACnh%20WEB/laptrinhWEB_baitap/frontend/src/components/TechGadgetVisual.jsx) với 11 thiết bị vector chi tiết cho danh mục và khung trưng bày **Tech Showcase** sản phẩm.
* **Khử lỗi dấu hỏi "?" tiếng Việt:** Tích hợp bộ lọc làm sạch chuỗi đa tầng tại `api.js` và cung cấp script `sql/04-fix-vietnamese-question-marks.sql`.
* **Tài liệu tham khảo thêm:** Xem chi tiết tại [docs/storefront-api.md](file:///d:/Trung%20Khang/Documents/L%E1%BA%ADp%20tr%C3%ACnh%20WEB/laptrinhWEB_baitap/docs/storefront-api.md), [docs/storefront-ui-spec.md](file:///d:/Trung%20Khang/Documents/L%E1%BA%ADp%20tr%C3%ACnh%20WEB/laptrinhWEB_baitap/docs/storefront-ui-spec.md) và [docs/asset-manifest.md](file:///d:/Trung%20Khang/Documents/L%E1%BA%ADp%20tr%C3%ACnh%20WEB/laptrinhWEB_baitap/docs/asset-manifest.md).
