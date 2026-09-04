# Bao cao thuc hien va ra soat du an Java Web

Ngay lap bao cao: 04/09/2026

## 1. Pham vi da doc

Da mo va ra soat cac thanh phan chinh cua du an:

- `README.md`
- `pom.xml`
- `src/main/resources/META-INF/persistence.xml`
- Entity hien co: `Category`, `Video`
- DAO/Service/Controller cua module Category
- Controller/DAO/Service/Model cua module dang nhap, dang xuat
- JSP login va cac JSP admin Category
- File SQL trong thu muc `sql`
- Cau hinh VS Code trong `.vscode/launch.json`

## 2. Hien trang source code

Du an la Java Web Maven, dong goi WAR voi ten `dangnhap.war`.

Cong nghe dang dung:

- Java compile target: 17 trong `pom.xml`
- Jakarta Servlet API 6.0, JSP API 3.1.1, JSTL 3.x
- Hibernate ORM 6.6.1.Final
- SQL Server JDBC Driver 12.4.2
- Tomcat 11
- JSP/Servlet MVC
- Bootstrap 5, FontAwesome 6 va Google Fonts Poppins qua CDN

Luu y: README ghi Java 21, nhung `pom.xml` hien compile voi Java 17 (`maven.compiler.source`, `target`, va `<release>17</release>`).

## 3. Database va persistence

Du an hien dang tach lam 2 database/luong truy cap:

- Module dang nhap/dang xuat dung JDBC qua `com.baitap.connection.DBConnection`, truy van bang `[User]` trong database `ShoppingServiceMVC`.
- Module quan ly danh muc dung JPA/Hibernate qua persistence unit `jpa-hibernate-mysql`, nhung JDBC URL thuc te tro den SQL Server database `ShoppingServiceMVC`.

Bang hien co:

- `[User]`: `id`, `email`, `username`, `fullname`, `password`, `avatar`, `roleid`, `phone`, `createddate`
- `Category`: `cate_id`, `cate_name`, `icons`
- `videos`: duoc khai bao qua entity `Video`, co quan he `ManyToOne` toi `Category`

Quan he hien co:

- `Category` 1-n `Video`
- `Video` n-1 `Category`

Can luu y: `Category` dang khai bao `cascade = CascadeType.ALL` va `orphanRemoval = true` cho `videos`. Neu sau nay co du lieu lien quan nhieu hon, can can than khi xoa Category de tranh xoa day chuyen ngoai y muon.

## 4. Module da hoat dong

Dang nhap/dang xuat:

- URL dang nhap: `/login`
- URL dang xuat: `/logout`
- Dang nhap tim user bang username trong `[User]`
- Mat khau hien dang so sanh dang plain text
- Sau khi dang nhap thanh cong, user duoc luu vao session attribute `account`
- Neu chon remember, tao cookie `username`

Quan ly danh muc:

- Danh sach: `/admin/category/list`
- Them: `/admin/category/add`
- Sua: `/admin/category/edit`
- Xoa: `/admin/category/delete`
- Upload icon dung native Servlet `@MultipartConfig` va `Part`
- Hinh upload luu theo duong dan cau hinh trong `vn.iotstar.util.Constant`
- JSP dung `${pageContext.request.contextPath}` cho cac link chinh

## 5. Cau hinh VS Code/Tomcat

Da dieu chinh `.vscode/launch.json` theo quy trinh chay bang Community Server Connector:

- `Debug Tomcat`: attach Java debugger toi `localhost:5005`
- `Mo trang Dang nhap`: mo `http://localhost:8080/dangnhap/login` bang Chrome

Khong con dung `.vscode/tasks.json` de tu start/copy WAR nua, vi Community Server Connector da quan ly viec start server va deploy WAR.

Quy trinh chay khuyen nghi:

1. Build project bang Maven: `mvn clean package -DskipTests`
2. Start Tomcat 11 bang Community Server Connector.
3. Deploy file `target/dangnhap.war`.
4. Neu can debug Java, bat Tomcat o debug mode trong Community Server Connector.
5. Qua Run and Debug, chon `Debug Tomcat`.
6. Mo trang `http://localhost:8080/dangnhap/login`.

