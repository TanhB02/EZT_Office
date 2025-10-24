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
            username = "TanhB02"
            password = "MY_TOKEN"
        }
    }
}


### build.gradle (Module:app)
```gradle
dependencies {
    implementation "ezt.documents:libreoffice:release-1.0.0"
}
```

### 🔧 Core Functions
| Function | Mô tả | Return | Lưu ý |
|----------|-------|--------|-------|
| `openFile(context: Context, uri: Uri, callback: DocumentCallback)` | Mở file bằng LibreOffice (`LOActivity`). Tự động cấp quyền URI và đăng ký callback. | `Unit` | Yêu cầu `Context` và `Uri` của file |
| `createFile(context: Context, fileName: String, fileType: String = "xlsx", callback: DocumentCallback)` | Tạo file mới trong `MediaStore` từ template có sẵn, sau đó tự động mở bằng LibreOffice. | `suspend Unit` | Yêu cầu `Context` và chạy trong coroutine |
| `openSystemPicker(context: Context, callback: DocumentCallback)` | Mở trình chọn file hệ thống với danh sách MIME types hỗ trợ. | `Unit` | Yêu cầu `Context`, fallback sang `ACTION_GET_CONTENT` nếu cần |
| `registerDocumentPicker(activity: ComponentActivity)` | Đăng ký launcher cho `ActivityResultContracts.OpenDocument` trong Activity. | `Unit` | Phải gọi trước khi sử dụng system picker |
| `registerDocumentPicker(fragment: Fragment, context: Context)` | Đăng ký launcher cho `ActivityResultContracts.OpenDocument` trong Fragment. | `Unit` | Phải gọi trước khi sử dụng system picker |
| `setIDAdsBanner(context: Context, idAdsBanner: String)` | Thiết lập ID quảng cáo banner | `Unit` | Phải gọi khi dùng LibreOffice |
| `setStateShowAds(context: Context, stateShowAds: Boolean)` | Bật/tắt hiển thị quảng cáo | `Unit` | Phải gọi khi dùng LibreOffice |

### 💡 Usage Examples

