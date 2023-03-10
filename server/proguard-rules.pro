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

-keep class com.gyf.immersionbar.* {*;}
-dontwarn com.gyf.immersionbar.**

-keep class com.github.shadowsocks.** {*;}
-dontwarn com.github.shadowsocks.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, java.lang.Boolean);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 -keepattributes *Annotation*
 -keepclassmembers class * {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }

 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }

 #okhttp
  -dontwarn okhttp3.**
  -keep class okhttp3.**{*;}

  #okio
  -dontwarn okio.**
  -keep class okio.**{*;}

  #okgo
  -dontwarn com.lzy.okgo.**
  -keep class com.lzy.okgo.**{*;}

  #okrx
  -dontwarn com.lzy.okrx.**
  -keep class com.lzy.okrx.**{*;}

  #okrx2
  -dontwarn com.lzy.okrx2.**
  -keep class com.lzy.okrx2.**{*;}

  #okserver
  -dontwarn com.lzy.okserver.**
  -keep class com.lzy.okserver.**{*;}



  ##---------------Begin: proguard configuration for Gson  ----------
  # Gson uses generic type information stored in a class file when working with fields. Proguard
  # removes such information by default, so configure it to keep all of it.
  -keepattributes Signature

  # For using GSON @Expose annotation
  -keepattributes *Annotation*

  # Gson specific classes
  -dontwarn sun.misc.**
  #-keep class com.google.gson.stream.** { *; }

  # Application classes that will be serialized/deserialized over Gson
  # 此处需修改成自己项目中的 bean 类位置
#  -keep class game.riddles.server.bean.** { <fields>; }

  # Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
  # JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
  -keep class * extends com.google.gson.TypeAdapter
  -keep class * implements com.google.gson.TypeAdapterFactory
  -keep class * implements com.google.gson.JsonSerializer
  -keep class * implements com.google.gson.JsonDeserializer

  # Prevent R8 from leaving Data object members always null
  -keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }

  # Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
  -keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
  -keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

  ##---------------End: proguard configuration for Gson  ----------