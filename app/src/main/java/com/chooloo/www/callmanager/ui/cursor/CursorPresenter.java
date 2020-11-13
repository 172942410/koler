package com.chooloo.www.callmanager.ui.cursor;

import android.database.Cursor;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.PermissionUtils;

public class CursorPresenter<V extends CursorMvpView> extends BasePresenter<V> implements CursorMvpPresenter<V> {
    @Override
    public void onRequestPermissionsResult(String[] permissions) {
        mMvpView.load();
    }

    @Override
    public void onRefresh() {
        mMvpView.load();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMvpView.setData(data);
        mMvpView.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMvpView.setData(null);
    }

    @Override
    public void onEnablePermissionClick() {
        mMvpView.askForPermissions();
    }
}
