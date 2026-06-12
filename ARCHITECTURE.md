# FoodOrderWeb Architecture

## Technology

- UI: ASP.NET Core MVC (.NET 8)
- Language: C#
- Database: SQL Server
- DB Tool: SQL Server Management Studio
- DB Access: ADO.NET with Microsoft.Data.SqlClient
- Architecture: Three-tier/layer architecture

## Three-layer Structure

```text
Presentation Layer
  Controllers + Views
  AccountController, MenuController, CartController, OrdersController, AdminController

Business Logic Layer
  BLL services
  UserService, FoodService, CartService, OrderService, VoucherService

Data Access Layer
  DAL repositories
  UserRepository, FoodRepository, OrderRepository, VoucherRepository

Database Layer
  SQL Server tables
  Users, Foods, Categories, Orders, OrderDetails, Vouchers...
```

Request flow:

```text
Browser
  -> MVC Controller
  -> BLL Service
  -> DAL Repository
  -> SQL Server Table
```

## Class Diagram

```mermaid
classDiagram
    class User {
        int UserId
        string Username
        string Password
        string Email
        string Phone
        string Role
        DateTime CreatedAt
        bool IsActive
    }

    class Category {
        int CategoryId
        string Name
        string Description
        bool IsActive
    }

    class Food {
        int FoodId
        string Name
        int CategoryId
        string Description
        decimal Price
        string Image
        string Status
        bool IsDeleted
        DateTime CreatedAt
    }

    class Cart {
        int CartId
        int UserId
        DateTime CreatedAt
    }

    class CartItem {
        int CartItemId
        int CartId
        int FoodId
        int Quantity
        decimal UnitPrice
    }

    class Order {
        int OrderId
        int UserId
        int VoucherId
        int AddressId
        DateTime OrderDate
        decimal TotalAmount
        string Status
        PaymentMethod PaymentMethod
        string ShippingAddress
    }

    class OrderDetail {
        int Id
        int OrderId
        int FoodId
        int Quantity
        decimal UnitPrice
    }

    class Voucher {
        int VoucherId
        string Code
        string Description
        int DiscountPercent
        decimal DiscountAmount
        int MinimumOrderValue
        int MaxUsage
        int UsedCount
        DateTime StartDate
        DateTime ExpiryDate
        bool IsActive
    }

    class Address {
        int AddressId
        int UserId
        string City
        string Street
        string BuildingNumber
    }

    class Review {
        int ReviewId
        int UserId
        int FoodId
        int Rating
        string Comment
        DateTime ReviewDate
    }

    class SupportRequest {
        int SupportRequestId
        int UserId
        string Subject
        string Message
        string Status
        DateTime CreatedAt
    }

    class PaymentMethod {
        <<enumeration>>
        Cash
        BankTransfer
        Card
    }

    User "1" --> "0..*" Cart
    Cart "1" --> "0..*" CartItem
    Food "1" --> "0..*" CartItem
    Category "1" --> "0..*" Food
    User "1" --> "0..*" Order
    Order "1" --> "1..*" OrderDetail
    Food "1" --> "0..*" OrderDetail
    Voucher "0..1" --> "0..*" Order
    User "1" --> "0..*" Address
    Address "0..1" --> "0..*" Order
    User "1" --> "0..*" Review
    Food "1" --> "0..*" Review
    User "1" --> "0..*" SupportRequest
    Order --> PaymentMethod
```

## Database Mapping

```mermaid
erDiagram
    Users ||--o{ Carts : owns
    Carts ||--o{ CartItems : contains
    Foods ||--o{ CartItems : selected

    Categories ||--o{ Foods : has

    Users ||--o{ Orders : places
    Orders ||--o{ OrderDetails : contains
    Foods ||--o{ OrderDetails : included
    Vouchers ||--o{ Orders : applies_to

    Users ||--o{ Addresses : has
    Addresses ||--o{ Orders : ships_to

    Users ||--o{ Reviews : writes
    Foods ||--o{ Reviews : receives

    Users ||--o{ SupportRequests : creates
    Orders ||--o{ Payments : paid_by
```

## Use Case Coverage

- Customer: Register, Login, Search Food, Manage Cart, Add Voucher, Payment, Place Order, View Order History, Track Order by status, Review, Support Customer.
- Admin: Manage Foods, Manage Categories, Manage Users, View Report, Update Order Status.
- Staff-compatible flow: Update Order Status, View Order History, Support Requests.
