# Add project specific ProGuard rules here.

# Keep Room entities
-keep class de.sysitprep.app.data.model.** { *; }

# Keep Gson serialized models
-keepclassmembers class de.sysitprep.app.data.model.** { *; }
-keep class de.sysitprep.app.data.repository.QuestionJson { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Navigation Safe Args
-keep class de.sysitprep.app.ui.** { *; }

-dontwarn java.lang.invoke.StringConcatFactory
