package com.example.android.politicalpreparedness.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class CivicsHttpClient: OkHttpClient() {

    companion object {

        const val API_KEY = "AIzaSyDDqCD2-unyH0-yHLiGEkHSPxsbmgOeV5s" //Place your API Key Here

        fun getClient(): OkHttpClient {

//            val okHttpClient = Builder().addInterceptor(object: Interceptor {
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val original = chain.request()
//                    val url = original
//                        .url()
//                        .newBuilder()
//                        .addQueryParameter("key", API_KEY)
//                        .build()
//                    val request = original
//                        .newBuilder()
//                        .url(url)
//                        .build()
//                    return chain.proceed(request)
//                }
//            }).build()

            val okHttpClient = Builder().addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }.build()


            return okHttpClient
        }

    }

}