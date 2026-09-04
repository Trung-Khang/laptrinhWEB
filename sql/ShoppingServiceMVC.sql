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

-- 8. Tao bang orders va order_items cho module Quan ly Don hang
IF OBJECT_ID(N'dbo.orders', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.orders
    (
        id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NULL,
        customer_name NVARCHAR(150) NOT NULL,
        phone NVARCHAR(20) NULL,
        shipping_address NVARCHAR(500) NULL,
        order_date DATETIME2 NOT NULL CONSTRAINT DF_orders_order_date DEFAULT SYSDATETIME(),
        total_amount DECIMAL(18,2) NOT NULL CONSTRAINT DF_orders_total DEFAULT 0,
        status VARCHAR(30) NOT NULL CONSTRAINT DF_orders_status DEFAULT 'PENDING',
        note NVARCHAR(500) NULL,
        CONSTRAINT CK_orders_status CHECK (status IN ('PENDING','CONFIRMED','SHIPPING','COMPLETED','CANCELLED'))
    );
    CREATE INDEX IX_orders_status ON dbo.orders(status);
    CREATE INDEX IX_orders_order_date ON dbo.orders(order_date);
    CREATE INDEX IX_orders_customer_phone ON dbo.orders(customer_name, phone);
END
GO

IF OBJECT_ID(N'dbo.order_items', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.order_items
    (
        id INT IDENTITY(1,1) PRIMARY KEY,
        order_id INT NOT NULL,
        product_id INT NOT NULL,
        quantity INT NOT NULL CONSTRAINT CK_order_items_quantity CHECK (quantity > 0),
        unit_price DECIMAL(18,2) NOT NULL CONSTRAINT CK_order_items_unit_price CHECK (unit_price >= 0),
        subtotal DECIMAL(18,2) NOT NULL CONSTRAINT CK_order_items_subtotal CHECK (subtotal >= 0),
        CONSTRAINT FK_order_items_orders FOREIGN KEY (order_id) REFERENCES dbo.orders(id),
        CONSTRAINT FK_order_items_products FOREIGN KEY (product_id) REFERENCES dbo.products(id)
    );
    CREATE INDEX IX_order_items_order_id ON dbo.order_items(order_id);
    CREATE INDEX IX_order_items_product_id ON dbo.order_items(product_id);
END
GO

-- 9. Seed don hang mau neu chua co
IF NOT EXISTS (SELECT 1 FROM dbo.orders)
BEGIN
    DECLARE @p1 INT = (SELECT TOP 1 id FROM dbo.products ORDER BY id);
    DECLARE @p2 INT = (SELECT TOP 1 id FROM dbo.products WHERE id <> @p1 ORDER BY id);
    DECLARE @p3 INT = (SELECT TOP 1 id FROM dbo.products WHERE id NOT IN (@p1, @p2) ORDER BY id);

    INSERT INTO dbo.orders(customer_name, phone, shipping_address, order_date, total_amount, status, note)
    VALUES
    (N'Nguyễn Văn An', N'0901000001', N'Quận 1, TP.HCM', DATEADD(day, -5, SYSDATETIME()), 0, 'PENDING', N'Giao giờ hành chính'),
    (N'Trần Thị Bình', N'0901000002', N'Thủ Đức, TP.HCM', DATEADD(day, -4, SYSDATETIME()), 0, 'CONFIRMED', N'Khách đã xác nhận'),
    (N'Lê Minh Cường', N'0901000003', N'Quận 7, TP.HCM', DATEADD(day, -3, SYSDATETIME()), 0, 'SHIPPING', N'Đang giao'),
    (N'Phạm Hoài Dương', N'0901000004', N'Biên Hòa, Đồng Nai', DATEADD(day, -2, SYSDATETIME()), 0, 'COMPLETED', N'Đã thanh toán'),
    (N'Võ Ngọc Hân', N'0901000005', N'Dĩ An, Bình Dương', DATEADD(day, -1, SYSDATETIME()), 0, 'CANCELLED', N'Khách hủy');

    INSERT INTO dbo.order_items(order_id, product_id, quantity, unit_price, subtotal)
    SELECT o.id, @p1, 1, p.price, p.price FROM dbo.orders o CROSS JOIN dbo.products p WHERE p.id = @p1 AND o.customer_name = N'Nguyễn Văn An'
    UNION ALL SELECT o.id, @p2, 2, p.price, p.price * 2 FROM dbo.orders o CROSS JOIN dbo.products p WHERE p.id = @p2 AND o.customer_name = N'Trần Thị Bình'
    UNION ALL SELECT o.id, @p3, 1, p.price, p.price FROM dbo.orders o CROSS JOIN dbo.products p WHERE p.id = @p3 AND o.customer_name = N'Lê Minh Cường'
    UNION ALL SELECT o.id, @p1, 1, p.price, p.price FROM dbo.orders o CROSS JOIN dbo.products p WHERE p.id = @p1 AND o.customer_name = N'Phạm Hoài Dương'
    UNION ALL SELECT o.id, @p2, 1, p.price, p.price FROM dbo.orders o CROSS JOIN dbo.products p WHERE p.id = @p2 AND o.customer_name = N'Võ Ngọc Hân';

    UPDATE o
    SET total_amount = x.total
    FROM dbo.orders o
    INNER JOIN (
        SELECT order_id, SUM(subtotal) AS total
        FROM dbo.order_items
        GROUP BY order_id
    ) x ON x.order_id = o.id;
END
GO
