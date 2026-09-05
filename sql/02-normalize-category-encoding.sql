USE ShoppingServiceMVC;
GO
SET XACT_ABORT ON;
BEGIN TRANSACTION;

DECLARE @mapping TABLE (bad_name NVARCHAR(255) NOT NULL, good_name NVARCHAR(255) NOT NULL);
INSERT INTO @mapping (bad_name, good_name) VALUES
    (N'Äiá»‡n thoáº¡i', N'Điện thoại'),
    (N'BÃ n phÃ­m', N'Bàn phím'),
    (N'Chuá»™t', N'Chuột'),
    (N'MÃ n hÃ¬nh', N'Màn hình'),
    (N'Phá»¥ kiá»‡n', N'Phụ kiện');

DECLARE @badId INT, @goodId INT, @goodIcon NVARCHAR(255), @badIcon NVARCHAR(255);
DECLARE category_cursor CURSOR LOCAL FAST_FORWARD FOR
    SELECT bad.cate_id, good.cate_id, good.icons, bad.icons
    FROM dbo.Category bad
    JOIN @mapping m ON m.bad_name = bad.cate_name
    LEFT JOIN dbo.Category good ON good.cate_name = m.good_name;

OPEN category_cursor;
FETCH NEXT FROM category_cursor INTO @badId, @goodId, @goodIcon, @badIcon;
WHILE @@FETCH_STATUS = 0
BEGIN
    IF @goodId IS NULL
    BEGIN
        UPDATE dbo.Category
        SET cate_name = (SELECT good_name FROM @mapping WHERE bad_name = (SELECT cate_name FROM dbo.Category WHERE cate_id = @badId))
        WHERE cate_id = @badId;
    END
    ELSE
    BEGIN
        IF NULLIF(@goodIcon, N'') IS NULL AND NULLIF(@badIcon, N'') IS NOT NULL
            UPDATE dbo.Category SET icons = @badIcon WHERE cate_id = @goodId;
        IF OBJECT_ID(N'dbo.products', N'U') IS NOT NULL
            UPDATE dbo.products SET category_id = @goodId WHERE category_id = @badId;
        IF OBJECT_ID(N'dbo.videos', N'U') IS NOT NULL
            UPDATE dbo.videos SET categoryId = @goodId WHERE categoryId = @badId;
        DELETE FROM dbo.Category WHERE cate_id = @badId;
    END
    FETCH NEXT FROM category_cursor INTO @badId, @goodId, @goodIcon, @badIcon;
END
CLOSE category_cursor;
DEALLOCATE category_cursor;

COMMIT TRANSACTION;
GO
