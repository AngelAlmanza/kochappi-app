package com.almanza.kochappi.di

import com.almanza.kochappi.data.repository.AuthRepositoryImpl
import com.almanza.kochappi.data.repository.CustomerRepositoryImpl
import com.almanza.kochappi.data.repository.ExerciseRepositoryImpl
import com.almanza.kochappi.data.repository.TemplateRepositoryImpl
import com.almanza.kochappi.data.repository.UserRepositoryImpl
import com.almanza.kochappi.domain.repository.AuthRepository
import com.almanza.kochappi.domain.repository.CustomerRepository
import com.almanza.kochappi.domain.repository.ExerciseRepository
import com.almanza.kochappi.domain.repository.TemplateRepository
import com.almanza.kochappi.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(impl: CustomerRepositoryImpl): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindTemplateRepository(impl: TemplateRepositoryImpl): TemplateRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
