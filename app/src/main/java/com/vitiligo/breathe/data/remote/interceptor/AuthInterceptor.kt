package com.vitiligo.breathe.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tenantSecret = "sk_main_df31bfd012ae4252b1fe5eea9907e300f5a85ba43dfe4704a3803f13aa04ac8a"

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-Tenant-Secret", tenantSecret)
            .build()

        return chain.proceed(newRequest)
    }
}