using System.ComponentModel.DataAnnotations;

namespace FoodOrderWeb.Models.ViewModels;

public class RegisterViewModel
{
    [Required]
    public string Username { get; set; } = string.Empty;

    [Required]
    public string Password { get; set; } = string.Empty;

    public string? Email { get; set; }
    public string? Phone { get; set; }
}
