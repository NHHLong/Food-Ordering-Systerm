using FoodOrderWeb.Models;
using Microsoft.Data.SqlClient;

namespace FoodOrderWeb.DAL;

public class VoucherRepository
{
    private readonly DbContext _db;

    public VoucherRepository(DbContext db)
    {
        _db = db;
    }

    public Voucher? GetByCode(string code)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        const string query = @"
SELECT VoucherId, Code, Description, DiscountPercent, DiscountAmount, MinimumOrderValue,
       MaxUsage, UsedCount, StartDate, ExpiryDate, IsActive
FROM Vouchers
WHERE Code = @code";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@code", code);
        using var reader = cmd.ExecuteReader();
        return reader.Read() ? Map(reader) : null;
    }

    public List<Voucher> GetAll()
    {
        var vouchers = new List<Voucher>();
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand(@"SELECT VoucherId, Code, Description, DiscountPercent, DiscountAmount, MinimumOrderValue,
MaxUsage, UsedCount, StartDate, ExpiryDate, IsActive FROM Vouchers ORDER BY VoucherId", conn);
        using var reader = cmd.ExecuteReader();
        while (reader.Read())
        {
            vouchers.Add(Map(reader));
        }

        return vouchers;
    }

    public void Save(Voucher voucher)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        var query = voucher.VoucherId == 0
            ? @"INSERT INTO Vouchers (Code, Description, DiscountPercent, DiscountAmount, MinimumOrderValue, MaxUsage, UsedCount, StartDate, ExpiryDate, IsActive)
                VALUES (@code, @description, @percent, @amount, @minimum, @maxUsage, @usedCount, @start, @expiry, @isActive)"
            : @"UPDATE Vouchers SET Code = @code, Description = @description, DiscountPercent = @percent,
                DiscountAmount = @amount, MinimumOrderValue = @minimum, MaxUsage = @maxUsage,
                UsedCount = @usedCount, StartDate = @start, ExpiryDate = @expiry, IsActive = @isActive
                WHERE VoucherId = @voucherId";

        using var cmd = new SqlCommand(query, conn);
        cmd.Parameters.AddWithValue("@voucherId", voucher.VoucherId);
        cmd.Parameters.AddWithValue("@code", voucher.Code);
        cmd.Parameters.AddWithValue("@description", (object?)voucher.Description ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@percent", (object?)voucher.DiscountPercent ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@amount", (object?)voucher.DiscountAmount ?? DBNull.Value);
        cmd.Parameters.AddWithValue("@minimum", voucher.MinimumOrderValue);
        cmd.Parameters.AddWithValue("@maxUsage", voucher.MaxUsage);
        cmd.Parameters.AddWithValue("@usedCount", voucher.UsedCount);
        cmd.Parameters.AddWithValue("@start", voucher.StartDate);
        cmd.Parameters.AddWithValue("@expiry", voucher.ExpiryDate);
        cmd.Parameters.AddWithValue("@isActive", voucher.IsActive);
        cmd.ExecuteNonQuery();
    }

    public void MarkUsed(int voucherId)
    {
        using var conn = _db.GetConnection();
        conn.Open();

        using var cmd = new SqlCommand("UPDATE Vouchers SET UsedCount = UsedCount + 1 WHERE VoucherId = @id", conn);
        cmd.Parameters.AddWithValue("@id", voucherId);
        cmd.ExecuteNonQuery();
    }

    private static Voucher Map(SqlDataReader reader)
    {
        return new Voucher
        {
            VoucherId = (int)reader["VoucherId"],
            Code = reader["Code"].ToString() ?? string.Empty,
            Description = reader["Description"] as string,
            DiscountPercent = reader["DiscountPercent"] == DBNull.Value ? null : (int)reader["DiscountPercent"],
            DiscountAmount = reader["DiscountAmount"] == DBNull.Value ? null : (decimal)reader["DiscountAmount"],
            MinimumOrderValue = reader["MinimumOrderValue"] == DBNull.Value ? 0 : (int)reader["MinimumOrderValue"],
            MaxUsage = reader["MaxUsage"] == DBNull.Value ? 0 : (int)reader["MaxUsage"],
            UsedCount = reader["UsedCount"] == DBNull.Value ? 0 : (int)reader["UsedCount"],
            StartDate = reader["StartDate"] == DBNull.Value ? DateTime.Today : (DateTime)reader["StartDate"],
            ExpiryDate = reader["ExpiryDate"] == DBNull.Value ? DateTime.Today.AddYears(1) : (DateTime)reader["ExpiryDate"],
            IsActive = reader["IsActive"] != DBNull.Value && (bool)reader["IsActive"]
        };
    }
}
