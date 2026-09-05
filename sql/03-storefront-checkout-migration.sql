/*
   KhangGear storefront migration - safe to run more than once.
   Uses the existing ShoppingServiceMVC database and never drops tables or data.
*/
USE ShoppingServiceMVC;
GO

-- 1. Bổ sung các cột phục vụ thanh toán (nếu chưa có)
IF OBJECT_ID(N'dbo.orders', N'U') IS NOT NULL
BEGIN
    IF COL_LENGTH(N'dbo.orders', N'email') IS NULL
        ALTER TABLE dbo.orders ADD email NVARCHAR(150) NULL;

    IF COL_LENGTH(N'dbo.orders', N'payment_method') IS NULL
        ALTER TABLE dbo.orders ADD payment_method VARCHAR(30) NULL;

    IF COL_LENGTH(N'dbo.orders', N'payment_status') IS NULL
        ALTER TABLE dbo.orders ADD payment_status VARCHAR(30) NULL;
END
GO

-- 2. Tạo chỉ mục tối ưu truy vấn lịch sử đơn hàng
IF OBJECT_ID(N'dbo.orders', N'U') IS NOT NULL
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM sys.indexes 
        WHERE name = N'IX_orders_user_id_order_date' 
          AND object_id = OBJECT_ID(N'dbo.orders')
    )
    BEGIN
        CREATE INDEX IX_orders_user_id_order_date ON dbo.orders(user_id, order_date DESC);
    END
END
GO

-- 3. Chuẩn hóa dữ liệu thanh toán cho các đơn hàng cũ
IF OBJECT_ID(N'dbo.orders', N'U') IS NOT NULL
BEGIN
    UPDATE dbo.orders
    SET payment_method = COALESCE(payment_method, 'COD'),
        payment_status = COALESCE(payment_status, CASE WHEN status = 'COMPLETED' THEN 'PAID' ELSE 'PENDING' END)
    WHERE payment_method IS NULL OR payment_status IS NULL;
END
GO
