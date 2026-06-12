# FoodOrder Spring Boot + React Refactor

This refactor replaces the old ASP.NET Core MVC project with Spring Boot + React while keeping the original three-tier/layer architecture and use-case logic:

```text
backend-spring
  controller  -> REST API layer
  service     -> service interfaces
  service/impl -> business logic implementations
  dao         -> DAO interfaces, DAO implementations, GenericDAO
  model       -> JPA entities mapped from the class diagram
  dto         -> request/response data transfer objects

frontend-react
  pages       -> use-case screens
  components  -> reusable UI
  services    -> API client
  public/images -> copied assets from wwwroot/images
```

The old C# project files and MVC folders were removed after the port:

```text
BLL, DAL, Controllers, Models, Views, wwwroot, Program.cs, FoodOrderWeb.csproj
```

## Backend

The backend follows the tutorial flow:

```text
UI / React
  -> Controller / REST API
  -> Service interface + ServiceImpl
  -> DAO interface + DAOImpl
  -> GenericDAOImpl
  -> JPA Entity / JPQL
  -> SQL Server tables
```

The class diagram is represented as JPA entities in `model`. Hibernate maps those entities to the local SQL Server database configured in `application.properties`.

Request data goes through DTOs first. Service implementations map DTO Request objects to entities before calling DAO methods, matching the tutorial's workflow.

Run after installing Java 21 and Maven:

```powershell
cd backend-spring
mvn spring-boot:run
```

Default API URL:

```text
http://localhost:8080/api
```

SQL Server config is in:

```text
backend-spring/src/main/resources/application.properties
```

For a new machine, open SQL Server Management Studio and run:

```text
database/setup.sql
```

That script creates `FoodOrderingDB`, the `foodorder_app` SQL login, all mapped tables, and seed accounts:

```text
admin / 123456
staff / 123456
customer / 123456
```

If Windows authentication does not work with the JDBC driver on your machine, switch the datasource URL to SQL authentication and add:

```properties
spring.datasource.username=your_user
spring.datasource.password=your_password
```

## Frontend

Run after installing Node dependencies:

```powershell
cd frontend-react
npm.cmd install
npm.cmd run dev
```

Default app URL:

```text
http://127.0.0.1:5173
```

If the backend runs on another port, create `.env` in `frontend-react`:

```text
VITE_API_BASE_URL=http://localhost:8080/api
```

## Use Case Mapping

- Register/Login: `AuthController` -> `UserService`/`UserServiceImpl` -> `IUserDAO`/`UserDAOImpl`
- Search Food: `MenuController` -> `FoodService`/`FoodServiceImpl` -> `IFoodDAO`/`FoodDAOImpl`
- Manage Cart/Add Voucher/Payment/Place Order/Shipping: `CartController` -> `CartService`/`VoucherService`/`OrderService` -> DAO layer
- View Order History/Track Status: `OrderController` -> `OrderService`/`OrderServiceImpl` -> `IOrderDAO`/`OrderDAOImpl`
- Review: `ReviewController` -> `ReviewService`/`ReviewServiceImpl` -> `IReviewDAO`/`ReviewDAOImpl`
- Support Customer: `SupportController` -> `SupportService`/`SupportServiceImpl` -> `ISupportDAO`/`SupportDAOImpl`
- Notification: `NotificationController` -> `NotificationService`/`NotificationServiceImpl` -> `INotificationDAO`/`NotificationDAOImpl`
- Update Order Status/Support Customer/View Order History for Staff: `StaffController` -> service layer -> DAO layer
- Manage Foods/Categories/Users/Orders/Vouchers/View Report: `AdminController` -> service layer -> DAO layer

## Class Diagram Mapping

- `User` -> `Users`
- `Food` -> `Foods`
- `Category` -> `Categories`
- `Cart` + `CartItem` -> `Carts` + `CartItems`
- `Order` + `OrderDetail` -> `Orders` + `OrderDetails`
- `Address` -> `Addresses`
- `Voucher` -> `Vouchers`
- `Review` -> `Reviews`
- `PaymentMethod` -> enum values `Cash`, `BankTransfer`
- `Notification` -> `Notifications`
