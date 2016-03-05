# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\dev\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Use unique member names to make stack trace reading easier
-useuniqueclassmembernames

# rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

#kotlin
-dontwarn kotlin.dom.**

-keepattributes Signature

#eventbus
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe *;
}
-keep class org.greenrobot.** {*;}

-dontwarn rx.lang.kotlin.**
