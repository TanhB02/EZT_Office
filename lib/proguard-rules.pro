# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class org.libreoffice.androidlib.ui.LOActivity {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep JNI methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep all classes in the main package
-keep class org.libreoffice.androidlib.** { *; }

# Keep WebView related classes
-keep class android.webkit.** { *; }
-keep class androidx.webkit.** { *; }

# Keep classes that extend Activity
-keep class * extends android.app.Activity { *; }

# Keep classes that extend Fragment
-keep class * extends androidx.fragment.app.Fragment { *; }

# Keep all public and protected methods in public classes
-keepclassmembers class * {
    public protected <methods>;
}

# Keep all fields in public classes
-keepclassmembers class * {
    public protected <fields>;
}

# Keep Serializable classes
-keep class * implements java.io.Serializable { *; }

# Keep classes needed for Java 11
-keep class java.lang.invoke.StringConcatFactory { *; }
-keep class java.lang.invoke.MethodHandle { *; }
-keep class java.lang.invoke.MethodHandles { *; }
-keep class java.lang.invoke.MethodHandles$Lookup { *; }
-keep class java.lang.invoke.MethodType { *; }
-keep class java.util.concurrent.ForkJoinPool { *; }
-keep class java.util.concurrent.ForkJoinTask { *; }
-keep class java.util.concurrent.ForkJoinWorkerThread { *; }
-keep class java.util.concurrent.RecursiveAction { *; }
-keep class java.util.concurrent.RecursiveTask { *; }

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

# Keep all invoke related classes
-keep class java.lang.invoke.** { *; }
-keep class java.util.concurrent.** { *; }

# Keep Kotlin metadata for proper reflection
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }

# Keep all classes that might use string concatenation
-keepclasseswithmembers class * {
    public static ** main(java.lang.String[]);
}

# Keep specific classes mentioned in the error
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn java.lang.invoke.MethodHandle
-dontwarn java.lang.invoke.MethodHandles
-dontwarn java.lang.invoke.MethodHandles$Lookup
-dontwarn java.lang.invoke.MethodType

# Keep Facebook Shimmer library classes
-keep class com.facebook.shimmer.** { *; }
-dontwarn com.facebook.shimmer.**

# Keep ShimmerFrameLayout specifically
-keep class com.facebook.shimmer.ShimmerFrameLayout { *; }

# Keep Jackson support classes
-keep class com.fasterxml.jackson.databind.ext.Java7SupportImpl { *; }
-dontwarn com.fasterxml.jackson.**

# Keep data binding generated classes and their references
-keep class org.libreoffice.androidlib.databinding.** { *; }

# Keep additional Java 8+ reflection classes that might be needed
-keep class java.lang.Class** { *; }
-keep class java.lang.Package** { *; }
-keep class java.lang.annotation.** { *; }
