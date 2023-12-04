package vn.com.dsk.demo.base.health_index.ui

import kotlinx.coroutines.flow.StateFlow

interface HealthIndexContract {
//    fun showError(): Boolean

    fun getEnable(): StateFlow<Boolean>

    fun setEnable(status: Boolean)


}