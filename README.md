# EZT_Office
## 📚 Contents
- 🚀 [Install](#-install)
- 🔧 [Core Functions](#-core-functions)
- 💡 [Usage Examples](#-usage-examples)

## Install
### Trong settings.gradle.kts hoặc build.gradle cấp project
```gradle
repositories {
    google()
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/TanhB02/EZT_Office")
        credentials {
            username = providers.gradleProperty("gpr.user").orElse(System.getenv("GITHUB_ACTOR") ?: "").get()
            password = providers.gradleProperty("gpr.token").orElse(System.getenv("GITHUB_TOKEN") ?: "").get()
        }
    }
}
```

### gradle.properties
```properties
gpr.user=TanhB02
gpr.token=MyToken
```

### build.gradle (Module:app)
```gradle
dependencies {
    implementation "ezt.documents:libreoffice:1.0.0-beta2"
}
```

### 🔧 Core Functions
| Function | Mô tả | Return | Lưu ý |
|----------|-------|--------|-------|
| `openFile(uri: Uri, callback: DocumentCallback)` | Mở file bằng LibreOffice (`LOActivity`). Tự động cấp quyền URI và đăng ký callback. | `Unit` | Yêu cầu `Uri` của file |
| `createFile(fileName: String, fileType: String = "xlsx", callback: DocumentCallback)` | Tạo file mới trong `MediaStore` từ template có sẵn, sau đó tự động mở bằng LibreOffice. | `suspend Unit` | Yêu cầu chạy trong coroutine |
| `openSystemPicker(callback: DocumentCallback)` | Mở trình chọn file hệ thống với danh sách MIME types hỗ trợ. | `Unit` | Fallback sang `ACTION_GET_CONTENT` nếu cần |
| `registerDocumentPicker()` | Đăng ký launcher cho `ActivityResultContracts.OpenDocument`. | `Unit` | Phải gọi trước khi sử dụng system picker |
| `setIDAdsBanner(idAdsBanner: String)` | Thiết lập ID quảng cáo banner | `Unit` | Phải gọi khi dùng libreofice |
| `setStateShowAds(stateShowAds: Boolean)` | Bật/tắt hiển thị quảng cáo | `Unit` | Phải gọi khi dùng libreofice |

### 💡 Usage Examples

#### 🔹 1. Mở file LibreOffice
```kotlin
openFile(fileUri, object : DocumentCallback {
    override fun onDocumentClosed() {
        // Xử lý khi LibreOffice đóng
        Log.d("DocumentAction", "LibreOffice document closed")
    }

    override fun onAdRevenueReceived(
        valueMicros: Long, 
        currencyCode: String, 
        precisionType: Int
    ) {
        // Xử lý doanh thu quảng cáo
        Log.d("AdRevenue", "Revenue: $valueMicros $currencyCode")
    }
})
```

#### 🔹 2. Tạo file mới và mở bằng LibreOffice
```kotlin
lifecycleScope.launch {
    createFile(
        fileName = "NewReport", 
        fileType = "xlsx", 
        object : DocumentCallback {
            override fun onDocumentClosed() {
                // Xử lý khi file mới được tạo và LibreOffice đóng
                Log.d("FileCreation", "New document created and closed")
            }
            
            override fun onAdRevenueReceived(
                valueMicros: Long, 
                currencyCode: String, 
                precisionType: Int
            ) {
                // Xử lý doanh thu quảng cáo từ file mới
                Log.d("AdRevenue", "Revenue from new file: $valueMicros $currencyCode")
            }
        }
    )
}
```

#### 🔹 3. Mở trình chọn file hệ thống
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Đăng ký document picker trước khi sử dụng
        registerDocumentPicker()
        
        // Mở system picker
        openSystemPicker(object : DocumentCallback {
            override fun onDocumentClosed() {
                // Xử lý khi file được chọn và LibreOffice đóng
                Log.d("SystemPicker", "Document selected and closed")
            }
            
            override fun onAdRevenueReceived(
                valueMicros: Long, 
                currencyCode: String, 
                precisionType: Int
            ) {
                // Xử lý doanh thu quảng cáo từ file được chọn
                Log.d("AdRevenue", "Revenue from picked file: $valueMicros $currencyCode")
            }
        })
    }
}
```

### 🌟 Quản Lý Quảng Cáo

#### 🔹 4. Thiết Lập ID Quảng Cáo Banner
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Thiết lập ID quảng cáo banner
        setIDAdsBanner("ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxxxxxxxx")
    }
}
```

#### 🔹 5. Điều Khiển Hiển Thị Quảng Cáo
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Bật hiển thị quảng cáo
        setStateShowAds(true)
        
        // Tắt hiển thị quảng cáo
        // setStateShowAds(false)
    }
}
```

## 🚨 Lưu Ý Quan Trọng
- Luôn gọi `registerDocumentPicker()` trước khi sử dụng `openSystemPicker()`
- Sử dụng `createFile()` trong coroutine
- Cung cấp `DocumentCallback` để xử lý các sự kiện như đóng tài liệu và doanh thu quảng cáo
- **BẮT BUỘC:** Thiết lập ID quảng cáo banner bằng `setIDAdsBanner()` trước khi sử dụng
- **BẮT BUỘC:** Xác định trạng thái hiển thị quảng cáo bằng `setStateShowAds()` 
  - `true`: Hiển thị quảng cáo
  - `false`: Ẩn quảng cáo
