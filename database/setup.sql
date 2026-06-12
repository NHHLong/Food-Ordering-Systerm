USE master;
GO

IF DB_ID('FoodOrderingDB') IS NULL
BEGIN
    CREATE DATABASE FoodOrderingDB;
END
GO

IF NOT EXISTS (SELECT 1 FROM sys.sql_logins WHERE name = 'foodorder_app')
BEGIN
    CREATE LOGIN foodorder_app WITH PASSWORD = 'FoodOrder@123';
END
GO

USE FoodOrderingDB;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'foodorder_app')
BEGIN
    CREATE USER foodorder_app FOR LOGIN foodorder_app;
END
GO

ALTER ROLE db_owner ADD MEMBER foodorder_app;
GO

IF OBJECT_ID('dbo.Users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Users (
        UserId INT IDENTITY(1,1) PRIMARY KEY,
        Username NVARCHAR(100) NOT NULL UNIQUE,
        Password NVARCHAR(255) NOT NULL,
        Email NVARCHAR(255) NULL,
        Phone NVARCHAR(50) NULL,
        Role NVARCHAR(50) NOT NULL DEFAULT 'Customer',
        CreatedAt DATETIME2 NULL DEFAULT SYSDATETIME(),
        IsActive BIT NOT NULL DEFAULT 1
    );
END
GO

