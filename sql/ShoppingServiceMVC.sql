-- =====================================================
-- Script tạo database ShoppingServiceMVC và bảng Category
-- Chạy trên SQL Server (SQL Server Management Studio)
-- =====================================================

-- 1. Tạo database (nếu chưa tồn tại)
IF DB_ID(N'ShoppingServiceMVC') IS NULL
BEGIN
    CREATE DATABASE ShoppingServiceMVC;
END
GO

USE ShoppingServiceMVC;
GO

-- 2. Tạo bảng Category (nếu chưa tồn tại)
IF OBJECT_ID(N'dbo.Category', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Category
    (
        cate_id   INT IDENTITY(1,1) PRIMARY KEY,  -- Khóa chính tự tăng
        cate_name NVARCHAR(255),                  -- Tên danh mục
        icons     NVARCHAR(255)                   -- Đường dẫn icon (relative path)
    );
END
GO

-- 3. (Tùy chọn) Dữ liệu mẫu - chạy sau khi đã có file ảnh trong D:\upload\category
-- INSERT INTO dbo.Category (cate_name, icons)
-- VALUES (N'Điện thoại', N'category/phone.png'),
--        (N'Laptop',     N'category/laptop.png'),
--        (N'Phụ kiện',   N'category/accessory.png');
-- GO

-- 4. Kiểm tra dữ liệu
SELECT * FROM dbo.Category;
GO
