package com.example.heroesandbandits.Utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MarvelRetrofit {

        private const val LOG = false

        private const val PUBLIC_KEY = "ed3da575ea342e591fc219b429c3c111"

        private const val PRIVATE_KEY = "9985cf0115724bc7aacb0cb7c41ba2a172b17111"

        private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    fun String.md5(): String {

        val MD5 = "MD5"

        try { // Create MD5 Hash

            val digest = MessageDigest

                .getInstance(MD5)

            digest.update(this.toByteArray())

            val messageDigest = digest.digest()

            // Create Hex String

            val hexString = StringBuilder()

            for (aMessageDigest in messageDigest) {

                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())

                while (h.length < 2) h = "0$h"

                hexString.append(h)

            }

            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {

            e.printStackTrace()

        }

        return ""

    }

        val marvelService: MarvelService = Retrofit.Builder()

            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

            .client(getOkHttpClient())

            .build()

            .create(MarvelService::class.java)


        private fun getOkHttpClient(): OkHttpClient {

            val logging = HttpLoggingInterceptor()

            logging.level = if (LOG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


            val builder = OkHttpClient.Builder()

                .addInterceptor{ chain ->

                    val original = chain.request()

                    val originalHttpUrl = original.url()

                    val timestamp = System.currentTimeMillis().toString()

                    val hash = (timestamp + PRIVATE_KEY + PUBLIC_KEY).md5()


                    val url = originalHttpUrl.newBuilder()

                        .addQueryParameter("apikey", PUBLIC_KEY)

                        .addQueryParameter("ts",timestamp)

                        .addQueryParameter("hash", hash)

                        .build()


                    val requestBuilder = original.newBuilder()

                        .url(url)


                    val request = requestBuilder.build()

                    chain.proceed(request)

                }

                .addInterceptor(logging)

            return builder.build()

        }

    }