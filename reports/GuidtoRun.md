cách chạy:
1. Khởi động server lên
2. chọn Tomcat + Add dangnhap.war làm vào làm file deploy
3. Run Tomcat ở chế độ debug
4. qua bên Run and Debug chọn Mo trang Dang nhap -> Run

khi cập nhập code:
Build lại project (để tạo file .war mới chứa code đã sửa):
Mở terminal gõ lại lệnh quen thuộc:


`mvn clean package -DskipTests`


Khởi động lại Tomcat (Restart/Redeploy):

Bạn bấm Stop Tomcat trong mục Servers.

Sau đó bấm Start (hoặc Debug lại nếu muốn soi code).
(Vì Tomcat cần nạp lại file dangnhap.war mới thì nó mới cập nhật code mới cho bạn).