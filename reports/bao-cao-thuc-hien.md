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

- Module dang nhap/dang xuat dung JDBC qua `com.baitap.connection.DBConnection`, truy van bang `[User]` trong database `DB_LapTrinhWeb`.
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

## 7. Cac module theo yeu cau lon chua co trong source

Theo yeu cau duoc cung cap, cac module sau can duoc trien khai tiep, nhung hien chua co source tuong ung:

- Quan ly nguoi dung trong admin
- Thong ke san pham lay du lieu tu database
- SQL seed du khoang 50 san pham cong nghe
- Seed customer va 10-15 don hang
- Filter bao ve `/admin/*` va phan quyen ADMIN/CUSTOMER

Sidebar trong JSP Category hien van co cac link placeholder `href="#"` cho:

- Quan ly Nguoi dung
- Thong ke
- Cai dat

Muc `Cai dat` can xoa khi trien khai tiep.

## 8. Module Quan ly Don hang da trien khai

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

## 9. Cac viec can lam tiep de hoan thanh day du yeu cau

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

## 10. Ket qua build

Da chay lenh:

```bash
mvn clean package -DskipTests
```

Ket qua:

- BUILD SUCCESS luc 22:31 ngay 04/09/2026
- WAR duoc tao tai `target/dangnhap.war`
- Tests bi skip theo tham so `-DskipTests`

## 11. Cac phan chua kiem thu thuc te

Chua kiem thu truc tiep tren SQL Server/Tomcat trong bao cao nay:

- Dang nhap bang database thuc te
- CRUD Category tren browser
- Upload icon tren browser
- Debug attach thanh cong trong VS Code
- CRUD Product tren browser
- Chay script SQL Product tren SSMS
- Danh sach/chi tiet/cap nhat trang thai don hang tren browser
- Chay script SQL Order/OrderItem tren SSMS

Ly do: qua terminal chi xac minh duoc build Maven va cau truc file; viec kiem thu browser/Tomcat/SQL Server can thuc hien tren moi truong VS Code/Tomcat dang chay cua may.

## 12. Tai khoan mau

Theo `sql/DB_LapTrinhWeb.sql`:

- `admin` / `123`
- `manager` / `123`
- `user` / `123`

## 13. File SQL can chay

Thu tu hien tai:

1. `sql/DB_LapTrinhWeb.sql`: tao database `DB_LapTrinhWeb` va bang `[User]`.
2. `sql/ShoppingServiceMVC.sql`: tao database `ShoppingServiceMVC` va bang `Category`.

Luu y: `persistence.xml` cua JPA dang ket noi `ShoppingServiceMVC`, con login JDBC dang ket noi theo `DBConnection.java`.
