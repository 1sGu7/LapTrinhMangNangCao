# Secure Client-Server System

Hệ thống Client-Server an toàn với các tính năng:
- Client ký số thông điệp bằng khóa riêng tư RSA
- Mã hóa khóa công khai bằng AES
- Server xác thực chữ ký và quét subdomain
- Giao diện người dùng thân thiện

## Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- Maven 3.6+
- Internet để tải thư viện

## Cài đặt
1. Clone repository:
```bash
git clone https://github.com/yourusername/secure-client-server.git
cd secure-client-server
```

2. Build project bằng Maven:
```bash
mvn clean package
```

## Tạo khóa RSA
Chạy chương trình tạo khóa:
```bash
java -cp target/secure-client-server-1.0-SNAPSHOT-jar-with-dependencies.jar crypto.KeyGeneratorApp
```
Hai file khóa `private.pem` và `public.pem` sẽ được tạo trong thư mục dự án.

## Chạy hệ thống

### Khởi động Server
```bash
java -jar target/secure-client-server-1.0-SNAPSHOT-jar-with-dependencies.jar server
```

### Khởi động Client
```bash
java -jar target/secure-client-server-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Hướng dẫn sử dụng
1. **Server**: Sau khi khởi động, server sẽ lắng nghe trên cổng 12345
2. **Client**:
   - Nhập thông điệp vào ô "Message"
   - Nhấn nút "Send" để gửi
   - Kết quả sẽ hiển thị trong ô "Response"

## Kiến trúc hệ thống
```
src/
├── main/
│   ├── java/
│   │   ├── client/       # Code phía client
│   │   ├── crypto/       # Mật mã và quản lý khóa
│   │   ├── model/        # Mô hình dữ liệu
│   │   ├── server/       # Code phía server
│   │   └── Main.java     # Điểm khởi chạy chính
│   └── resources/        # Tài nguyên (nếu có)
└── test/                 # Kiểm thử
```

## Giải thích các thành phần chính

### Client
- `ClientGUI.java`: Giao diện người dùng
- `ClientSocketHandler.java`: Xử lý kết nối đến server
- `ClientApp.java`: Khởi chạy ứng dụng client

### Server
- `ServerSocketHandler.java`: Xử lý kết nối và yêu cầu
- `SubdomainScanner.java`: Công cụ quét subdomain
- `Database.java`: Quản lý cơ sở dữ liệu SQLite
- `ServerApp.java`: Khởi chạy ứng dụng server

### Crypto
- `KeyManager.java`: Quản lý tải khóa RSA
- `CryptoUtils.java`: Tiện ích mã hóa/giải mã
- `KeyGeneratorApp.java`: Tạo cặp khóa RSA

## Xử lý sự cố thường gặp

### Kết nối không thành công
```log
Error: Connection refused
```
- Kiểm tra server đã chạy chưa
- Đảm bảo không có tường lửa chặn cổng 12345

### Lỗi xác thực
```log
VERIFICATION_FAILED
```
- Kiểm tra file `private.pem` và `public.pem` có khớp nhau
- Chạy lại KeyGeneratorApp để tạo cặp khóa mới

### Quét subdomain lâu
- Hệ thống đang quét danh sách lớn subdomain
- Kiểm tra kết nối internet để tải wordlist

## Đóng góp
1. Fork repository
2. Tạo branch mới (`git checkout -b feature/your-feature`)
3. Commit thay đổi (`git commit -am 'Add some feature'`)
4. Push branch (`git push origin feature/your-feature`)
5. Tạo Pull Request


---

File README này cung cấp hướng dẫn đầy đủ để người dùng có thể:
1. Cài đặt các công cụ cần thiết
2. Build project từ mã nguồn
3. Tạo khóa mã hóa
4. Khởi chạy server và client
5. Sử dụng hệ thống cơ bản
6. Xử lý các lỗi thường gặp

Để chạy dự án trong IntelliJ:
1. Mở project bằng IntelliJ
2. Chạy `KeyGeneratorApp` để tạo khóa
3. Chạy `ServerApp` để khởi động server
4. Chạy `ClientGUI` để khởi động client
5. Nhập thông điệp và nhấn "Send"
