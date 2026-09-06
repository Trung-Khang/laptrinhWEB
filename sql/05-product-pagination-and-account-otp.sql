/*
   KhangGear migration: product chronology, e-mail verification and OTP storage.
   Safe to execute repeatedly on ShoppingServiceMVC. It never drops tables or data.
*/
USE ShoppingServiceMVC;
GO

SET XACT_ABORT ON;
BEGIN TRANSACTION;

/* Preserve all old product data. A shared backfill date relies on id DESC as the stable tie-breaker. */
IF COL_LENGTH(N'dbo.products', N'created_at') IS NULL
    ALTER TABLE dbo.products ADD created_at DATETIME2 NULL;
IF COL_LENGTH(N'dbo.products', N'updated_at') IS NULL
    ALTER TABLE dbo.products ADD updated_at DATETIME2 NULL;

UPDATE dbo.products
SET created_at = COALESCE(created_at, CONVERT(DATETIME2, '2000-01-01T00:00:00')),
    updated_at = COALESCE(updated_at, created_at, CONVERT(DATETIME2, '2000-01-01T00:00:00'))
WHERE created_at IS NULL OR updated_at IS NULL;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID(N'dbo.products') AND name = N'IX_products_active_created_at_id')
    CREATE INDEX IX_products_active_created_at_id ON dbo.products(active, created_at DESC, id DESC);

/* Existing accounts remain usable; only new public registrations begin unverified. */
IF COL_LENGTH(N'dbo.[User]', N'email_verified') IS NULL
    ALTER TABLE dbo.[User] ADD email_verified BIT NULL;

EXEC(N'UPDATE dbo.[User] SET email_verified = 1 WHERE email_verified IS NULL;');

IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID(N'dbo.[User]') AND name = N'email_verified' AND is_nullable = 1)
    EXEC(N'ALTER TABLE dbo.[User] ALTER COLUMN email_verified BIT NOT NULL;');

IF NOT EXISTS (SELECT 1 FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID(N'dbo.[User]') AND parent_column_id = COLUMNPROPERTY(OBJECT_ID(N'dbo.[User]'), N'email_verified', 'ColumnId'))
    ALTER TABLE dbo.[User] ADD CONSTRAINT DF_User_email_verified DEFAULT (1) FOR email_verified;

IF OBJECT_ID(N'dbo.account_otps', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.account_otps (
        id INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_account_otps PRIMARY KEY,
        user_id INT NOT NULL,
        purpose VARCHAR(30) NOT NULL,
        otp_hash VARCHAR(64) NOT NULL,
        expires_at DATETIME2 NOT NULL,
        used_at DATETIME2 NULL,
        attempt_count INT NOT NULL CONSTRAINT DF_account_otps_attempt_count DEFAULT (0),
        created_at DATETIME2 NOT NULL CONSTRAINT DF_account_otps_created_at DEFAULT (SYSUTCDATETIME()),
        CONSTRAINT FK_account_otps_user FOREIGN KEY (user_id) REFERENCES dbo.[User](id),
        CONSTRAINT CK_account_otps_purpose CHECK (purpose IN ('REGISTER_VERIFY', 'PASSWORD_RESET')),
        CONSTRAINT CK_account_otps_attempt_count CHECK (attempt_count >= 0)
    );
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID(N'dbo.account_otps') AND name = N'IX_account_otps_user_purpose_created')
    CREATE INDEX IX_account_otps_user_purpose_created ON dbo.account_otps(user_id, purpose, created_at DESC);

COMMIT TRANSACTION;
GO
