package com.example.weather

import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.hilt.internal.definecomponent.DefineComponentClasses
import dagger.hilt.migration.DisableInstallInCheck

//@DisableInstallInCheck
@InstallIn(SingletonComponent::class)
//@DefineComponent(parent = MyComponent::class)
@Module
class ViewModelModule {

//    @Provides
//    fun getViewModel()=ViewModel().also { DaggerMyComponent.create().inject(it) }
//    @Provides
//    fun provideNetworkManager() = NetworkManager()
}