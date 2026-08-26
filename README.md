# Hướng dẫn chạy project Servlet/JDBC CRUD trên VS Code

## 1. Giới thiệu

Đây là project Java Web sử dụng:

- Java
- Servlet / JSP
- JDBC
- Maven
- SQL Server
- Apache Tomcat 11
- VS Code
- Community Server Connectors (RSP) để quản lý Tomcat
- Công nghệ JPA API

Project được build thành file WAR:

```text
target/dangnhap.war
```

URL chạy ứng dụng:

```text
http://localhost:8080/dangnhap/login
```

---

# 2. Yêu cầu môi trường

Trước khi chạy project, cần cài:

### Java

Khuyến nghị sử dụng JDK 21 trở lên.

Kiểm tra:

```powershell
java -version
```

Kiểm tra JAVA_HOME:

```powershell
echo $env:JAVA_HOME
```

### Maven

Kiểm tra:

```powershell
mvn -version
```

Maven phải nhận đúng Java đang sử dụng.

### Apache Tomcat

Project hiện được cấu hình để chạy với:

```text
Apache Tomcat 11.0.25
```

Ví dụ thư mục:

```text
C:\apache-tomcat-11.0.25
```

### SQL Server

SQL Server phải đang chạy và database của project phải tồn tại.

Project hiện sử dụng port SQL Server:

```text
52282
```

Lưu ý: đây là port động của SQL Server Express. Nếu SQL Server đổi port sau khi restart, cần cập nhật lại JDBC connection trong source code.

---

# 3. Cấu trúc project

Cấu trúc chính:

```text
24133028_Nguyễn Trung Khang_Servlet_JDBC_CRUD/
│
├── pom.xml
│
├── sql/
│   ├── DB_LapTrinhWeb.sql
│   └── ShoppingServiceMVC.sql
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── com.baitap/
│       │   └── vn.iotstar/
│       │
│       ├── resources/
│       │
│       └── webapp/
│           ├── WEB-INF/
│           └── ...
│
├── upload/
│
└── target/
    └── dangnhap.war
```

Hai file SQL thuộc về hai database khác nhau:

```text
sql/DB_LapTrinhWeb.sql
        ↓
Database: DB_LapTrinhWeb

sql/ShoppingServiceMVC.sql
        ↓
Database: ShoppingServiceMVC
```

Không được xem hai file này là file trùng nhau.

---

# 4. Mở project bằng VS Code

Mở VS Code và chọn:

```text
File → Open Folder
```

Chọn **thư mục project chứa trực tiếp `pom.xml`**.

Ví dụ:

```text
D:\Downloads\24133028_Nguyễn Trung Khang_Servlet_JDBC_CRUD
```

Sau khi mở project, kiểm tra:

```powershell
Get-Location
```

và:

```powershell
Get-ChildItem -Force
```

Phải nhìn thấy:

```text
pom.xml
src
sql
upload
```

Đặc biệt, khi chạy Maven phải đứng đúng thư mục có `pom.xml`.

Nếu chạy:

```powershell
mvn clean package -DskipTests
```

mà xuất hiện:

```text
The goal you specified requires a project to execute
because there is no POM in this directory
```

thì đang đứng sai thư mục.

Có thể chuyển vào đúng thư mục bằng:

```powershell
cd "D:\Downloads\24133028_Nguyễn Trung Khang_Servlet_JDBC_CRUD"
```

Sau đó kiểm tra:

```powershell
Test-Path .\pom.xml
```

Nếu kết quả:

```text
True
```

thì đã đúng thư mục.

---

# 5. Cài extension cần thiết trên VS Code

Khuyến nghị cài:

### Java

Cài:

- Extension Pack for Java

Extension này cung cấp các thành phần cần thiết để VS Code nhận diện và chạy project Java.

### Server

Cài:

- Community Server Connectors

Extension này dùng để quản lý server như Tomcat trực tiếp trong VS Code.

> Lưu ý: Community Server Connectors/RSP chủ yếu dùng để quản lý server. Maven vẫn phải được dùng để build WAR.