Neu attach debug fail, kiem tra log Tomcat co dong `Listening for transport dt_socket at address: 5005` hay khong. Neu port khac, sua `port` trong `launch.json`.

## 6. Module Quan ly San pham da trien khai

Da bo sung module Quan ly San pham trong admin.

File Java da tao/sua:

- `src/main/java/vn/iotstar/entity/Product.java`
- `src/main/java/vn/iotstar/dao/IProductDao.java`
- `src/main/java/vn/iotstar/dao/ProductDao.java`
- `src/main/java/vn/iotstar/service/IProductService.java`
- `src/main/java/vn/iotstar/service/impl/ProductServiceImpl.java`
- `src/main/java/vn/iotstar/controller/ProductBaseController.java`
- `src/main/java/vn/iotstar/controller/ProductListController.java`
- `src/main/java/vn/iotstar/controller/ProductAddController.java`
- `src/main/java/vn/iotstar/controller/ProductEditController.java`
- `src/main/java/vn/iotstar/controller/ProductDetailController.java`
- `src/main/java/vn/iotstar/controller/ProductDeleteController.java`
- `src/main/java/vn/iotstar/entity/Category.java`
- `src/main/resources/META-INF/persistence.xml`

File JSP da tao/sua:

- `src/main/webapp/views/admin/list-product.jsp`
- `src/main/webapp/views/admin/add-product.jsp`
- `src/main/webapp/views/admin/edit-product.jsp`
- `src/main/webapp/views/admin/detail-product.jsp`
- `src/main/webapp/views/admin/list-category.jsp`
- `src/main/webapp/views/admin/add-category.jsp`
- `src/main/webapp/views/admin/edit-category.jsp`

File SQL da sua:

- `sql/ShoppingServiceMVC.sql`

Chuc nang da co:

- Danh sach san pham
- Them san pham
- Sua san pham
- Xem chi tiet san pham
- Xoa san pham
- Tim kiem theo ten/mo ta
- Loc theo danh muc
- Loc theo trang thai
- Phan trang 10 san pham/trang
- Upload anh san pham bang `@MultipartConfig`
- Nhap URL anh san pham
- Anh placeholder neu chua co anh
- Validate server-side: ten, danh muc, gia, ton kho
- Gia va ton kho khong duoc am
- Kiem tra rang buoc `order_items` truoc khi xoa san pham neu bang nay ton tai
- Link sidebar "Quan ly San pham" da tro den `/admin/product/list`
- Da xoa muc "Cai dat" khoi sidebar cua cac trang Category va Product moi

URL moi:

- `/admin/product/list`
- `/admin/product/add`
- `/admin/product/edit?id=...`
- `/admin/product/detail?id=...`
- `/admin/product/delete?id=...`

Database moi:

- Bang `products`
- Khoa ngoai `products.category_id` tham chieu `Category(cate_id)`
- Index cho `category_id`, `active`, `name`
- Seed nhanh cac danh muc cong nghe neu chua co
- Seed mot so san pham mau de kiem tra giao dien

## 7. Module Quan ly Nguoi dung Admin da trien khai

Da bo sung module quan ly nguoi dung admin va dung lai model/bang `[User]` hien co.

File Java da tao/sua:

- `src/main/java/com/baitap/model/User.java`
- `src/main/java/com/baitap/dao/UserDao.java`
- `src/main/java/com/baitap/dao/impl/UserDaoImpl.java`
- `src/main/java/com/baitap/service/UserService.java`
- `src/main/java/com/baitap/service/impl/UserServiceImpl.java`
- `src/main/java/com/baitap/controller/AdminUserListController.java`
- `src/main/java/com/baitap/controller/AdminUserAddController.java`
- `src/main/java/com/baitap/controller/AdminUserEditController.java`
- `src/main/java/com/baitap/controller/AdminUserToggleController.java`
- `src/main/java/com/baitap/controller/LoginController.java`
- `src/main/java/com/baitap/connection/DBConnection.java`

File JSP da tao:

- `src/main/webapp/views/admin/list-user.jsp`
- `src/main/webapp/views/admin/add-user.jsp`
- `src/main/webapp/views/admin/edit-user.jsp`

Chuc nang da co:

