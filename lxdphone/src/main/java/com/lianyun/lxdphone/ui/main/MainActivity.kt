package com.lianyun.lxdphone.ui.main

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.lianyun.lxdphone.R
import com.lianyun.lxdphone.databinding.MainBinding
import com.lianyun.lxdphone.di.factory.fragment.FragmentFactory
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.interactor.screen.ScreensInteractor
import com.perry.lib.ui.base.BaseActivity
import com.perry.lib.ui.contacts.ContactsViewState
import com.perry.lib.ui.dialer.DialerViewState
import com.perry.lib.ui.recents.RecentsViewState
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject
import com.perry.lib.di.factory.fragment.FragmentFactory as ChoolooFragmentFactory

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewState>()
, EasyPermissions.PermissionCallbacks
{
    override val contentView by lazy { binding.root }
    override val viewState: MainViewState by viewModels()

    private val _dialerViewState: DialerViewState by viewModels()
    private val recentsViewState: RecentsViewState by viewModels()
    private val contactsViewState: ContactsViewState by viewModels()
    private val binding by lazy { MainBinding.inflate(layoutInflater) }
    private val _fragments by lazy { listOf(_contactsFragment, _recentsFragment) }
    private val _recentsFragment by lazy { choolooFragmentFactory.getRecentsFragment() }
    private val _contactsFragment by lazy { choolooFragmentFactory.getContactsFragment() }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var screens: ScreensInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var choolooFragmentFactory: ChoolooFragmentFactory

    private val PERMISSION_REQ = 333
    override fun onSetup() {
        getPermissions()
        screens.disableKeyboard()

        binding.apply {
            mainTabs.viewPager = mainViewPager
            mainTabs.setHeadersResList(arrayOf(R.string.contacts, R.string.recents))

            mainMenuButton.setOnClickListener {
                viewState.onMenuClick()
            }

            mainDialpadButton.setOnClickListener {
                viewState.onDialpadFabClick()
            }

            mainSearchBar.setOnTextChangedListener { st ->
                contactsViewState.onFilterChanged(st)
                recentsViewState.onFilterChanged(st)
            }

            mainSearchBar.editText?.setOnFocusChangeListener { _, hasFocus ->
                viewState.onSearchFocusChange(hasFocus)
            }

            mainViewPager.adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount() = _fragments.size
                override fun createFragment(position: Int) = _fragments[position]
            }

            mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewState.onPageSelected(position)
                }
            })
        }

        viewState.apply {
            searchText.observe(this@MainActivity) {
                binding.mainSearchBar.text = it
            }

            searchHint.observe(this@MainActivity) {
                binding.mainSearchBar.hint = it.toString()
            }

            currentPageIndex.observe(this@MainActivity) {
                binding.mainViewPager.currentItem = it
            }

            isSearching.observe(this@MainActivity) {
                if (it) binding.root.transitionToState(R.id.constraint_set_main_collapsed)
            }

            showMenuEvent.observe(this@MainActivity) { ev ->
                ev.ifNew?.let { prompts.showFragment(fragmentFactory.getSettingsFragment()) }
            }

            showDialerEvent.observe(this@MainActivity) { ev ->
                ev.ifNew?.let {
                    prompts.showFragment(choolooFragmentFactory.getDialerFragment())
                    _dialerViewState.onTextChanged(it)
                }
            }
        }

        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            viewState.onViewIntent(intent)
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        screens.ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        Log.d("onPermissionsGranted","onPermissionsGranted:" + perms);
    }

    @TargetApi(23)
    private fun getPermissions() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // 已经拥有相关权限
            isManager() //修改这里！！！ 如果是安卓11以上 获取文件管理者
        } else {
            // 没有获取相关权限 去申请权限
//            val permissions = arrayOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
            if (!EasyPermissions.hasPermissions(this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )) {
                EasyPermissions.requestPermissions(
                    this, "应用程序需要RECORD_AUDIO权限才能访问麦克风和WRITE_EEXTERNAL_STORAGE保存录制的音频",
                    PERMISSION_REQ,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    //添加以下
    fun gtSdk30(): Boolean { //获取当前系统版本是否安卓11以上
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
//R = 安卓11，O = 8, M = 6

    //R = 安卓11，O = 8, M = 6
    private fun isManager() {
        if (gtSdk30()) { //判断当前手机系统版本
            if (Environment.isExternalStorageManager()) {
//        init();
            } else {
                getManager()
            }
        } else {
//      init();
        }
    }

    private fun getManager() {
        val alertDialog: AlertDialog //生成一个对话框 可跳转设置里手动开启权限
        val builder = AlertDialog.Builder(this) //嫌麻烦，样式可设为null
        builder.setPositiveButton("确认", null)
        builder.setTitle("权限弹框")
        builder.setMessage("权限获取")
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { v: View? ->
            alertDialog.dismiss() //去获取文件管理
            val intent =
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, 0x99)
        }
    }

    private fun openSettingsPage() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent,PERMISSION_REQ)
    }
    private fun showRationale() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Permissions Required")
            .setCancelable(false)
            .setMessage("应用程序需要RECORD_AUDIO权限才能访问麦克风和WRITE_EEXTERNAL_STORAGE保存录制的音频")
            .setPositiveButton("确认") { dialog, which ->
                openSettingsPage()
                dialog.dismiss()
            }
            .setNegativeButton(
                "取消"
            ) { dialog, which -> onBackPressed() }
            .show()
    }
    override fun onPermissionsDenied(requestCode: Int, perms: List<String?>?) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms!!)) {
            showRationale()
            return
        }
        finish()
    }
}