package com.yidian.upgrade.utils

import android.util.Log

/**
 * Log工具
 *
 * @author hyj
 */
class LogUtil private constructor() {
    private var isDebug = true
    fun setDebug(debug: Boolean) {
        isDebug = debug
    }

    fun v(tag: String?, msg: String) {
        if (isDebug) Log.v(tag, "----->$msg")
    }

    fun v(tag: String?, msg: String?, t: Throwable?) {
        if (isDebug) Log.v(tag, msg, t)
    }

    fun d(tag: String?, msg: String) {
        if (isDebug) Log.d(tag, "----->$msg")
    }

    fun d(tag: String?, msg: String?, t: Throwable?) {
        if (isDebug) Log.d(tag, msg, t)
    }

    fun i(tag: String?, msg: String) {
        if (isDebug) Log.i(tag, "----->$msg")
    }

    fun i(tag: String?, msg: String?, t: Throwable?) {
        if (isDebug) Log.i(tag, msg, t)
    }

    fun w(tag: String?, msg: String?) {
        if (isDebug) msg?.let { Log.w(tag, it) }
    }

    fun w(tag: String?, msg: String?, t: Throwable?) {
        if (isDebug) Log.w(tag, msg, t)
    }

    fun e(tag: String?, msg: String) {
        if (isDebug) Log.e(tag, "----->$msg")
    }

    fun e(tag: String?, msg: String?, t: Throwable?) {
        if (isDebug) Log.e(tag, msg, t)
    }

    fun print(`object`: Any?) {
        if (isDebug) {
            if (`object` != null) {
                println("----->$`object`")
            }
        }
    }

    companion object {
        @JvmStatic
        val INSTANCE: LogUtil by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LogUtil() }
    }


}

val UPLog = LogUtil.INSTANCE