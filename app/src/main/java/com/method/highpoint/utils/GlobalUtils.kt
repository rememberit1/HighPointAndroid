package com.method.highpoint.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.method.highpoint.R


fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null)
            return true
    }
    return false
}

fun getUserGUID(activity: Activity): String? {
    val sharedPreference = activity.getPreferences(Context.MODE_PRIVATE)
    return sharedPreference?.getString("UserGUID", "")?.toString()
}

fun showLoginAlert(context: Context) {
    val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.HPDialogTheme)
        .setTitle(R.string.please_login)
        .setMessage(R.string.login_description)
        .setNegativeButton(R.string.close) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(R.color.hp_primary)
}

fun showConnectionAlert(context: Context) {
    val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.HPDialogTheme)
        .setTitle(R.string.no_network)
        .setMessage(R.string.no_network_description)
        .setNeutralButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(R.color.hp_primary)
}