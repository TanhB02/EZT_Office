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

# Keep LibreOffice web assets
-keep class * extends android.app.Activity {
    public void *(android.webkit.WebView);
}

# Keep web resources that are loaded via file:///android_asset/
-keepclassmembers class * {
    public static *** getAssetManager(...);
}
