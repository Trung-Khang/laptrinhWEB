USE ShoppingServiceMVC;
GO

IF OBJECT_ID(N'dbo.[User]', N'U') IS NOT NULL
BEGIN
    IF COL_LENGTH(N'dbo.[User]', N'phone') IS NULL
    BEGIN
        ALTER TABLE dbo.[User] ADD phone NVARCHAR(30) NULL;
    END;

    IF COL_LENGTH(N'dbo.[User]', N'avatar') IS NULL
    BEGIN
        ALTER TABLE dbo.[User] ADD avatar NVARCHAR(500) NULL;
    END;
END;
GO