---

# 6. Cấu hình Tomcat trong VS Code

Mở thanh:

```text
Explorer
```

Ở phía dưới sẽ có:

```text
Servers
```

Nếu `Servers` đang thu gọn thì bấm vào để mở rộng.

Thêm Tomcat:

```text
Servers
→ Add Server
```

Chọn thư mục Tomcat:

```text
C:\apache-tomcat-11.0.25
```

Sau khi thêm thành công sẽ thấy server tương tự:

```text
Servers
└── Tomcat 11.x
```

---

# 7. Build project bằng Maven

Đây là bước quan trọng.

Mỗi khi source code có thay đổi, nên build lại WAR trước khi chạy.

Mở Terminal trong VS Code:

```text
Terminal → New Terminal
```

Đảm bảo terminal đang ở thư mục có `pom.xml`.

Chạy:

```powershell
mvn clean package -DskipTests
```

Nếu thành công sẽ xuất hiện:

```text
BUILD SUCCESS
```

Sau đó kiểm tra:

```text
target/dangnhap.war
```

Đây là file WAR được Tomcat triển khai.

---

# 8. Chạy project bằng Tomcat

Sau khi Maven build thành công:

```text
target/dangnhap.war
```

Tomcat cần deploy WAR này.

Trong VS Code:

```text
Servers
→ Tomcat 11.x
```

Sau đó start/restart Tomcat.

Nếu dùng Community Server Connectors, có thể sử dụng các thao tác Start/Restart/Publish/Deploy tùy giao diện phiên bản extension.

Sau khi Tomcat chạy, kiểm tra Terminal/Output.

Có thể thấy thông báo tương tự:

```text
Deployment of web application archive
[C:\apache-tomcat-11.0.25\webapps\dangnhap.war]
has finished
```

Khi đó ứng dụng đã được deploy.

---

# 9. Mở trang đăng nhập

Mở trình duyệt:

```text
http://localhost:8080/dangnhap/login
```

Hoặc dùng cấu hình `launch.json` được mô tả ở phần bên dưới để VS Code tự mở Chrome.

---

# 10. Cấu hình `.vscode/launch.json`

Nếu muốn sử dụng Run and Debug của VS Code để mở trang web, project có thể có:

```text
.vscode/
└── launch.json
```

