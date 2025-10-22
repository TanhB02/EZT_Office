# EztPayWall Library - TanhX

## 📚 Contents

- 🚀 [Install](#install)
- ⚙️ [Setup Library](#setup-purchase--paywall)
- 🔥 [Firebase Remote Config](#setup-remote-config-firebase)
- 🧾 [Product Config Details](#-chi-tiết-cấu-hình-các-trường)
- 🖼️ [CMS (BE) - PayWall Setup](#cms---be-paywall-demo)
- 🛠️ [API Reference](#api-reference---purchaseutils-functions)
  - 🔧 [Core Functions](#-core-functions)
  - 💰 [Purchase Functions](#-purchase-functions)
  - 🎨 [UI Functions](#-ui-functions)
  - 💲 [Price & Product Info Functions](#-price--product-info-functions)
  - 🛠️ [Configuration Functions](#-configuration-functions)
  - 📊 [PayWall & Template Functions](#-paywall--template-functions)
- 📦 [PremiumHelper – Sử dụng nhanh](#-premiumhelper--ví-dụ-sử-dụng)


## Install

## Trong settings.gradle.kts hoặc build.gradle cấp project

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/TanhB02/TanhXEztPayWall")
            credentials {
                username = providers.gradleProperty("gpr.user").orElse(System.getenv("GITHUB_ACTOR") ?: "").get()
                password = providers.gradleProperty("gpr.token").orElse(System.getenv("GITHUB_TOKEN") ?: "").get()
            }
        }
    }
}
```

### gradle.properties --- 

```
gpr.user=TanhB02
gpr.token= github k cho push key public ----- CHECK TOKEN ĐƯỢC CẤP 
```

### build.gradle(Module:app)

```
dependencies {
	implementation ("TanhB02:eztpaywall:Recent versions")
}
```

## Setup Purchase + PayWall
Your application entends EztApplication

```
class App : EztApplication() {
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        //TODO Gán biến check đã premium hay chưa để check load ads dùng PurchaseUtils.isRemoveAds()
    }
    
    //TODO  TanhX - Setup local product config
    override fun getDefaultProductConfig(): RemoteProductConfig {
        return RemoteProductConfig(
            subscriptions = listOf(
                "product_id_yearly",
                "product_id_1monthly",
                "product_id_6monthly"
            ),
            oneTimeProducts = listOf("product_id_lifetime"),
            consumableProducts = emptyList(),
            removeAds = listOf(
                "product_id_yearly",
                "product_id_1monthly",
                "product_id_6monthly",
                "product_id_lifetime"
            )
        )
    }
}
```

## Setup Remote Config Firebase

### Remote config on Firebase (Product ID)

Parameter name (key): config_iap  
Data type: Json  
Mẫu value:
```
{
  "subscriptions": [
    "sub_yearly",
    "sub_1monthly",
    "sub_6monthly"
  ],
  "one_time_products": [
    "sub_lifetime"
  ],
  "consumable_products": [],
  "remove_ads": [
    "sub_yearly",
    "sub_1monthly",
    "sub_6monthly",
    "sub_lifetime"
  ],
  "free_trial": "sub-yearly-free-trial"
}
```

## 📋 Chi tiết cấu hình các trường

### Bảng mô tả chi tiết

| Trường | Loại dữ liệu | Mô tả | Cách sử dụng | Ví dụ |
|--------|--------------|-------|--------------|-------|
| **subscriptions** | `Array<String>` | **Add Product ID** của các gói subscription | Thêm trực tiếp Product ID được tạo trên Google Play Console | `["sub_yearly", "sub_1monthly", "sub_6monthly"]` |
| **one_time_products** | `Array<String>` | **Add Product ID** của các sản phẩm mua một lần | Thêm trực tiếp Product ID được tạo trên Google Play Console | `["sub_lifetime"]` |
| **consumable_products** | `Array<String>` | **Add Product ID** của các sản phẩm tiêu hao | Thêm trực tiếp Product ID được tạo trên Google Play Console | `[]` (empty - không có) |
| **remove_ads** | `Array<String>` | **Add Product ID** của các sản phẩm khi mua sẽ tắt ads | Thêm trực tiếp Product ID từ subscriptions hoặc one_time_products | `["sub_yearly", "sub_1monthly", "sub_6monthly", "sub_lifetime"]` |
| **free_trial** | `String` | **Base Plan ID** của subscription có free trial | Thêm Base Plan ID (không phải Product ID) của subscription có trial | `"sub-yearly-free-trial"` |

---

### CMS - BE (PayWall) Demo  
Setup Store và Key phải trùng với packagename của project

![Alt text](https://i.postimg.cc/YS7NLjj4/firebase.png)

Setup Template để call api lấy PayWall - Dưới native sẽ show PayWall với URL tương ứng được set

![Alt text](https://i.postimg.cc/YCYfSQ2K/template.png)

---

## 📚 API Reference - PurchaseUtils Functions

### 🔧 Core Functions

| Function | Mô tả | Return |
|----------|-------|--------|
| `init()` | Khởi tạo billing service | `Unit` |
| `addInitBillingFinishListener()` | Lắng nghe khi billing khởi tạo xong | `Unit` |

### 💰 Purchase Functions

| Function | Mô tả | Return |
|----------|-------|--------|
| `buy()` | Thực hiện mua hàng | `Unit` |
| `isRemoveAds()` | Kiểm tra user đã mua gói remove ads chưa | `Boolean` |
| `checkPurchased()` | Kiểm tra user đã mua sản phẩm nào chưa | `Boolean` |
| `setActionPurchase()` | Thực hiện action dựa trên trạng thái premium | `Unit` |

### 🎨 UI Functions

| Function | Mô tả | Return |
|----------|-------|--------|
| `showDialogPayWall()` | Hiển thị PayWall dạng Dialog | `Unit` |
| `showBottomSheetPayWall()` | Hiển thị PayWall dạng Bottom Sheet | `Unit` |
| `startActivityIAP()` | Mở PayWall Activity | `Unit` |

### 💲 Price & Product Info Functions

| Function | Mô tả | Return |
|----------|-------|--------|
| `getPrice()` | Lấy giá sản phẩm (có currency) | `String` |
| `getPriceWithoutCurrency()` | Lấy giá sản phẩm (không có currency) | `Float` |
| `getCurrency()` | Lấy đơn vị tiền tệ | `String` |
| `getDiscountPrice()` | Lấy giá khuyến mãi | `String` |

### 🛠️ Configuration Functions

| Function | Mô tả | Return |
|----------|-------|--------|
| `setCountryCode()` | Set mã quốc gia | `Unit` |
| `setDarkMode()` | Set dark mode | `Unit` |
| `checkFreeTrial()` | Kiểm tra free trial | `Boolean` |

### 📊 PayWall & Template Functions

| Function | Mô tả | Return |
|----------|-------|--------|
| `getPayWall()` | Lấy URL PayWall từ CMS | `String` |
| `getPayload()` | Lấy payload JSON cho PayWall | `String` |

## 📦 PremiumHelper – Ví dụ sử dụng

### ✅ Hiển thị Dialog PayWall

Sử dụng `PremiumHelper.showDialogPayWall()` để hiện dialog IAP với URL từ CMS:

```kotlin
PremiumHelper.showDialogPayWall(
    context = this,
    lifecycleCoroutineScope = lifecycleScope,
    screenName = "main_paywall", // key lấy từ CMS template
    onFailure = {}
)
```

---

### ✅ Hiển thị Bottom Sheet PayWall

Sử dụng `PremiumHelper.showBottomSheetPayWall()` để hiển thị PayWall dạng Bottom Sheet:

```kotlin
PremiumHelper.showBottomSheetPayWall(
    activity = this,
    lifecycleCoroutineScope = lifecycleScope,
    screenName = "main_paywall", // key lấy từ CMS template
    onFailure = { },
    watchAdsCallBack = {}
)
```

---

### ✅ Mở PayWall dạng Activity

Sử dụng `PremiumHelper.startIAP()` để mở PayWall dưới dạng activity:

```kotlin
PremiumHelper.startIAP(
    activity = this,
    lifecycleCoroutineScope = lifecycleScope,
    screenName = "main_paywall", // key lấy từ CMS template
    onReceivedError = {}
)
```

---

### 🔍 Gợi ý khi sử dụng:
- `screenName` là **key config CMS** mà backend định nghĩa cho từng màn.
- `onFailure` dùng khi URL trống hoặc web trả lỗi→ nên fallback show ui native
- `onReceivedError ` là callback khi paywall lỗi, timeout, chưa setup url trên Cms, Api lỗi
- `EZT_Purchase` tag liên quan đến Purchase, Paywall, Cms, Api, ...
- `onUpgradeNow` là callback khi user click để sang màn IAP (giành cho bottomsheet, dialog)
- `watchAdsCallBack` là callback khi user chấp nhận xem ads thay vì mua IAP (Dialog, BottomSheet)
- `onPurchaseSuccess` callback khi user mua thành công với PayWall (IAP)
- `onCloseClicked` callback khi user đóng màn PayWall (IAP)