- Danh sach nguoi dung
- Tim kiem theo username, ho ten, email
- Loc theo role va trang thai
- Them nguoi dung
- Sua thong tin nguoi dung
- Khoa/mo khoa tai khoan
- Khong hien password tren giao dien danh sach/sua
- Phan biet `ADMIN` va `CUSTOMER` theo `roleid`
- Khong cho admin dang dang nhap tu khoa chinh minh
- Login chi cho tai khoan active dang nhap

URL moi:

- `/admin/user/list`
- `/admin/user/add`
- `/admin/user/edit?id=...`
- `/admin/user/toggle?id=...&active=...`

Ghi chu bao mat: mat khau van duoc giu plain text de tuong thich du lieu cu va tai khoan mau `admin/123`. Chua nang cap bam mat khau trong dot sua nay.

## 8. Module Thong ke San pham da trien khai

File Java da tao:

- `src/main/java/vn/iotstar/dao/StatisticsDao.java`
- `src/main/java/vn/iotstar/controller/StatisticsController.java`

File JSP da tao:

- `src/main/webapp/views/admin/statistics.jsp`

Chuc nang da co:

- Tong so san pham
- Tong ton kho
- So san pham sap het hang
- So san pham het hang
- Tong gia tri ton kho
- So san pham theo danh muc
- Danh sach san pham sap het hang
- Top 5 san pham ban chay dua tren `order_items`

URL moi:

- `/admin/statistics`

## 9. Bao ve Admin

Da them filter:

- `src/main/java/com/baitap/filter/AdminAuthFilter.java`

Chuc nang:

- Bao ve tat ca URL `/admin/*`
- Chua dang nhap thi chuyen ve `/login`
- Chi user co `roleid = 1` va `active = true` moi vao duoc admin
- User khong du quyen se nhan HTTP 403

## 10. Thong nhat database

Da chon cach thong nhat hop ly: dua User/login/admin user sang cung database `ShoppingServiceMVC`.

Da sua:

- `src/main/java/com/baitap/connection/DBConnection.java`

JDBC login truoc day tro den `DB_LapTrinhWeb`, nay tro den:

- `ShoppingServiceMVC`

SQL `ShoppingServiceMVC.sql` da tao/cap nhat bang `[User]` trong cung database nay de cac module admin dung chung database voi Product/Order/Statistics.

Da bo sung fallback tam thoi trong `UserDaoImpl.findByUsername`: app uu tien tim user trong `ShoppingServiceMVC`; neu chua tim thay se thu doc database cu `DB_LapTrinhWeb`. Muc dich la giu dang nhap `admin/123` khong bi dut trong luc chua chay script SQL moi. Sau khi chay `ShoppingServiceMVC.sql`, user admin se nam trong database thong nhat.

## 11. Cac module theo yeu cau lon chua co trong source

Theo yeu cau duoc cung cap, cac module sau can duoc trien khai tiep, nhung hien chua co source tuong ung:

- Bieu do Chart.js cho thong ke, hien tai moi co bang/so lieu thong ke.
- Tru ton kho theo giao dich khi cap nhat don hang; hien tai module admin moi cap nhat trang thai va xem don.

Sidebar trong JSP Category hien van co cac link placeholder `href="#"` cho:

- Mot so trang con toi gian nhu add/edit product, detail order, add/edit user chua tach sidebar thanh fragment dung chung.

Muc `Cai dat` da duoc xoa khoi cac sidebar da cap nhat.

## 12. Module Quan ly Don hang da trien khai

Da bo sung module Quan ly Don hang trong admin.

File Java da tao/sua:

- `src/main/java/vn/iotstar/entity/Order.java`
- `src/main/java/vn/iotstar/entity/OrderItem.java`
- `src/main/java/vn/iotstar/dao/IOrderDao.java`
- `src/main/java/vn/iotstar/dao/OrderDao.java`
- `src/main/java/vn/iotstar/service/IOrderService.java`
- `src/main/java/vn/iotstar/service/impl/OrderServiceImpl.java`
- `src/main/java/vn/iotstar/controller/OrderBaseController.java`
- `src/main/java/vn/iotstar/controller/OrderListController.java`
- `src/main/java/vn/iotstar/controller/OrderDetailController.java`
- `src/main/java/vn/iotstar/controller/OrderUpdateStatusController.java`
- `src/main/resources/META-INF/persistence.xml`

