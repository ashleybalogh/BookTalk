# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep source file names, line numbers for stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Hilt
-keep,allowobfuscation,allowshrinking class dagger.hilt.android.** { *; }
-keep,allowobfuscation,allowshrinking @dagger.hilt.** class * { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# Keep Hilt Modules and Components
-keep class * extends dagger.Module { *; }
-keep class * extends dagger.hilt.InstallIn { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }

# Keep Mock implementations
-keep class com.booktalk.data.repository.mock.** { *; }
-keep class com.booktalk.di.Mock { *; }
-keep @com.booktalk.di.Mock class * { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep your data models
-keep class com.booktalk.domain.model.** { *; }
-keep class com.booktalk.data.local.entity.** { *; }
-keep class com.booktalk.data.remote.dto.** { *; }

# Auth-related classes
-keep class com.booktalk.data.remote.dto.auth.** { *; }
-keep class com.booktalk.domain.model.auth.** { *; }

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Auth0 JWT Library
-keep class com.auth0.android.** { *; }

# Keep Qualifier annotations
-keepattributes *Annotation*
-keepclassmembers @javax.inject.Qualifier class * { *; }
-keepclassmembers @dagger.Qualifier class * { *; }
-keepclassmembers @javax.inject.Scope class * { *; }
-keepclassmembers @dagger.Scope class * { *; }

# Keep injection methods
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <methods>;
}

# Keep BuildConfig for debug checks
-keep class com.booktalk.BuildConfig { *; }

# Security
-keep class androidx.security.crypto.** { *; }
-keep class com.google.crypto.tink.** { *; }
-dontwarn androidx.security.crypto.**

# Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.animation.** { *; }
-dontwarn androidx.compose.**

# Keep generic type information for Compose
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes *Annotation*
-keep class * extends androidx.compose.runtime.internal.ComposableCallable { *; }

# Debugging - remove these in production if size is an issue
-keepattributes LocalVariableTable,LocalVariableTypeTable
-keepattributes MethodParameters
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations