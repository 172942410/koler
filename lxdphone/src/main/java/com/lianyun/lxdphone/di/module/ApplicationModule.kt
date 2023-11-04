package com.lianyun.lxdphone.di.module

import com.lianyun.lxdphone.di.factory.fragment.FragmentFactory
import com.lianyun.lxdphone.di.factory.fragment.FragmentFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [ApplicationModule.BindsModule::class])
class ApplicationModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun bindFragmentFactory(fragmentFactoryImpl: FragmentFactoryImpl): FragmentFactory
    }
}