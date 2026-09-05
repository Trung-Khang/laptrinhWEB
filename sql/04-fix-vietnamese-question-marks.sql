USE ShoppingServiceMVC;
GO

SET XACT_ABORT ON;
BEGIN TRANSACTION;

IF EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = N'products'
      AND COLUMN_NAME = N'name'
      AND DATA_TYPE <> N'nvarchar'
)
BEGIN
    ALTER TABLE dbo.products ALTER COLUMN name NVARCHAR(255) NOT NULL;
END;

IF EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = N'products'
      AND COLUMN_NAME = N'image'
      AND DATA_TYPE <> N'nvarchar'
)
BEGIN
    ALTER TABLE dbo.products ALTER COLUMN image NVARCHAR(500) NULL;
END;

-- Sua cac chuoi san pham bi mat dau tieng Viet do du lieu cu duoc luu sai encoding.
UPDATE dbo.products
SET name = N'Tay c' + NCHAR(7847) + N'm Xbox Wireless',
    description = COALESCE(NULLIF(description, N''), N'Tay c' + NCHAR(7847) + N'm ch' + NCHAR(417) + N'i game kh' + NCHAR(244) + N'ng d' + NCHAR(226) + N'y.')
WHERE name LIKE N'%Xbox Wireless%'
  AND (name LIKE N'%?%' OR name LIKE N'%Tay c%');

UPDATE dbo.products
SET name = N'b' + NCHAR(224) + N'n ph' + NCHAR(237) + N'm Aula f87 t' + NCHAR(7841) + N'o th' + NCHAR(7917) + N' test'
WHERE name LIKE N'%Aula f87%'
  AND (name LIKE N'%?%' OR name LIKE N'%test%');

UPDATE dbo.products
SET description = REPLACE(description, N'Tay c?m', N'Tay c' + NCHAR(7847) + N'm')
WHERE description LIKE N'%Tay c?m%';

UPDATE dbo.products
SET description = REPLACE(description, N't?o th?', N't' + NCHAR(7841) + N'o th' + NCHAR(7917))
WHERE description LIKE N'%t?o th?%';

UPDATE dbo.products
SET name = REPLACE(name, N'b?n ph?m', N'b' + NCHAR(224) + N'n ph' + NCHAR(237) + N'm')
WHERE name LIKE N'%b?n ph?m%';

UPDATE dbo.products
SET name = REPLACE(name, N'chu?t', N'chu' + NCHAR(7897) + N't')
WHERE name LIKE N'%chu?t%';

UPDATE dbo.Category
SET cate_name = REPLACE(cate_name, N'b?n ph?m', N'B' + NCHAR(224) + N'n ph' + NCHAR(237) + N'm')
WHERE cate_name LIKE N'%b?n ph?m%';

UPDATE dbo.Category
SET cate_name = REPLACE(cate_name, N'chu?t', N'Chu' + NCHAR(7897) + N't')
WHERE cate_name LIKE N'%chu?t%';

COMMIT TRANSACTION;
GO
