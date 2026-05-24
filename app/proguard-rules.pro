# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.ghost.legion.**$$serializer { *; }
-keepclassmembers class com.ghost.legion.** {
    *** Companion;
}
-keepclasseswithmembers class com.ghost.legion.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Google Generative AI
-keep class com.google.ai.client.generativeai.** { *; }
