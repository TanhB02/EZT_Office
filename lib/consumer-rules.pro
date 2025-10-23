# Keep all classes in the main package
-keep class org.libreoffice.androidlib.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep classes that extend Activity
-keep class * extends android.app.Activity { *; }

# Keep classes that extend Fragment
-keep class * extends androidx.fragment.app.Fragment { *; }

# Keep WebView related classes
-keep class android.webkit.** { *; }

# Keep JNI related classes
-keep class org.libreoffice.androidlib.LOActivity { *; }

# Keep all public and protected methods in public classes
-keepclassmembers class * {
    public protected <methods>;
}

# Keep important asset files
-keep class ** { *; }
-dontwarn android.webkit.**
-dontwarn org.libreoffice.**

# Keep Facebook Shimmer library classes
-keep class com.facebook.shimmer.** { *; }
-dontwarn com.facebook.shimmer.**

# Keep ShimmerFrameLayout specifically
-keep class com.facebook.shimmer.ShimmerFrameLayout { *; }

# Keep all methods and fields in ShimmerFrameLayout
-keepclassmembers class com.facebook.shimmer.ShimmerFrameLayout {
    public *;
    protected *;
}

# Keep Shimmer namespace attributes
-keep class **.R$attr { *; }
-keep class **.R$styleable { *; }
