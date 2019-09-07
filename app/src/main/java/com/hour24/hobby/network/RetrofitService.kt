package com.hour24.hobby.network

import com.hour24.hobby.BuildConfig
import com.hour24.hobby.const.APIHost
import com.hour24.hobby.network.service.IMindSwService
import com.hour24.hobby.network.service.ISeoulService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 서비스 생성
 */
object RetrofitService {

    // 서울시 공공데이터
    val seoulService: ISeoulService by lazy {
        createJSONService(ISeoulService::class.java, APIHost.seoul)
    }

    // mindSw
    val mindSwService: IMindSwService by lazy {
        createJSONService(IMindSwService::class.java, APIHost.mindsw)
    }

    /**
     * JSON 서비스 생성
     */
    private fun <T> createJSONService(classes: Class<T>, url: String): T {

        // 로깅
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.addInterceptor(httpLoggingInterceptor)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .client(okHttpClient.build())
            .build()

        return retrofit.create(classes)
    }

}
