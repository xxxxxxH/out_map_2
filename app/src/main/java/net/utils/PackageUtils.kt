package net.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.text.TextUtils

class PackageUtils {
    companion object {
        private var instance: PackageUtils? = null
            get() {
                field?.let {

                } ?: run {
                    instance = PackageUtils()
                }
                return field
            }

        @Synchronized
        fun get(): PackageUtils {
            return instance!!
        }
    }

    fun checkAppInstalled(context: Context, pkgName: String): Boolean {
        if (TextUtils.isEmpty(pkgName)) {
            return false
        }

        var packageInfo: PackageInfo? = null
        packageInfo = try {
            context.packageManager.getPackageInfo(pkgName, 0)
        } catch (e: Exception) {
            null
        }
        return packageInfo != null
    }
}