# Phu Kien Cong Nghe - Ứng Dụng Thương Mại Điện Tử Di Động

👥 **Số lượng thành viên:** 4

Ứng dụng Android thương mại điện tử toàn diện dành cho mua bán phụ kiện điện tử. Xây dựng với các mẫu kiến trúc Android hiện đại và theo đúng thực hành tốt nhất, bao gồm xác thực an toàn, quản lý sản phẩm real-time, và theo dõi giao hàng thông minh.

## 🛠️ Stack & Công Nghệ

- **Ngôn ngữ:** Java 11
- **Framework:** Android (API 26+, Target API 36)
- **Kiến trúc:** Clean Architecture + MVVM Pattern
- **Dependency Injection:** Hilt
- **Cơ sở dữ liệu cục bộ:** Room (SQLite)
- **Mạng:** Retrofit + OkHttp với Quản lý Token JWT
- **Tải ảnh:** Glide
- **Xác thực:** Firebase Auth (Google Sign-In)
- **Lưu trữ đám mây:** Firebase Storage
- **Bản đồ & Vị trí:** Google Maps Platform (Geocoding/Places/Maps SDK)
- **Thành phần UI:** ViewPager2, Material Design, RecyclerView
- **Tính năng Real-time:** Chatbot gợi ý sản phẩm và tư vấn

## 📁 Cấu Trúc Dự Án

```
app/src/main/java/com/infix/phukiencongnghe/
├── ui/                           Lớp Giao Diện Người Dùng
│   ├── auth/                      Màn hình xác thực
│   ├── main/                      Màn hình chính
│   ├── search/                    Tìm kiếm sản phẩm
│   ├── searchadvance/             Tìm kiếm nâng cao
│   ├── product_category/          Duyệt danh mục
│   ├── cart/                      Quản lý giỏ hàng
│   ├── order/                     Quản lý đơn hàng
│   ├── payment/                   Xử lý thanh toán
│   ├── user_manage/               Hồ sơ người dùng
│   ├── setting/                   Cài đặt ứng dụng
│   ├── admin/                     Bảng quản trị
│   ├── infoshop/                  Thông tin cửa hàng
│   ├── dialog/                    Hộp thoại tái sử dụng
│   ├── header/                    Header/toolbar
│   ├── adapter/                   RecyclerView adapters
│   ├── voucher/                   Quản lý voucher
│   └── share_viewmodel/           Shared ViewModels
├── data/                          Lớp Dữ Liệu
│   ├── source/                    Nguồn dữ liệu
│   ├── repository/                Repository pattern
│   ├── model/                     Thực thể Room
│   └── dto/                       Đối tượng chuyển dữ liệu
├── di/                            Tiêm Phụ Thuộc (Hilt)
│   ├── FirebaseModule.java        Firebase services
│   ├── RetrofitModule.java        Cấu hình mạng
│   ├── MyDatabaseModule.java      Database setup
│   ├── repository/                Repository bindings
│   └── source/                    Data source bindings
├── common/                        Tài nguyên chung
├── utils/                         Các lớp tiện ích
│   ├── ApiClient.java             JWT token management
│   ├── InjectUtils.java           DI utilities
│   ├── AppExecutors.java          Thread pool
│   ├── SharePrefUtils.java        SharedPreferences
│   ├── AppUtils.java              Utilities
│   ├── KeyboardUtils.java         Keyboard management
│   ├── SnackbarUtils.java         Toast/Snackbar
│   └── paging/                    Pagination
└── MyApplication.java             Application class
```

## ✨ Các Tính Năng Chính

### 🔐 Xác Thực & Quản Lý Người Dùng
- Tích hợp Google Sign-In qua Firebase
- Đăng ký email/mật khẩu với xác minh email
- Chức năng đặt lại mật khẩu qua email
- Xác thực dựa trên token JWT với tự động làm mới
- Quản lý hồ sơ người dùng với dữ liệu được lưu vào bộ nhớ cache

### 🛍️ Quản Lý Sản Phẩm
- Duyệt sản phẩm theo danh mục
- Tìm kiếm nâng cao với nhiều bộ lọc
- Xem chi tiết sản phẩm với phòng trưng bày hình ảnh
- Lựa chọn biến thể sản phẩm
- Theo dõi lịch sử tìm kiếm

### 🛒 Mua Sắm & Đơn Hàng
- Giỏ hàng với lưu trữ bền vững
- Tạo và quản lý đơn hàng
- Theo dõi lịch sử đơn hàng
- Nhiều trạng thái đơn hàng

