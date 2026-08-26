-- Bước 1: Tạo Database trước
CREATE DATABASE DB_LapTrinhWeb;
GO

-- Bước 2: Sau đó mới chọn sử dụng Database vừa tạo
USE DB_LapTrinhWeb;
GO

-- Bước 3: Tạo bảng User
CREATE TABLE [User] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(100),
    username VARCHAR(50) UNIQUE NOT NULL,
    fullname NVARCHAR(100),
    password VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    roleid INT,
    phone VARCHAR(20),
    createddate DATE
);
GO

-- Bước 4: Thêm dữ liệu mẫu
INSERT INTO [User] (email, username, fullname, password, roleid, createddate)
VALUES 
('admin@gmail.com', 'admin', N'Quản trị viên', '123', 1, GETDATE()),
('manager@gmail.com', 'manager', N'Quản lý', '123', 2, GETDATE()),
('user@gmail.com', 'user', N'Khách hàng', '123', 3, GETDATE());
GO