package com.perry.audiorecorder.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import com.perry.audiorecorder.R;
import com.perry.audiorecorder.audiorecording.RecordFragment;
import com.perry.audiorecorder.mvpbase.BaseActivity;
import java.util.List;
import javax.inject.Inject;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity
    implements EasyPermissions.PermissionCallbacks {

  private static final String LOG_TAG = MainActivity.class.getSimpleName();
  private static final int PERMISSION_REQ = 222;

  @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.main_container, RecordFragment.newInstance())
          .commit();
    }
    getPermissions();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Intent i = new Intent(this, SettingsActivity.class);
      startActivity(i);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  //添加以下
  public boolean gtSdk30() { //获取当前系统版本是否安卓11以上
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
  }
//R = 安卓11，O = 8, M = 6

  private void isManager() {
    if (gtSdk30()) {//判断当前手机系统版本
      if (Environment.isExternalStorageManager()) {
//        init();
      }
      else {
        getManager();
      }
    } else {
//      init();
    }
  }

  private void getManager() {
    AlertDialog alertDialog;//生成一个对话框 可跳转设置里手动开启权限
    AlertDialog.Builder builder = new AlertDialog.Builder(this);//嫌麻烦，样式可设为null
    builder.setPositiveButton("确认", null);
    builder.setTitle("权限弹框");
    builder.setMessage("权限获取");
    builder.setCancelable(false);
    alertDialog = builder.create();
    alertDialog.show();
    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
      alertDialog.dismiss();//去获取文件管理
      Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
      intent.setData(Uri.parse("package:" + getPackageName()));
      startActivityForResult(intent, 0x99);
    });
  }

  @TargetApi(23)
  private void getPermissions() {
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    if (EasyPermissions.hasPermissions(this, perms)) {
      // 已经拥有相关权限
      isManager();//修改这里！！！ 如果是安卓11以上 获取文件管理者
    } else {
      // 没有获取相关权限 去申请权限
      String[] permissions = new String[]{
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.RECORD_AUDIO,
              Manifest.permission.MANAGE_EXTERNAL_STORAGE,
              Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION,
      };
      if (!EasyPermissions.hasPermissions(MainActivity.this, permissions)) {
        EasyPermissions.requestPermissions(this, getString(R.string.permissions_required),
                PERMISSION_REQ, permissions);
      }
    }
  }

  @TargetApi(23)
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  private void showRationale() {
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Permissions Required")
        .setCancelable(false)
        .setMessage(
            getString(R.string.permissions_required))
        .setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> {
          openSettingsPage();
          dialog.dismiss();
        })
        .setNegativeButton(R.string.dialog_action_cancel,
            (dialog, which) -> onBackPressed())
        .show();
  }

  private void openSettingsPage() {
    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivityForResult(intent, PERMISSION_REQ);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    getPermissions();
  }

//  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
//    return dispatchingAndroidInjector;
//  }

  @Override public void onPermissionsGranted(int requestCode, List<String> perms) {
    //NO-OP
  }

  @Override public void onPermissionsDenied(int requestCode, List<String> perms) {
    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
      showRationale();
      return;
    }
    finish();
  }
}
