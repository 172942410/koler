package com.perry.lib.interactor.navigation

import com.perry.lib.interactor.base.BaseInteractor
import com.perry.lib.ui.base.BaseActivity

interface NavigationsInteractor : BaseInteractor<NavigationsInteractor.Listener> {
    interface Listener

    fun donate()
    fun rateApp()
    fun sendEmail()
    fun reportBug()
    fun goToAppGithub()
    fun manageBlockedNumber()
    fun goToLauncherActivity()
    fun sendSMS(number: String?)
    fun openWhatsapp(number: String?)
    fun addContact(number: String)
    fun viewContact(contactId: Long)
    fun editContact(contactId: Long)
    fun goToActivity(activityClass: Class<out BaseActivity<*>>)
}