package com.yidian.upgrade.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

class FilePathUtil private constructor() {
    private val TAG = "FilePathUtil"
    private val FILE_PROVIDER = "%s.provider"
    fun getDownloadFileUri(isProvider: Boolean, context: Context?, path: String?): Uri? {
        path?.let {
            val file = File(path)
            return if (isProvider) {
                val fileUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(context!!, FILE_PROVIDER, file)
                } else {
                    Uri.fromFile(file)
                }
                UPLog.i(TAG, fileUri.path!!)
                fileUri
            } else {
                Uri.fromFile(file).path?.let { UPLog.i(TAG, it) }
                Uri.fromFile(file)
            }
        }
        return null
    }

    fun forceMkdir(directory: File?) {
        directory?.let {
            if (directory.exists()) {
                UPLog.print("文件夹存在:" + directory.absolutePath)
                if (!directory.isDirectory) {
                    throw IOException("File $directory exists and is not a directory. Unable to create directory.")
                }
            } else {
                UPLog.print("文件夹不存在，创建:" + directory.absolutePath)
                if (!directory.mkdirs()) {
                    if (!directory.isDirectory) {
                        throw IOException("Unable to create directory $directory")
                    }
                }
            }
        }

    }

    fun forceMkdir(directoryPath: String?) {
        forceMkdir(File(directoryPath))
    }

    fun deleteFile(path: String?) {
        if (isSDCardExsist) {
            val folder = File(path)
            val files = folder.listFiles()
            for (file in files) {
                file.delete()
            }
        }
    }

    fun deleteFile(folder: File) {
        if (isSDCardExsist) {
            val files = folder.listFiles()
            for (file in files) {
                file.delete()
            }
        }
    }

    fun deleteFile(path: String?, fileName: String) {
        if (isSDCardExsist) {
            val folder = File(path)
            val files = folder.listFiles()
            for (file in files) {
                if (file.name.split("\\.".toRegex()).toTypedArray()[0] == fileName) {
                    file.delete()
                }
            }
        }
    }





    private val isSDCardExsist: Boolean
        private get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    private fun getExternalPath(ctx: Context, dirName: String): File {
        return File(
            Environment.getExternalStorageDirectory().absolutePath
                    + "/android/data/"
                    + ctx.packageName
                    + File.separator
                    + dirName + File.separator
        )
    }

    companion object {
        @JvmStatic
        val INSTANCE: FilePathUtil by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { FilePathUtil() }
    }
}