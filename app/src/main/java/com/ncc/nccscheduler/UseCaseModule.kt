package com.ncc.nccscheduler

import com.ncc.domain.repository.MainRepository
import com.ncc.domain.repository.SplashRepository
import com.ncc.domain.usecase.handover.GetHandoverUsecase
import com.ncc.domain.usecase.GetIdUsecase
import com.ncc.domain.usecase.GetIsLogin
import com.ncc.domain.usecase.GetUserInfoUsecase
import com.ncc.domain.usecase.routine.GetRoutineUsecase
import com.ncc.domain.usecase.LoginUsecase
import com.ncc.domain.usecase.SaveIdUsecase
import com.ncc.domain.usecase.UpdateUserInfoUsecase
import com.ncc.domain.usecase.handover.SetHandoverUsecase
import com.ncc.domain.usecase.routine.SetRoutineUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {


    @Provides
    @Singleton
    fun provideGetRoutineUsecase(repository: MainRepository) =
        GetRoutineUsecase(repository)

    @Provides
    @Singleton
    fun provideSetRoutineUsecase(repository: MainRepository) =
        SetRoutineUsecase(repository)

    @Provides
    @Singleton
    fun provideGetHandoverUsecase(repository: MainRepository) =
        GetHandoverUsecase(repository)

    @Provides
    @Singleton
    fun provideSetHandoverUsecase(repository: MainRepository) =
        SetHandoverUsecase(repository)

    @Provides
    @Singleton
    fun providerLoginUsecase(repository: SplashRepository) =
        LoginUsecase(repository)

//    @Provides
//    @Singleton
//    fun providerLogoutUsecase(repository: SplashRepository) =
//        LoginUsecase(repository)

    @Provides
    @Singleton
    fun providerIsLoginUsecase(repository: SplashRepository) =
        GetIsLogin(repository)

    @Provides
    @Singleton
    fun providerSaveIdUsecase(repository: SplashRepository) =
        SaveIdUsecase(repository)

    @Provides
    @Singleton
    fun providerGetIdUsecase(repository: SplashRepository) =
        GetIdUsecase(repository)

    @Provides
    @Singleton
    fun providerGetUserInfoUsecase(repository: MainRepository) =
        GetUserInfoUsecase(repository)


    @Provides
    @Singleton
    fun providerUpdateUserInfoUsecase(repository: MainRepository) =
        UpdateUserInfoUsecase(repository)
}