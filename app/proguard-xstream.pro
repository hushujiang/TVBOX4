# 保留XStream核心功能
-keep class com.thoughtworks.xstream.** { *; }
-keepattributes *Annotation*,EnclosingMethod

# 处理XML解析相关类
-keep class org.xmlpull.** { *; }
-keep class android.content.res.XmlResourceParser { *; }

# 保留XStream转换器
-keepclassmembers class * implements com.thoughtworks.xstream.converters.ConverterMatcher {
    public *;
}
