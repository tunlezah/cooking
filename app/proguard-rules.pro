# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.potpilot.cookbook.data.** {
    *** Companion;
}
-keepclasseswithmembers class com.potpilot.cookbook.data.** {
    kotlinx.serialization.KSerializer serializer(...);
}
