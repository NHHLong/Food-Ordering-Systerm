using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class OrderRepository
{
    private readonly DbContext _db;

    public OrderRepository(DbContext db)
    {
        _db = db;
    }

    public int CreateOrder(Order order)
    {
        using var conn = _db.GetConnection();
        conn.Open();
        using var tx = conn.BeginTransaction();

        try
        {
            const string orderQuery = @"
INSERT INTO Orders (UserId, VoucherId, AddressId, OrderDate, TotalAmount, Status, PaymentMethod, ShippingAddress)
OUTPUT INSERTED.OrderId
VALUES (@userId, @voucherId, @addressId, GETDATE(), @total, @status, @payment, @shippingAddress)";

            using var orderCmd = new SqlCommand(orderQuery, conn, tx);
            orderCmd.Parameters.AddWithValue("@userId", order.UserId);
            orderCmd.Parameters.AddWithValue("@voucherId", (object?)order.VoucherId ?? DBNull.Value);
            orderCmd.Parameters.AddWithValue("@addressId", (object?)order.AddressId ?? DBNull.Value);
            orderCmd.Parameters.AddWithValue("@total", order.TotalAmount);
            orderCmd.Parameters.AddWithValue("@status", order.Status);
            orderCmd.Parameters.AddWithValue("@payment", order.PaymentMethod.ToString());
            orderCmd.Parameters.AddWithValue("@shippingAddress", (object?)order.ShippingAddress ?? DBNull.Value);
            var orderId = (int)orderCmd.ExecuteScalar()!;

            const string detailQuery = @"
INSERT INTO OrderDetails (OrderId, FoodId, Quantity, UnitPrice)
VALUES (@orderId, @foodId, @quantity, @unitPrice)";

            foreach (var detail in order.Details)
            {
                using var detailCmd = new SqlCommand(detailQuery, conn, tx);
                detailCmd.Parameters.AddWithValue("@orderId", orderId);
                detailCmd.Parameters.AddWithValue("@foodId", detail.FoodId);
                detailCmd.Parameters.AddWithValue("@quantity", detail.Quantity);
                detailCmd.Parameters.AddWithValue("@unitPrice", detail.UnitPrice);
                detailCmd.ExecuteNonQuery();
            }

            tx.Commit();
            return orderId;
        }
        catch
        {
            tx.Rollback();
            throw;
        }
    }

    public List<Order> GetOrders(int? userId = null)
    {
        var orders = new List<Order>();
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT o.OrderId, o.UserId, u.Username, o.VoucherId, o.AddressId, o.OrderDate, o.TotalAmount,
       o.Status, o.PaymentMethod, o.ShippingAddress
FROM Orders o
JOIN Users u ON o.UserId = u.UserId
WHERE (@userId IS NULL OR o.UserId = @userId)
ORDER BY o.OrderDate DESC";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@userId", userId.HasValue ? userId.Value : DBNull.Value);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            orders.Add(MapOrder(reader));
        }

        return orders;
    }

    public List<OrderDetail> GetDetails(int orderId)
    {
        var details = new List<OrderDetail>();
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT od.Id, od.OrderId, od.FoodId, f.Name AS FoodName, od.Quantity, od.UnitPrice
FROM OrderDetails od
JOIN Foods f ON od.FoodId = f.FoodId
WHERE od.OrderId = @orderId";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@orderId", orderId);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            details.Add(new OrderDetail
            {
                Id = (int)reader["Id"],
                OrderId = (int)reader["OrderId"],
                FoodId = (int)reader["FoodId"],
                FoodName = reader["FoodName"].ToString() ?? string.Empty,
                Quantity = (int)reader["Quantity"],
                UnitPrice = (decimal)reader["UnitPrice"]
            });
        }

        return details;
    }

    public void UpdateStatus(int orderId, string status)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("UPDATE Orders SET Status = @status WHERE OrderId = @orderId", conn);
        cmd.Parameters.AddWithValue("@status", status);
        cmd.Parameters.AddWithValue("@orderId", orderId);
        cmd.ExecuteNonQuery();
    }

    public decimal GetRevenue()
    {
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("SELECT COALESCE(SUM(TotalAmount), 0) FROM Orders", conn);
        return (decimal)cmd.ExecuteScalar()!;
    }

    private static Order MapOrder(SqlDataReader reader)
    {
        Enum.TryParse(reader["PaymentMethod"].ToString(), out PaymentMethod paymentMethod);
        return new Order
        {
            OrderId = (int)reader["OrderId"],
            UserId = (int)reader["UserId"],
            Username = reader["Username"].ToString() ?? string.Empty,
            VoucherId = reader["VoucherId"] == DBNull.Value ? null : (int)reader["VoucherId"],
            AddressId = reader["AddressId"] == DBNull.Value ? null : (int)reader["AddressId"],
            OrderDate = (DateTime)reader["OrderDate"],
            TotalAmount = (decimal)reader["TotalAmount"],
            Status = reader["Status"].ToString() ?? "Pending",
            PaymentMethod = paymentMethod,
            ShippingAddress = reader["ShippingAddress"] as string
        };
    }
}
