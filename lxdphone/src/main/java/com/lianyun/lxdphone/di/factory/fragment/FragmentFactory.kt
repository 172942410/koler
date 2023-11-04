package com.lianyun.lxdphone.di.factory.fragment

import com.lianyun.lxdphone.ui.settings.SettingsFragment

interface FragmentFactory {
    fun getSettingsFragment(): SettingsFragment
}