### 📍 Dịch Vụ Giao Hàng & Vị Trí
- Quản lý địa chỉ với nhiều điểm giao hàng
- Google Places autocomplete
- Geocoding để phát hiện vĩ độ/kinh độ
- Tích hợp bản đồ
- Vị trí real-time

### 💳 Thanh Toán & Khuyến Mãi
- Xử lý thanh toán an toàn
- Ứng dụng mã voucher/khuyến mãi
- Tính toán chiết khấu

### 💬 Liên Lạc
- Chatbot gợi ý sản phẩm
- Tư vấn sản phẩm real-time
- Chat hỗ trợ khách hàng

### ⚙️ Bảng Quản Trị
- Quản lý sản phẩm
- Quản lý đơn hàng
- Quản lý người dùng

## 💾 Cơ Sở Dữ Liệu Cục Bộ (Room)

Ứng dụng sử dụng Room để lưu vào bộ nhớ cache:
- Dữ liệu xác thực người dùng
- Lịch sử tìm kiếm
- Biến thể sản phẩm
- Đơn hàng và mục giỏ hàng
- Địa chỉ người dùng
- Tùy chọn giao hàng

Điều này đảm bảo ứng dụng hoạt động mượt mà ngay cả khi bị ngắt kết nối mạng.

## 🌐 Cấu Hình Mạng

### 🔄 Xử Lý Token JWT
Lớp `ApiClient` quản lý:
- Tự động làm mới token khi access token hết hạn
- Lưu token bền vững trong các phiên ứng dụng
- Request/response interceptor để tiêm token
- Lưu trữ token an toàn qua SharedPreferences

### 📡 Thiết Lập HTTP Client
- Retrofit với Gson converter
- OkHttp với custom interceptor
- Cấu hình timeout cho các lệnh gọi API
- Request/response logging

### 🔥 Tích Hợp Firebase
- Xác thực Firebase cho tài khoản người dùng
- Firebase Storage cho hình ảnh sản phẩm
- Quản lý token tự động

## 🚀 Bắt Đầu

### 📋 Yêu Cầu Trước

Trước khi chạy ứng dụng, bạn cần thiết lập máy chủ backend:

**[android-ecommerce-backend](https://github.com/NTV-CKN/android-ecommerce-backend)**

### ⚙️ Hướng Dẫn Thiết Lập

1. **Clone Repository**
   ```bash
   git clone https://github.com/NTV-CKN/android-ecommerce-app.git
   cd android-ecommerce-app
   ```

2. **Cấu Hình Google Maps API Key**
   - Tạo file `local.properties` tại thư mục gốc dự án
   ```properties
   MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
   ```

3. **Cấu Hình Firebase**
   - Tải `google-services.json` từ Firebase Console
   - Đặt vào thư mục `app/`

4. **Build & Chạy**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

### 📦 Yêu Cầu Build
- Android SDK: API 26+
- Target SDK: 36
- Java: JDK 11+
- Gradle: 8.x+

## 🏗️ Tổng Quan Kiến Trúc

**Clean Architecture** với ba lớp:

1. **Presentation Layer (UI)**
   - Activities và Fragments
   - ViewModels, LiveData
   - Adapters

2. **Domain Layer**
   - Repository interfaces
   - Use cases

3. **Data Layer**
   - Repository implementations
   - Local/Remote data sources
   - DTOs

## 🎯 Thiết Lập Tiêm Phụ Thuộc

Quản lý qua Hilt:
- **FirebaseModule:** Firebase services
- **RetrofitModule:** API client
- **MyDatabaseModule:** Room database
- **Repository Modules:** Bindings

## ⚠️ Ghi Chú Quan Trọng

- Ứng dụng yêu cầu kết nối internet
- Cần Google Maps API key
- Firebase phải được cấu hình đúng
- Token JWT tự động làm mới
- Dữ liệu nhạy cảm lưu trữ an toàn

## 🔧 Khắc Phục Sự Cố

### Ứng dụng crash khi khởi động
- Xác minh backend đang chạy
- Kiểm tra Google Maps API key
- Đảm bảo Firebase hoàn tất

### Tính năng vị trí không hoạt động
- Xác minh API key hợp lệ
- Kiểm tra quyền vị trí thiết bị
- Bật Geocoding/Places API

### Vấn đề xác thực
- Kiểm tra Firebase Authentication
- Xác minh Google Sign-In
- Kiểm tra internet

---

**Để thiết lập backend:** [android-ecommerce-backend](https://github.com/NTV-CKN/android-ecommerce-backend)
