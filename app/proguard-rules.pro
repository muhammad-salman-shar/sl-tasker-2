# Add project specific ProGuard rules here (SL Tasker).

# --- Moshi (reflection-free via codegen, but keep adapters safe) -------------
-keepclassmembers class com.sltasker.app.data.remote.dto.** {
    <fields>;
    *** get*();
    void set*(***);
}
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