#### 🔹 1. Mở file LibreOffice
```kotlin
UtilsOffice.openFile(context, fileUri, object : DocumentCallback {
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
  UtilsOffice.createFile(
    context = this@MainActivity,
    fileName = "NewReport",
    fileType = "xlsx",
    callback = object : DocumentCallback {
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

#### 🔹 3. Mở trình chọn file hệ thống (Activity)
```kotlin
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Đăng ký document picker trước khi sử dụng
    UtilsOffice.registerDocumentPicker(this)

    // Mở system picker
    UtilsOffice.openSystemPicker(this, object : DocumentCallback {
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

#### 🔹 3b. Mở trình chọn file hệ thống (Fragment)
```kotlin
class DocumentFragment : Fragment() {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Đăng ký document picker trong Fragment
    UtilsOffice.registerDocumentPicker(this, requireContext())

    // Mở system picker
    UtilsOffice.openSystemPicker(requireContext(), object : DocumentCallback {
      override fun onDocumentClosed() {
        Log.d("SystemPicker", "Document selected and closed")
      }

      override fun onAdRevenueReceived(
        valueMicros: Long,
        currencyCode: String,
        precisionType: Int
      ) {
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
    UtilsOffice.setIDAdsBanner(
      context = this,
      idAdsBanner = "ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxxxxxxxx"
    )
  }
}
```

#### 🔹 5. Điều Khiển Hiển Thị Quảng Cáo
```kotlin
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Bật hiển thị quảng cáo
    UtilsOffice.setStateShowAds(
      context = this,
      stateShowAds = true
    )

    // Tắt hiển thị quảng cáo
    // UtilsOffice.setStateShowAds(this, false)
  }
}
```

## 🚨 Lưu Ý Quan Trọng

### API Changes
- **TẤT CẢ các function** trong `UtilsOffice` đều yêu cầu tham số `context: Context` làm tham số đầu tiên
- Sử dụng `UtilsOffice.` prefix khi gọi các function (ví dụ: `UtilsOffice.openFile()`)

### Quy Tắc Sử Dụng
- ✅ Luôn gọi `UtilsOffice.registerDocumentPicker()` trước khi sử dụng `openSystemPicker()`
- ✅ Có 2 overload cho `registerDocumentPicker()`:
  - `registerDocumentPicker(activity: ComponentActivity)` - cho Activity
  - `registerDocumentPicker(fragment: Fragment, context: Context)` - cho Fragment
- ✅ Sử dụng `createFile()` trong coroutine (suspend function)
- ✅ Luôn cung cấp `DocumentCallback` để xử lý các sự kiện:
  - `onDocumentClosed()` - được gọi khi đóng tài liệu
  - `onAdRevenueReceived()` - được gọi khi có doanh thu quảng cáo

### Cấu Hình Quảng Cáo
- **BẮT BUỘC:** Thiết lập ID quảng cáo banner bằng `UtilsOffice.setIDAdsBanner(context, id)` trước khi sử dụng
- **BẮT BUỘC:** Xác định trạng thái hiển thị quảng cáo bằng `UtilsOffice.setStateShowAds(context, state)`
  - `true`: Hiển thị quảng cáo
  - `false`: Ẩn quảng cáo

## 🔄 Multi-Process Architecture

Library này sử dụng **multi-process architecture** với 2 processes:
- **Main Process** (`com.your.package`): Process chính của ứng dụng
- **LOActivity Process** (`com.your.package:loactivity`): Process riêng biệt cho LibreOffice Editor

### ⚠️ QUAN TRỌNG: Application.onCreate() Pattern

Do library chạy trong 2 processes riêng biệt, `Application.onCreate()` sẽ được gọi **2 lần** (mỗi process 1 lần). Để tránh khởi tạo không cần thiết và xung đột (ví dụ: khởi tạo Firebase, Analytics, v.v.), bạn **PHẢI** kiểm tra process name:

#### 🔹 6. Cấu Hình Application Class (BẮT BUỘC)

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // ⚠️ QUAN TRỌNG: Kiểm tra process name để tránh khởi tạo trùng lặp
        // Chỉ chạy logic khởi tạo trong main process
        val processName = getCurrentProcessNameCompat(this)
        if (processName != null && processName != applicationContext.packageName) {
            // Đây là LOActivity process (:loactivity)
            // KHÔNG khởi tạo các service/SDK không cần thiết
            return
        }

        // ✅ Code bên dưới CHỈ chạy trong main process

        // Khởi tạo Firebase, Analytics, Crash Reporting, v.v.
        FirebaseApp.initializeApp(this)

        // Khởi tạo các SDK khác
        // ...
    }

    /**
     * Lấy tên process hiện tại một cách tương thích với mọi phiên bản Android
     *
     * @param context Application context
     * @return Tên process hiện tại (ví dụ: "com.your.package" hoặc "com.your.package:loactivity")
     *         hoặc null nếu không thể xác định
     */
    private fun getCurrentProcessNameCompat(context: Context): String? {
        // Android P (API 28) trở lên: sử dụng API chính thức
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }

        // Android O (API 27) trở xuống: đọc từ /proc/[pid]/cmdline
        return try {
            val pid = android.os.Process.myPid()
            val reader = File("/proc/$pid/cmdline").bufferedReader()
            reader.use { it.readLine()?.trim('\u0000') }
        } catch (e: Exception) {
            null
        }
    }
}
```

### Tại sao cần kiểm tra process name?

#### ❌ Vấn đề nếu KHÔNG kiểm tra:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // ⚠️ KHÔNG TỐT: Code này sẽ chạy 2 lần!
        FirebaseApp.initializeApp(this)  // Khởi tạo 2 lần → lãng phí tài nguyên
        initAnalytics()                   // Có thể gây xung đột
        setupDatabase()                   // Database có thể bị khởi tạo trùng lặp
    }
}
```

**Hậu quả:**
- 🔴 Firebase khởi tạo 2 lần → tốn memory và CPU
- 🔴 Analytics tracking bị duplicate → số liệu sai
- 🔴 Database connection conflicts → crash app
- 🔴 Crash reporting SDK khởi tạo 2 lần → báo cáo sai
- 🔴 Tốn thời gian khởi động không cần thiết

#### ✅ Giải pháp: Kiểm tra process name

```kotlin
val processName = getCurrentProcessNameCompat(this)
if (processName != null && processName != applicationContext.packageName) {
    // Đây là :loactivity process → KHÔNG khởi tạo gì cả
    return
}

// ✅ TỐT: Code này CHỈ chạy trong main process
FirebaseApp.initializeApp(this)  // ✅ Chỉ khởi tạo 1 lần
```

**Lợi ích:**
- ✅ Tiết kiệm memory và CPU
- ✅ Tránh xung đột giữa các processes
- ✅ Khởi động app nhanh hơn
- ✅ Tracking chính xác
- ✅ Tránh crash do duplicate initialization

### Chi tiết về `getCurrentProcessNameCompat()`

Hàm này xác định process name một cách tương thích với mọi phiên bản Android:

| Android Version | Method | Implementation |
|----------------|--------|----------------|
| Android P+ (API 28+) | `Application.getProcessName()` | API chính thức, đơn giản và an toàn |
| Android O- (API 27-) | Đọc `/proc/[pid]/cmdline` | Fallback cho các phiên bản cũ |

**Return values:**
- `"com.your.package"` → Main process
- `"com.your.package:loactivity"` → LOActivity process
- `null` → Không xác định được (hiếm gặp)
