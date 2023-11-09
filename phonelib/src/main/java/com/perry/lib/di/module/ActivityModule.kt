package com.perry.lib.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.perry.lib.interactor.dialog.DialogsInteractor
import com.perry.lib.interactor.dialog.DialogsInteractorImpl
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.interactor.prompt.PromptsInteractorImpl
import com.perry.lib.interactor.screen.ScreensInteractor
import com.perry.lib.interactor.screen.ScreensInteractorImpl
import com.perry.lib.ui.base.BaseActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@InstallIn(ActivityComponent::class)
@Module(includes = [ActivityModule.BindsModule::class])
class ActivityModule {
    @Provides
    fun provideBaseActivity(@ActivityContext context: Context): BaseActivity<*> =
        context as BaseActivity<*>

    @Provides
    fun provideFragmentManager(@ActivityContext context: Context): FragmentManager =
        (context as AppCompatActivity).supportFragmentManager

    @Module
    @InstallIn(ActivityComponent::class)
    interface BindsModule {
        @Binds
        fun bindDialogsInteractor(dialogsInteractorImpl: DialogsInteractorImpl): DialogsInteractor

        @Binds
        fun bindScreensInteractor(screensInteractorImpl: ScreensInteractorImpl): ScreensInteractor

        @Binds
        fun bindPromptsInteractor(promptsInteractorImpl: PromptsInteractorImpl): PromptsInteractor
    }
}