Nội dung đề xuất:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Tomcat",
            "request": "attach",
            "hostName": "localhost",
            "port": 5005
        },
        {
            "type": "chrome",
            "name": "Mở trang Đăng nhập",
            "request": "launch",
            "url": "http://localhost:8080/dangnhap/login",
            "webRoot": "${workspaceFolder}"
        }
    ]
}
```

## Ý nghĩa

### Debug Tomcat

```text
Debug Tomcat
```

Dùng khi muốn debug Java bằng breakpoint.

Tomcat phải được chạy với Java Debug Port:

```text
5005
```

### Mở trang Đăng nhập

```text
Mở trang Đăng nhập
```

Dùng để mở:

```text
http://localhost:8080/dangnhap/login
```

trong Chrome.

---

# 11. Cách chạy bình thường - khuyến nghị

Nếu chỉ muốn chạy project để sử dụng, không cần debug Java.

Thực hiện theo thứ tự:

### Bước 1

Mở đúng project.

### Bước 2

Build:

```powershell
mvn clean package -DskipTests
```

### Bước 3

Start/restart Tomcat trong:

```text
Servers
```

### Bước 4

Chờ Tomcat deploy:

```text
dangnhap.war
```

### Bước 5

Mở:

```text
http://localhost:8080/dangnhap/login
```

Đây là cách đơn giản nhất.

---

# 12. Cách chạy bằng Run and Debug

Sau khi Tomcat đã chạy:

```text
Ctrl + Shift + D
```

Mở Run and Debug.

Ở danh sách cấu hình chọn:

```text
Mở trang Đăng nhập
```

Sau đó bấm:

```text
▶
```

VS Code sẽ mở:

```text
http://localhost:8080/dangnhap/login
```

## Không chọn

Không chọn các cấu hình cũ kiểu:

```text
DBConnection
DBConnection(1)
Current File
```

Các cấu hình này dùng để chạy class Java riêng lẻ và không phải cách chạy ứng dụng Servlet/JSP trên Tomcat.

---

# 13. Debug Java bằng breakpoint

Nếu cần debug Controller/DAO:

1. Start Tomcat với Java Debug Port `5005`.
2. Mở:

```text
Ctrl + Shift + D
```

3. Chọn:

```text
Debug Tomcat
```

4. Bấm:

```text
▶
```

5. Đặt breakpoint trong Java source.

Ví dụ:

```java
LoginController.java
```

Khi gửi request từ trình duyệt, VS Code có thể dừng tại breakpoint để kiểm tra:

- biến
- request
- session
- dữ liệu form
- kết quả DAO
- exception

---

# 14. Database

Project sử dụng SQL Server.

Có hai database chính:

```text
DB_LapTrinhWeb
ShoppingServiceMVC
```

Các script SQL nằm tại:

```text
sql/
```

## DB_LapTrinhWeb

File:

```text
sql/DB_LapTrinhWeb.sql
```

Được sử dụng cho flow đăng nhập/xác thực.

Ví dụ tài khoản mẫu:

```text
username: admin
password: 123
```

Các tài khoản mẫu khác có thể được xem trong database sau khi import script.

## ShoppingServiceMVC

File:

```text
sql/ShoppingServiceMVC.sql
```

Được sử dụng cho flow quản lý danh mục và các chức năng liên quan.

---

# 15. JDBC connection

Các class kết nối database hiện sử dụng SQL Server trực tiếp qua port:

```text
127.0.0.1:52282
```

Không sử dụng:

```text
instanceName=SQLEXPRESS
```

Mục đích là tránh bước SQL Browser phải tìm dynamic port qua UDP 1434.

Nếu kiểm tra SQL Server và thấy port đã thay đổi, cần cập nhật JDBC URL tương ứng trong source code trước khi chạy project.

---

# 16. Thư mục upload

Project có:

```text
upload/
```

Thư mục này được sử dụng để lưu file upload, ví dụ hình ảnh danh mục.

Không nên tự ý xóa thư mục này nếu database đang chứa đường dẫn tới các hình ảnh hiện có.

---

# 17. Nếu thay đổi code

Mỗi khi sửa:

```text
.java
.jsp
pom.xml
```

nên thực hiện:

```powershell
mvn clean package -DskipTests
```

Sau đó restart/redeploy Tomcat.

Nếu trình duyệt vẫn hiển thị code cũ:

1. Stop Tomcat.
2. Build lại Maven.
3. Deploy lại `target/dangnhap.war`.
4. Start Tomcat.
5. Refresh trình duyệt.

---

# 18. Nếu Tomcat vẫn chạy WAR cũ

Nếu nghi ngờ Tomcat đang sử dụng bản WAR cũ, có thể thực hiện quy trình sạch:

### Stop Tomcat

Dừng server trước.

### Xóa deployment cũ

Trong:

```text
C:\apache-tomcat-11.0.25\webapps\
```

xóa:

```text
dangnhap/
dangnhap.war
```

Nếu cần có thể xóa thư mục work tương ứng:

```text
C:\apache-tomcat-11.0.25\work\Catalina\localhost\dangnhap
```

### Build lại

Trong project:

```powershell
mvn clean package -DskipTests
```

### Deploy lại

Copy:

```text
target\dangnhap.war
```

vào:

```text
C:\apache-tomcat-11.0.25\webapps\
```

### Start Tomcat

Sau đó mở:

```text
http://localhost:8080/dangnhap/login
```

---

# 19. Các lỗi thường gặp

## Lỗi 1: Không tìm thấy pom.xml

Thông báo:

```text
The goal you specified requires a project to execute
because there is no POM in this directory
```

Nguyên nhân: terminal đang đứng sai thư mục.

Kiểm tra:

```powershell
Get-Location
Get-ChildItem -Force
```

Phải thấy:

```text
pom.xml
```

Sau đó chạy lại:

```powershell
mvn clean package -DskipTests
```

---

## Lỗi 2: Port 8080 đang được sử dụng

Kiểm tra:

```powershell
netstat -ano | findstr :8080
```

Nếu Tomcat đã chạy thì không cần start thêm một Tomcat khác.

---

## Lỗi 3: Trang web không truy cập được

Kiểm tra Tomcat có đang chạy:

```text
Servers
→ Tomcat 11.x
```

Kiểm tra port:

```powershell
netstat -ano | findstr :8080
```

Sau đó thử:

```text
http://localhost:8080/dangnhap/login
```

---

## Lỗi 4: Login không kết nối database

Kiểm tra SQL Server đang chạy.

Kiểm tra port hiện tại.

Project hiện đang sử dụng:

```text
127.0.0.1:52282
```

Kiểm tra lại username/password trong:

```text
DBConnection.java
```

---

## Lỗi 5: Chạy `DBConnection` bị ClassNotFoundException

Nếu thấy:

```text
Could not find or load main class
com.baitap.connection.DBConnection
```

thì không nên dùng Run and Debug để chạy `DBConnection` như một ứng dụng Java độc lập.

Đây là Java Web project.

Hãy:

```text
Maven build
    ↓
