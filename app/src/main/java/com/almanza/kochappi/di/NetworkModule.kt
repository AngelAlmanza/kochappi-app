package com.almanza.kochappi.di

import com.almanza.kochappi.data.remote.api.AuthApi
import com.almanza.kochappi.data.remote.api.CustomerApi
import com.almanza.kochappi.data.remote.api.ExerciseApi
import com.almanza.kochappi.data.remote.api.TemplateApi
import com.almanza.kochappi.data.remote.api.UserApi
import com.almanza.kochappi.data.remote.interceptor.AuthInterceptor
import com.almanza.kochappi.data.remote.interceptor.LoggingInterceptor
import com.almanza.kochappi.data.remote.interceptor.TokenAuthenticator
import com.almanza.kochappi.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: LoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)  // Logging PRIMERO (antes de auth)
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideExerciseApi(retrofit: Retrofit): ExerciseApi =
        retrofit.create(ExerciseApi::class.java)

    @Provides
    @Singleton
    fun provideCustomerApi(retrofit: Retrofit): CustomerApi =
        retrofit.create(CustomerApi::class.java)

    @Provides
    @Singleton
    fun provideTemplateApi(retrofit: Retrofit): TemplateApi =
        retrofit.create(TemplateApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)
}
