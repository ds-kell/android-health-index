package vn.com.dsk.demo.base.health_index.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HealthIndexViewModel: HealthIndexContract, ViewModel(){

    private val stateEnable = MutableStateFlow(false)
    override fun getEnable(): StateFlow<Boolean> = stateEnable

    override fun setEnable(status: Boolean) {
        stateEnable.value = status
    }
}