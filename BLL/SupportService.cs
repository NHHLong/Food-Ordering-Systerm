using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class SupportService
{
    private readonly SupportRepository _support;

    public SupportService(SupportRepository support)
    {
        _support = support;
    }

    public List<SupportRequest> GetAll() => _support.GetAll();

    public void Add(SupportRequest request)
    {
        if (string.IsNullOrWhiteSpace(request.Subject) || string.IsNullOrWhiteSpace(request.Message))
        {
            throw new InvalidOperationException("Subject and message are required.");
        }

        _support.Add(request);
    }
}
