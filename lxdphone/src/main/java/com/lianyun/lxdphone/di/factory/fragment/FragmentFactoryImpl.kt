package com.lianyun.lxdphone.di.factory.fragment

import com.lianyun.lxdphone.ui.settings.SettingsFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FragmentFactoryImpl @Inject constructor() : FragmentFactory {
    override fun getSettingsFragment() = SettingsFragment()
}