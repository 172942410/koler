package com.perry.lib.interactor.permission

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.perry.lib.di.module.IoDispatcher
import com.perry.lib.ui.permissions.DefaultDialerRequestActivity
import com.perry.lib.ui.permissions.PermissionRequestActivity
import com.perry.lib.ui.permissions.PermissionRequestActivity.Companion.PERMISSIONS_ARGUMENT_KEY
import com.perry.lib.ui.permissions.PermissionRequestActivity.Companion.PermissionResult
import com.perry.lib.ui.permissions.PermissionRequestActivity.Companion.PermissionState.GRANTED
import com.perry.lib.ui.permissions.PermissionRequestActivity.Companion.REQUEST_CODE_ARGUMENT_KEY
import com.perry.lib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class PermissionsInteractorImpl @Inject constructor(
    private val telecomManager: TelecomManager,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    BaseObservable<PermissionsInteractor.Listener>(),
    PermissionsInteractor {

    override val isDefaultDialer: Boolean
        get() = context.packageName == telecomManager.defaultDialerPackage

    private val _onPermissionsResultListeners =
        ConcurrentHashMap<Int, (List<PermissionResult>) -> Unit>(1)

    private val _onDefaultDialerResultListeners = mutableListOf<(Boolean) -> Unit>()
    private var _requestCode = 255
        get() {
            _requestCode = field--
            return if (field < 0) 255 else field
        }

    override fun entryPermissionResult(
        responses: List<PermissionResult>,
        requestCode: Int
    ) {
        _onPermissionsResultListeners[requestCode]?.invoke(responses)
        _onPermissionsResultListeners.remove(requestCode)
    }


    override fun entryDefaultDialerResult(granted: Boolean) {
        _onDefaultDialerResultListeners.forEach { it.invoke(granted) }
        _onDefaultDialerResultListeners.clear()
    }

    override fun hasSelfPermission(permission: String) =
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    override fun hasSelfPermissions(permissions: Array<String>) =
        permissions.all { hasSelfPermission(it) }

    override fun checkDefaultDialer(callback: (Boolean) -> Unit) {
        if (isDefaultDialer) {
            callback.invoke(true)
        } else {
            _onDefaultDialerResultListeners.add(callback)
            val intent = Intent(context, DefaultDialerRequestActivity::class.java)
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun checkPermissions(
        vararg permissions: String,
        callback: (List<PermissionResult>) -> Unit
    ) {
        val intent = Intent(context, PermissionRequestActivity::class.java)
            .putExtra(PERMISSIONS_ARGUMENT_KEY, permissions)
            .putExtra(REQUEST_CODE_ARGUMENT_KEY, _requestCode)
            .addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        _onPermissionsResultListeners[_requestCode] = callback
    }

    override suspend fun checkPermissions(vararg permissions: String): Boolean =
        withContext(ioDispatcher) {
            suspendCancellableCoroutine { continuation ->
                checkPermissions(*permissions) { results ->
                    continuation.resume(results.all { it.state == GRANTED })
                }
            }
        }

    override suspend fun checkPermission(permission: String): Boolean = withContext(ioDispatcher) {
        suspendCancellableCoroutine { continuation ->
            checkPermissions(permission) { results ->
                continuation.resume(results.all { it.state == GRANTED })
            }
        }
    }

    override fun runWithPermissions(
        permissions: Array<String>,
        grantedCallback: () -> Unit,
        deniedCallback: (() -> Unit)?
    ) {
        if (permissions.all(this::hasSelfPermission)) {
            grantedCallback.invoke()
        } else {
            checkPermissions(*permissions) {
                if (it.all { a -> a.state == GRANTED }) {
                    grantedCallback.invoke()
                } else {
                    deniedCallback?.invoke()
                }
            }
        }
    }

    override fun runWithPermissions(vararg permissions: String, callback: (Boolean) -> Unit) {
        runWithPermissions(
            permissions as Array<String>,
            { callback.invoke(true) },
            { callback.invoke(false) }
        )
    }

    override fun runWithDefaultDialer(errorMessageRes: Int?, callback: () -> Unit) {
        checkDefaultDialer {
            if (it) {
                callback.invoke()
            } else {
                errorMessageRes?.let { it1 ->
                    Toast.makeText(
                        context,
                        it1, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun runWithDefaultDialer(
        errorMessageRes: Int?,
        grantedCallback: () -> Unit,
        notGrantedCallback: (() -> Unit)?
    ) {
        checkDefaultDialer {
            if (it) {
                grantedCallback.invoke()
            } else {
                errorMessageRes?.let { it1 ->
                    Toast.makeText(
                        context,
                        it1, Toast.LENGTH_SHORT
                    ).show()
                }
                notGrantedCallback?.invoke()
            }
        }
    }

    override fun runWithReadCallLogPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(READ_CALL_LOG),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithReadContactsPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(READ_CONTACTS),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteContactsPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(WRITE_CONTACTS),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteCallLogPermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(WRITE_CALL_LOG),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteCallPhonePermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(CALL_PHONE),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }

    override fun runWithWriteReadPhoneStatePermissions(callback: (Boolean) -> Unit) {
        runWithPermissions(
            arrayOf(READ_PHONE_STATE),
            grantedCallback = { callback.invoke(true) },
            deniedCallback = { callback.invoke(false) }
        )
    }
}