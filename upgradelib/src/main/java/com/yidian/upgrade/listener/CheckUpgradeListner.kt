package com.yidian.upgrade.listener

import com.yidian.upgrade.task.UpgradeInfo

/**
 * @description: TODO 类描述
 * @author: huyajun
 * @date:  2020/12/14
 **/
interface CheckUpgradeListner {
    fun onCheck(upgradeInfo: UpgradeInfo)
}