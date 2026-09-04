-- Script tao/cap nhat database DB_LapTrinhWeb cho module login cu.
-- Co the chay lai nhieu lan, khong xoa du lieu cu.

IF DB_ID(N'DB_LapTrinhWeb') IS NULL
BEGIN
    CREATE DATABASE DB_LapTrinhWeb;
END
GO

USE DB_LapTrinhWeb;
GO

IF OBJECT_ID(N'dbo.[User]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[User] (
        id INT IDENTITY(1,1) PRIMARY KEY,
        email VARCHAR(100),
        username VARCHAR(50) UNIQUE NOT NULL,
        fullname NVARCHAR(100),
        password VARCHAR(100) NOT NULL,
        avatar VARCHAR(255),
        roleid INT,
        phone VARCHAR(20),
        createddate DATE,
        active BIT NOT NULL CONSTRAINT DF_User_active DEFAULT 1
    );
END
GO

IF COL_LENGTH('dbo.[User]', 'active') IS NULL
BEGIN
    ALTER TABLE dbo.[User] ADD active BIT NOT NULL CONSTRAINT DF_User_active DEFAULT 1;
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.[User] WHERE username = 'admin')
BEGIN
    INSERT INTO dbo.[User] (email, username, fullname, password, roleid, createddate, active)
    VALUES ('admin@gmail.com', 'admin', N'Quản trị viên', '123', 1, GETDATE(), 1);
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.[User] WHERE username = 'manager')
BEGIN
    INSERT INTO dbo.[User] (email, username, fullname, password, roleid, createddate, active)
    VALUES ('manager@gmail.com', 'manager', N'Quản lý', '123', 2, GETDATE(), 1);
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.[User] WHERE username = 'user')
BEGIN
    INSERT INTO dbo.[User] (email, username, fullname, password, roleid, createddate, active)
    VALUES ('user@gmail.com', 'user', N'Khách hàng', '123', 3, GETDATE(), 1);
END
GO

SELECT id, email, username, fullname, roleid, phone, createddate, active
FROM dbo.[User];
GO
