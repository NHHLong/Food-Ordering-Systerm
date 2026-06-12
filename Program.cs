var builder = WebApplication.CreateBuilder(args);
builder.Logging.ClearProviders();
builder.Logging.AddConsole();
builder.Logging.AddDebug();

// Add services to the container.
builder.Services.AddControllersWithViews();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromHours(2);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

builder.Services.AddScoped<FoodOrderWeb.DAL.DbContext>();
builder.Services.AddScoped<FoodOrderWeb.DAL.UserRepository>();
builder.Services.AddScoped<FoodOrderWeb.DAL.CategoryRepository>();
builder.Services.AddScoped<FoodOrderWeb.DAL.FoodRepository>();
builder.Services.AddScoped<FoodOrderWeb.DAL.VoucherRepository>();
builder.Services.AddScoped<FoodOrderWeb.DAL.OrderRepository>();
builder.Services.AddScoped<FoodOrderWeb.DAL.ReviewRepository>();
builder.Services.AddScoped<FoodOrderWeb.DAL.SupportRepository>();
builder.Services.AddScoped<FoodOrderWeb.BLL.UserService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.CategoryService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.FoodService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.CartService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.VoucherService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.OrderService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.ReviewService>();
builder.Services.AddScoped<FoodOrderWeb.BLL.SupportService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseSession();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
