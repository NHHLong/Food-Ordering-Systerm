using FoodOrderWeb.DAL;
using FoodOrderWeb.Models;

namespace FoodOrderWeb.BLL;

public class UserService
{
    private readonly UserRepository _users;

    public UserService(UserRepository users)
    {
        _users = users;
    }

    public User? Login(string username, string password)
    {
        if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
        {
            throw new InvalidOperationException("Please enter username and password.");
        }

        return _users.Login(username.Trim(), password);
    }

    public int Register(User user)
    {
        if (string.IsNullOrWhiteSpace(user.Username) || string.IsNullOrWhiteSpace(user.Password))
        {
            throw new InvalidOperationException("Username and password are required.");
        }

        user.Username = user.Username.Trim();
        if (_users.UsernameExists(user.Username))
        {
            throw new InvalidOperationException("Username already exists.");
        }

        user.Role = string.IsNullOrWhiteSpace(user.Role) ? "Customer" : user.Role;
        return _users.Register(user);
    }

    public List<User> GetAll() => _users.GetAll();

    public void SetActive(int userId, bool isActive) => _users.SetActive(userId, isActive);
}
