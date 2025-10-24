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
-keep class org.libreoffice.androidlib.ui.LOActivity { *; }

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

# Keep Java Beans classes required by Jackson and other libraries
-keep class java.beans.ConstructorProperties { *; }
-keep class java.beans.Transient { *; }

# Keep comprehensive Java reflection classes required by Google Guava and other libraries
-keep class java.lang.reflect.** { *; }
-keep class java.lang.reflect.AnnotatedType { *; }
-keep class java.lang.reflect.AnnotatedType** { *; }
-keep class java.lang.reflect.Type** { *; }
-keep class java.lang.reflect.Method { *; }
-keep class java.lang.reflect.Field { *; }
-keep class java.lang.reflect.Constructor { *; }
-keep class java.lang.reflect.Parameter { *; }
-keep class java.lang.reflect.Executable { *; }
-keep class java.lang.reflect.AccessibleObject { *; }

# Keep Jackson support classes
-keep class com.fasterxml.jackson.databind.ext.Java7SupportImpl { *; }
-dontwarn com.fasterxml.jackson.**

# Keep additional Java 8+ reflection classes that might be needed
-keep class java.lang.Class** { *; }
-keep class java.lang.Package** { *; }
-keep class java.lang.annotation.** { *; }

# Keep Google Guava reflection classes and suppress warnings
-keep class com.google.common.reflect.** { *; }
-keep class com.google.common.reflect.Invokable** { *; }
-dontwarn com.google.common.reflect.**
-dontwarn com.google.common.**
-dontwarn java.lang.reflect.**

# Keep data binding generated classes and their references
-keep class org.libreoffice.androidlib.databinding.** { *; }