WAR
    ↓
Tomcat
    ↓
Browser
```

---

# 20. Quy trình chạy nhanh nhất

Nếu project đã được cấu hình đầy đủ, mỗi lần làm việc chỉ cần:

```powershell
cd "D:\Downloads\24133028_Nguyễn Trung Khang_Servlet_JDBC_CRUD"
```

Sau đó:

```powershell
mvn clean package -DskipTests
```

Nếu:

```text
BUILD SUCCESS
```

thì:

```text
Start/Restart Tomcat
```

Cuối cùng mở:

```text
http://localhost:8080/dangnhap/login
```

Hoặc:

```text
Ctrl + Shift + D
→ Mở trang Đăng nhập
→ ▶
```

---

# 21. Quy trình tổng quát

```text
Sửa source code
      ↓
mvn clean package -DskipTests
      ↓
target/dangnhap.war
      ↓
Tomcat 11
      ↓
Deploy dangnhap.war
      ↓
localhost:8080
      ↓
/dangnhap/login
      ↓
Ứng dụng Servlet/JSP
      ↓
JDBC
      ↓
SQL Server
```

---

# 22. Lưu ý khi nộp bài

Nếu giảng viên sử dụng Eclipse, các file cấu hình riêng của VS Code không phải thành phần bắt buộc để project chạy trên Maven/Tomcat.

Có thể giữ `.vscode/` trong bản làm việc cá nhân để thuận tiện chạy/debug bằng VS Code.

Nếu tạo bản nộp dành riêng cho Eclipse, có thể loại bỏ:

```text
.vscode/
```

nhưng phải đảm bảo các thành phần cần thiết của project vẫn còn:

```text
pom.xml
src/
sql/
upload/
```

Không xóa:

```text
pom.xml
src/
sql/
upload/
```

---

# 23. Checklist trước khi chạy

- [ ] Đã cài JDK
- [ ] `java -version` hoạt động
- [ ] `mvn -version` hoạt động
- [ ] SQL Server đang chạy
- [ ] Database đã được tạo
- [ ] Đúng port SQL Server
- [ ] Đang đứng trong thư mục có `pom.xml`
- [ ] Chạy `mvn clean package -DskipTests`
- [ ] Có `target/dangnhap.war`
- [ ] Tomcat 11 đang chạy
- [ ] WAR đã được deploy
- [ ] Port 8080 đang LISTENING
- [ ] Mở `http://localhost:8080/dangnhap/login`

---

## Lệnh quan trọng nhất

```powershell
mvn clean package -DskipTests
```

Sau khi build thành công:

```text
Start/Restart Tomcat
```

Sau đó:

```text
http://localhost:8080/dangnhap/login
```

Đây là quy trình chuẩn để chạy project trên VS Code.
