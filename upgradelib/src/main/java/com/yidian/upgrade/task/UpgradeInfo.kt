package com.yidian.upgrade.task

import com.yidian.upgrade.Constants
import org.json.JSONObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @description: TODO 类描述
 * @author: huyajun
 * @date:  2020/12/14
 **/

class UpgradeInfo(
    var appUrl: String?,var appVersion: String?,
    var updateTime: String?, var description: String?, var isMandatory: Boolean
) {


//    var appUrl: String? = null
//    var appVersion: String? = null
//    var updateTime: String? = null
//    var description: String? = null
//    var isMandatory: Boolean = false

    companion object {
        fun parse(json: String): UpgradeInfo {
            var obj = JSONObject(json)
            var appUrl: String? = obj[Constants.ResponseKey.APP_URL] as String
            var appVersion: String? = obj[Constants.ResponseKey.APP_VERSION] as String
            var updateTime: String? = obj[Constants.ResponseKey.UPDATE_TIME] as String
            var description: String? = obj[Constants.ResponseKey.DESCRIPTION] as String
            var isMandatory: Boolean = obj[Constants.ResponseKey.IS_MANDATORY] as Boolean
            return UpgradeInfo(appUrl, appVersion, updateTime, description, isMandatory)
        }


    }


}