File JSP da tao/sua:

- `src/main/webapp/views/admin/list-order.jsp`
- `src/main/webapp/views/admin/detail-order.jsp`
- `src/main/webapp/views/admin/list-category.jsp`
- `src/main/webapp/views/admin/add-category.jsp`
- `src/main/webapp/views/admin/edit-category.jsp`
- `src/main/webapp/views/admin/list-product.jsp`

File SQL da sua:

- `sql/ShoppingServiceMVC.sql`

Chuc nang da co:

- Danh sach don hang
- Tim kiem theo ma don, ten khach hang, so dien thoai
- Loc theo trang thai
- Phan trang 10 don/trang
- Xem chi tiet don hang
- Hien thi danh sach san pham trong don
- Cap nhat trang thai don hang
- Kiem tra luong chuyen trang thai hop le
- Khong hard-delete don hang; muon huy thi chuyen sang trang thai `CANCELLED`
- Link sidebar "Quan ly Don hang" da tro den `/admin/order/list`

Trang thai don hang:

- `PENDING`: Cho xac nhan
- `CONFIRMED`: Da xac nhan
- `SHIPPING`: Dang giao
- `COMPLETED`: Hoan thanh
- `CANCELLED`: Da huy

Quy tac chuyen trang thai:

- `PENDING` -> `CONFIRMED` hoac `CANCELLED`
- `CONFIRMED` -> `SHIPPING` hoac `CANCELLED`
- `SHIPPING` -> `COMPLETED` hoac `CANCELLED`
- `COMPLETED` va `CANCELLED` khong duoc chuyen tiep

URL moi:

- `/admin/order/list`
- `/admin/order/detail?id=...`
- `/admin/order/update-status`

Database moi:

- Bang `orders`
- Bang `order_items`
- Khoa ngoai `order_items.order_id` tham chieu `orders(id)`
- Khoa ngoai `order_items.product_id` tham chieu `products(id)`
- Index cho trang thai, ngay dat, khach hang/so dien thoai, order item
- Seed mot so don hang mau voi nhieu trang thai khac nhau

## 13. Cac viec can lam tiep de hoan thanh day du yeu cau

Can bo sung cac nhom file sau:

- Entity: `Product`, `Order`, `OrderItem`, co the can tao entity JPA cho `User` neu muon thong nhat JPA.
- DAO/Repository: product, order, user admin, statistics.
- Service: product, order, user admin, statistics.
- Controller/Servlet: list/add/edit/detail/delete product; list/detail/update status order; list/add/edit/lock user; dashboard statistics.
- JSP: cac trang admin moi va fragment dung chung cho sidebar/header.
- SQL: script tao/cap nhat bang, foreign key, index, unique constraint va seed data.
- Filter: bao ve `/admin/*`.

Khuyen nghi khi trien khai:

- Thong nhat database cho admin modules moi. Hien Category dang o `ShoppingServiceMVC`, User login dang o `DB_LapTrinhWeb`; neu can Order lien ket User va Product thi nen hop nhat hoac tao script ro rang de tranh foreign key cheo database.
- Dung JPA cho cac module moi nhu yeu cau.
- Khong xoa du lieu Category/User cu.
- Khong hard-code context path trong JSP.
- Khong hien password tren giao dien.
- Them validate server-side cho product/order/user.
- Xoa `target/` khoi Git neu dang bi track.

## 14. Ket qua build

Da chay lenh:

```bash
mvn clean package -DskipTests
```

Ket qua:

- BUILD SUCCESS luc 23:08 ngay 04/09/2026
- WAR duoc tao tai `target/dangnhap.war`
- Tests bi skip theo tham so `-DskipTests`

## 15. Cac phan chua kiem thu thuc te

Chua kiem thu truc tiep tren SQL Server/Tomcat trong bao cao nay:

- Dang nhap bang database thuc te
- CRUD Category tren browser
- Upload icon tren browser
- Debug attach thanh cong trong VS Code
- CRUD Product tren browser
- Chay script SQL Product tren SSMS
- Danh sach/chi tiet/cap nhat trang thai don hang tren browser
- Chay script SQL Order/OrderItem tren SSMS
- Quan ly nguoi dung admin tren browser
- Dashboard thong ke tren browser
- Filter `/admin/*` voi user khong phai ADMIN tren Tomcat thuc te

