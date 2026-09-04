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

-- 5. Tao bang products cho module Quan ly San pham
IF OBJECT_ID(N'dbo.products', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.products
    (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        category_id INT NOT NULL,
        description NVARCHAR(MAX) NULL,
        price DECIMAL(18,2) NOT NULL CONSTRAINT CK_products_price_non_negative CHECK (price >= 0),
        stock_quantity INT NOT NULL CONSTRAINT CK_products_stock_non_negative CHECK (stock_quantity >= 0),
        image NVARCHAR(500) NULL,
        active BIT NOT NULL CONSTRAINT DF_products_active DEFAULT 1,
        created_at DATETIME2 NULL CONSTRAINT DF_products_created_at DEFAULT SYSDATETIME(),
        updated_at DATETIME2 NULL CONSTRAINT DF_products_updated_at DEFAULT SYSDATETIME(),
        CONSTRAINT FK_products_category FOREIGN KEY (category_id) REFERENCES dbo.Category(cate_id)
    );

    CREATE INDEX IX_products_category_id ON dbo.products(category_id);
    CREATE INDEX IX_products_active ON dbo.products(active);
    CREATE INDEX IX_products_name ON dbo.products(name);
END
GO

-- 6. Seed nhanh cac danh muc cong nghe neu chua co
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Điện thoại')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Điện thoại', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Laptop')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Laptop', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Bàn phím')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Bàn phím', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Chuột')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Chuột', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Tai nghe')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Tai nghe', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Màn hình')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Màn hình', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Gear gaming')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Gear gaming', N'');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Phụ kiện')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Phụ kiện', N'');
GO

-- 7. Seed mot so san pham mau neu bang dang rong
IF NOT EXISTS (SELECT 1 FROM dbo.products)
BEGIN
    INSERT INTO dbo.products(name, category_id, description, price, stock_quantity, image, active)
    SELECT N'iPhone 17 Pro Max', cate_id, N'Điện thoại cao cấp dung lượng lớn.', 34990000, 8, N'https://placehold.co/300x300?text=iPhone', 1 FROM dbo.Category WHERE cate_name = N'Điện thoại'
    UNION ALL SELECT N'Samsung Galaxy S26 Ultra', cate_id, N'Flagship Android màn hình lớn.', 31990000, 6, N'https://placehold.co/300x300?text=Galaxy', 1 FROM dbo.Category WHERE cate_name = N'Điện thoại'
    UNION ALL SELECT N'MacBook Air M4 13 inch', cate_id, N'Laptop mỏng nhẹ cho học tập và làm việc.', 28990000, 5, N'https://placehold.co/300x300?text=MacBook', 1 FROM dbo.Category WHERE cate_name = N'Laptop'
    UNION ALL SELECT N'ASUS Vivobook 15 OLED', cate_id, N'Laptop phổ thông màn OLED.', 17990000, 10, N'https://placehold.co/300x300?text=ASUS', 1 FROM dbo.Category WHERE cate_name = N'Laptop'
    UNION ALL SELECT N'Bàn phím Aula F75', cate_id, N'Bàn phím cơ layout 75%.', 1290000, 14, N'https://placehold.co/300x300?text=Aula+F75', 1 FROM dbo.Category WHERE cate_name = N'Bàn phím'
    UNION ALL SELECT N'Akko 5075B Plus', cate_id, N'Bàn phím cơ không dây nhiều mode.', 1890000, 4, N'https://placehold.co/300x300?text=Akko', 1 FROM dbo.Category WHERE cate_name = N'Bàn phím'
    UNION ALL SELECT N'Logitech G Pro X Superlight', cate_id, N'Chuột gaming không dây siêu nhẹ.', 2790000, 3, N'https://placehold.co/300x300?text=Logitech', 1 FROM dbo.Category WHERE cate_name = N'Chuột'
    UNION ALL SELECT N'Razer DeathAdder V3', cate_id, N'Chuột gaming ergonomic.', 1590000, 0, N'https://placehold.co/300x300?text=Razer', 1 FROM dbo.Category WHERE cate_name = N'Chuột'
    UNION ALL SELECT N'Sony WH-1000XM5', cate_id, N'Tai nghe chống ồn cao cấp.', 6990000, 2, N'https://placehold.co/300x300?text=Sony', 1 FROM dbo.Category WHERE cate_name = N'Tai nghe'
    UNION ALL SELECT N'LG UltraGear 27 inch', cate_id, N'Màn hình gaming tần số quét cao.', 5990000, 7, N'https://placehold.co/300x300?text=LG', 1 FROM dbo.Category WHERE cate_name = N'Màn hình'
    UNION ALL SELECT N'Tay cầm Xbox Wireless', cate_id, N'Tay cầm chơi game không dây.', 1490000, 9, N'https://placehold.co/300x300?text=Xbox', 1 FROM dbo.Category WHERE cate_name = N'Gear gaming'
    UNION ALL SELECT N'Hub USB-C 7 in 1', cate_id, N'Hub mở rộng cổng cho laptop.', 690000, 20, N'https://placehold.co/300x300?text=USB-C+Hub', 1 FROM dbo.Category WHERE cate_name = N'Phụ kiện';
END
GO
