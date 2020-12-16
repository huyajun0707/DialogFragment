package com.yidian.upgrade.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Process
import android.text.TextUtils
import com.yidian.upgrade.Constants
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * App工具
 *
 * @author hyj
 */
class ApplicationUtil private constructor() {
    fun getPackageName(ctx: Context): String {
        return ctx.packageName
    }

    fun getPackageInfo(ctx: Context): PackageInfo? {
        var info: PackageInfo? = null
        try {
            info = ctx.packageManager.getPackageInfo(getPackageName(ctx), 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return info
    }

    fun getVersionName(ctx: Context): String {
        return getPackageInfo(ctx)!!.versionName
    }

    fun getMetaData(ctx: Context, key: String?): Any? {
        return try {
            ctx.packageManager.getApplicationInfo(
                getPackageName(ctx),
                PackageManager.GET_META_DATA
            ).metaData[key]
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getVersionCode(ctx: Context): Int {
        return getPackageInfo(ctx)!!.versionCode
    }

    fun getInstallStatus(ctx: Context, packageName: String?): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }
        for (info in ctx.packageManager
            .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)) {
            if (info.packageName.equals(packageName, ignoreCase = true)) return true
        }
        return false
    }

    fun isPerceptible(ctx: Context): Boolean {
        val manager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        if (infos != null && infos.size > 0) {
            for (info in infos) {
                // LogUtil.print("info:" + info.processName + "-" +
                // info.importance);
//                if (info.processName.equals(ctx.getPackageName())
//                        && info.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
//                    return true;
//                }
                if (info.processName == ctx.packageName) {
                    if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE || info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun getProcessName(ctx: Context): String? {
        val manager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in manager.runningAppProcesses) {
            if (appProcess.pid == Process.myPid()) {
                return appProcess.processName
            }
        }
        return null
    }

    fun isProcessRunning(context: Context, processName: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = am.runningAppProcesses
        for (info in infos) {
            if (info.processName == processName) {
                return true
            }
        }
        return false
    }

    fun isServiceRunning(ctx: Context, className: String): Boolean {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = activityManager.getRunningServices(Int.MAX_VALUE)
        if (infos.size != 0) {
            for (i in infos.indices) {
                if (infos[i].service.className == className) {
                    return true
                }
            }
        }
        return false
    }

    fun chmod(permission: String?, path: String?): Boolean {
        try {
            val command = StringBuilder(Constants.CHMOD).append(permission).append(Constants.SPACE)
                .append(path).toString()
            val runtime = Runtime.getRuntime()
            val pr = runtime.exec(command)
            pr.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun getSha1(ctx: Context): String? {
        try {
            val info =
                ctx.packageManager.getPackageInfo(ctx.packageName, PackageManager.GET_SIGNATURES)
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance("SHA1")
            val publicKey = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(
                    0xFF and publicKey[i]
                        .toInt()
                )
                    .toUpperCase(Locale.US)
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            return hexString.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        @JvmStatic
        val INSTANCE: ApplicationUtil by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApplicationUtil() }
    }

}