Ly do: qua terminal chi xac minh duoc build Maven va cau truc file; viec kiem thu browser/Tomcat/SQL Server can thuc hien tren moi truong VS Code/Tomcat dang chay cua may.

## 16. Tai khoan mau

Theo `sql/ShoppingServiceMVC.sql` sau khi thong nhat database:

- `admin` / `123` / role ADMIN
- `customer01` / `123` / role CUSTOMER
- `customer02` / `123` / role CUSTOMER
- `customer03` / `123` / role CUSTOMER

## 17. File SQL can chay

Thu tu khuyen nghi hien tai:

1. Chay `sql/ShoppingServiceMVC.sql` tren SQL Server bang SSMS.
2. Script nay tao/cap nhat database `ShoppingServiceMVC`.
3. Script tao/cap nhat cac bang `[User]`, `Category`, `products`, `orders`, `order_items`.
4. Script seed admin/customer, danh muc cong nghe, khoang 50 san pham va 10-15 don hang mau.

`sql/DB_LapTrinhWeb.sql` la script cu cua module login. Sau khi thong nhat database, app dang uu tien `ShoppingServiceMVC`.

File `sql/DB_LapTrinhWeb.sql` da duoc sua thanh script co the chay lai nhieu lan (`IF DB_ID`, `IF OBJECT_ID`, `IF NOT EXISTS`) va them cot `active`. File nay chi can chay neu muon giu/cap nhat database login cu dung cho fallback. De app admin co day du Product/Order/User/Statistics, can chay `sql/ShoppingServiceMVC.sql`.

## 18. Huong dan chay de co du lieu moi

1. Mo SSMS va ket noi SQL Server local.
2. Mo file `sql/ShoppingServiceMVC.sql`.
3. Bam Execute de chay toan bo script.
4. Dam bao port/user/password trong `persistence.xml` va `DBConnection.java` khop voi SQL Server cua may.
5. Chay lenh build:

```bash
mvn clean package -DskipTests
```

6. Deploy lai file `target/dangnhap.war` len Tomcat 11 trong VS Code.
7. Dang nhap bang `admin` / `123`.
8. Kiem tra cac URL:

- `/admin/category/list`
- `/admin/product/list`
- `/admin/order/list`
- `/admin/user/list`
- `/admin/statistics`

## 19. Sua loi font tieng Viet

Da sua cac JSP admin bi mojibake dang `Quáº£n lÃ½`, `ÄÄƒng xuáº¥t`, `Sáº£n pháº©m` ve UTF-8 tieng Viet chuan.

Da quet lai thu muc:

- `src/main/webapp/views/admin`

Ket qua: khong con chuoi mojibake pho bien trong cac JSP admin.

Da build lai:

```bash
mvn clean package -DskipTests
```

Ket qua: BUILD SUCCESS luc 23:16 ngay 04/09/2026.

## Bo sung 2026-09-04: dang nhap, dang ky, phan quyen va quan ly nguoi dung

### Nguyen nhan goc

- `LoginController` chuyen moi tai khoan den `/admin/category/list`, nen CUSTOMER nhan 403 ngay sau khi dang nhap.
- `UserDaoImpl` fallback am tham sang `DB_LapTrinhWeb`, trong khi dang ky va danh sach admin dung `ShoppingServiceMVC`.
- DAO nuot exception SQL, controller van redirect va tao ra trang trang/kho truy vet.
- Controller user dua thong bao tieng Viet thang vao URL redirect. Tomcat tra HTTP 302 nhung khong co `Location` hop le.
- Role xu ly rai rac theo magic number; MANAGER chua co quy tac thong nhat.

### Quy uoc role cuoi cung

| Role | Gia tri | Quyen sau dang nhap |
|---|---:|---|
| ADMIN | 1 | Toan bo `/admin/*`, bao gom quan ly nguoi dung |
| MANAGER | 2 | Danh muc, san pham, don hang, thong ke; khong vao `/admin/user/*` |
| CUSTOMER | 3 | `/home`; vao truc tiep `/admin/*` nhan 403 |