IF OBJECT_ID('dbo.Categories', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Categories (
        CategoryId INT IDENTITY(1,1) PRIMARY KEY,
        Name NVARCHAR(100) NOT NULL,
        Description NVARCHAR(500) NULL,
        IsActive BIT NOT NULL DEFAULT 1
    );
END
GO

IF OBJECT_ID('dbo.Foods', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Foods (
        FoodId INT IDENTITY(1,1) PRIMARY KEY,
        Name NVARCHAR(150) NOT NULL,
        CategoryId INT NULL,
        Description NVARCHAR(1000) NULL,
        Price DECIMAL(18,2) NOT NULL DEFAULT 0,
        Image NVARCHAR(500) NULL,
        Status NVARCHAR(50) NULL DEFAULT 'Available',
        IsDeleted BIT NOT NULL DEFAULT 0,
        CreatedAt DATETIME2 NULL DEFAULT SYSDATETIME()
    );
END
GO

IF OBJECT_ID('dbo.Addresses', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Addresses (
        AddressId INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NOT NULL,
        City NVARCHAR(100) NULL,
        Street NVARCHAR(200) NULL,
        BuildingNumber NVARCHAR(50) NULL
    );
END
GO

IF OBJECT_ID('dbo.Vouchers', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Vouchers (
        VoucherId INT IDENTITY(1,1) PRIMARY KEY,
        Code NVARCHAR(50) NOT NULL UNIQUE,
        Description NVARCHAR(500) NULL,
        DiscountPercent INT NULL,
        DiscountAmount DECIMAL(18,2) NULL,
        MinimumOrderValue INT NOT NULL DEFAULT 0,
        MaxUsage INT NOT NULL DEFAULT 0,
        UsedCount INT NOT NULL DEFAULT 0,
        StartDate DATETIME2 NULL,
        ExpiryDate DATETIME2 NULL,
        IsActive BIT NOT NULL DEFAULT 1
    );
END
GO

IF OBJECT_ID('dbo.Orders', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Orders (
        OrderId INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NOT NULL,
        VoucherId INT NULL,
        AddressId INT NULL,
        OrderDate DATETIME2 NULL DEFAULT SYSDATETIME(),
        TotalAmount DECIMAL(18,2) NOT NULL DEFAULT 0,
        Status NVARCHAR(50) NOT NULL DEFAULT 'Pending',
        PaymentMethod NVARCHAR(50) NOT NULL DEFAULT 'Cash',
        ShippingAddress NVARCHAR(500) NULL
    );
END
GO

IF OBJECT_ID('dbo.OrderDetails', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.OrderDetails (
        Id INT IDENTITY(1,1) PRIMARY KEY,
        OrderId INT NOT NULL,
        FoodId INT NOT NULL,
        Quantity INT NOT NULL,
        UnitPrice DECIMAL(18,2) NOT NULL DEFAULT 0
    );
END
GO

IF OBJECT_ID('dbo.Carts', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Carts (
        CartId INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NOT NULL,
        CreatedAt DATETIME2 NULL DEFAULT SYSDATETIME()
    );
END
GO

IF OBJECT_ID('dbo.CartItems', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CartItems (
        CartItemId INT IDENTITY(1,1) PRIMARY KEY,
        CartId INT NOT NULL,
        FoodId INT NOT NULL,
        Quantity INT NOT NULL,
        UnitPrice DECIMAL(18,2) NOT NULL DEFAULT 0
    );
END
GO

IF OBJECT_ID('dbo.Reviews', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Reviews (
        ReviewId INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NOT NULL,
        FoodId INT NOT NULL,
        Rating INT NOT NULL,
        Comment NVARCHAR(1000) NULL,
        ReviewDate DATETIME2 NULL DEFAULT SYSDATETIME()
    );
END
GO

IF OBJECT_ID('dbo.SupportRequests', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.SupportRequests (
        SupportRequestId INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NOT NULL,
        Subject NVARCHAR(200) NOT NULL,
        Message NVARCHAR(1000) NOT NULL,
        Status NVARCHAR(50) NULL DEFAULT 'Open',
        CreatedAt DATETIME2 NULL DEFAULT SYSDATETIME()
    );
END
GO

IF OBJECT_ID('dbo.Notifications', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Notifications (
        NotificationId INT IDENTITY(1,1) PRIMARY KEY,
        UserId INT NULL,
        Title NVARCHAR(200) NOT NULL,
        Message NVARCHAR(1000) NOT NULL,
        IsRead BIT NOT NULL DEFAULT 0,
        CreatedAt DATETIME2 NULL DEFAULT SYSDATETIME()
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = 'admin')
BEGIN
    INSERT INTO dbo.Users (Username, Password, Email, Phone, Role, CreatedAt, IsActive)
    VALUES ('admin', '123456', 'admin@foodorder.local', '0900000000', 'Admin', SYSDATETIME(), 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = 'staff')
BEGIN
    INSERT INTO dbo.Users (Username, Password, Email, Phone, Role, CreatedAt, IsActive)
    VALUES ('staff', '123456', 'staff@foodorder.local', '0900000001', 'Staff', SYSDATETIME(), 1);
END

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = 'customer')
BEGIN
    INSERT INTO dbo.Users (Username, Password, Email, Phone, Role, CreatedAt, IsActive)
    VALUES ('customer', '123456', 'customer@foodorder.local', '0900000002', 'Customer', SYSDATETIME(), 1);
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Categories)
BEGIN
    INSERT INTO dbo.Categories (Name, Description, IsActive)
    VALUES
        ('Burger', 'Burger and fast food', 1),
        ('Pizza', 'Pizza menu', 1),
        ('Drink', 'Soft drinks and tea', 1);
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Foods)
BEGIN
    INSERT INTO dbo.Foods (Name, CategoryId, Description, Price, Image, Status, IsDeleted, CreatedAt)
    VALUES
        ('Cheese Burger', 1, 'Burger with cheese', 45000, '/images/burger.jpg', 'Available', 0, SYSDATETIME()),
        ('Pepperoni Pizza', 2, 'Classic pepperoni pizza', 99000, '/images/pepperoni pizza.jpg', 'Available', 0, SYSDATETIME()),
        ('Coca Cola', 3, 'Cold drink', 15000, '/images/coca.jpg', 'Available', 0, SYSDATETIME());
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Vouchers WHERE Code = 'WELCOME10')
BEGIN
    INSERT INTO dbo.Vouchers
        (Code, Description, DiscountPercent, DiscountAmount, MinimumOrderValue, MaxUsage, UsedCount, StartDate, ExpiryDate, IsActive)
    VALUES
        ('WELCOME10', '10 percent off for testing', 10, NULL, 0, 100, 0, SYSDATETIME(), DATEADD(YEAR, 1, SYSDATETIME()), 1);
END
GO
