cách chạy:
1. Khởi động server lên
2. chọn Tomcat + Add dangnhap.war làm vào làm file deploy
3. Run Tomcat ở chế độ debug
4. qua bên Run and Debug chọn Mo trang Dang nhap -> Run

khi cập nhập code:

Stop Tomcat trong mục Servers.
Build lại project (để tạo file .war mới chứa code đã sửa):
Mở terminal gõ lại lệnh quen thuộc:

`mvn clean package -DskipTests`

Khởi động lại Tomcat (Restart in Debug Mode):
Ấn Enter
Qua Run and Debug => run