Tat ca module hien dung duy nhat `ShoppingServiceMVC`: JDBC login/register/user-admin, JPA, va DAO category/product/order/statistics. Fallback sang `DB_LapTrinhWeb` da bi xoa khoi source. File `DB_LapTrinhWeb.sql` chi la script legacy, khong chay cho ung dung nay.

### File da sua/them

- Controller: `LoginController`, `RegisterController`, `LogoutController`, `HomeController`, va bon `AdminUser*Controller`.
- Security: `AdminAuthFilter`, `CharacterEncodingFilter`, `UserRole`, `LoginRedirect`, `FlashMessage`.
- Data layer: `UserDao`, `UserDaoImpl`, `UserService`, `UserServiceImpl`, `User`.
- JSP: login/register/home/access-denied va ba JSP quan ly user.
- `sql/ShoppingServiceMVC.sql`, `pom.xml`, `src/test/java/com/baitap/util/LoginRedirectTest.java`.

### CSDL va migration

Da chay that `sql/ShoppingServiceMVC.sql` tren SQL Server `TrungKhang-laptop\SQLEXPRESS`, database `ShoppingServiceMVC`.

- Them idempotent migration cho `dbo.[User]`: `id`, `email`, `username`, `fullname`, `password`, `avatar`, `roleid`, `phone`, `createddate`, `active`.
- Co unique index username; email duoc tao unique index khi du lieu hien co khong trung.
- Seed va chuan hoa `admin / 123` (1), `manager / 123` (2), `user / 123` (3), deu active.
- Khong drop database/bang va khong xoa du lieu cu.

Chay SQL UTF-8 bang lenh sau:

```powershell
sqlcmd -f 65001 -S 127.0.0.1,52282 -U sa -P trungkhang -i sql\ShoppingServiceMVC.sql -b -r 1
```

Mot lan chay thu truoc khi bo sung `-f 65001` da tao vai category ten loi ma hoa. Cac ban ghi nay duoc giu lai de khong vi pham yeu cau khong xoa du lieu cu; cac lan sau phai dung `-f 65001`.

### Kiem thu da chay

| # | Truong hop | Ket qua |
|---:|---|---|
| 1 | `admin / 123` vao admin | Dat, HTTP 302 toi `/admin/category/list` |
| 2 | `manager / 123` dang nhap | Dat, HTTP 302 toi `/admin/category/list` |
| 3 | `user / 123` dang nhap | Dat, HTTP 302 toi `/home` |
| 4 | CUSTOMER vao truc tiep admin | Dat, HTTP 403 |
| 5 | Dang ky username moi | Dat voi `verify_customer` |
| 6 | Account dang ky role/active dung | Dat, SQL tra `roleid=3`, `active=1` |
| 7 | Account moi dang nhap | Dat, redirect `/home` |
| 8 | Dang ky trung username | Dat, form hien thi loi |
| 9 | Danh sach admin user | Dat, HTTP 200 co du user seed va user test |
| 10 | ADMIN them CUSTOMER | Dat |
| 11 | ADMIN them MANAGER | Dat |
| 12 | MANAGER moi dang nhap | Dat, redirect admin category |
| 13 | Khoa CUSTOMER | Dat, login hien loi |
| 14 | Mo khoa CUSTOMER | Dat, login redirect `/home` |
| 15 | Thao tac user khong lam mat session ADMIN | Dat |
| 16 | Logout roi truy cap admin | Dat, redirect `/login` |
| 17 | Maven | Dat: `mvn clean test` va `mvn clean package`; 3 JUnit tests pass |

### Deploy lai WAR

```powershell
mvn clean package
```

WAR nam tai `target/dangnhap.war`. VS Code Tomcat extension cua may hien tai da dong bo file nay vao `C:\apache-tomcat-11.0.25\webapps\dangnhap.war`; neu may khong tu dong bo, redeploy file nay qua muc Servers. Mo `http://localhost:8080/dangnhap/login` va hard refresh `Ctrl+F5`.

Mat khau van luu plain text de tuong thich du lieu `123` hien co. Can migration rieng neu sau nay nang cap sang BCrypt.
