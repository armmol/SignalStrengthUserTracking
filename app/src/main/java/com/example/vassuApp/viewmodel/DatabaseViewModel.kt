package com.example.vassuApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vassuApp.model.VartotojaiMAC
import com.example.vassuApp.repository.ConnectionUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DeferredResultUnused")
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val connectionUtil: ConnectionUtil
) : ViewModel() {
    val vartotojaiMACList = connectionUtil.vartotojaiMACList
    val matavimai = connectionUtil.matavimaiList
    val stiprumai = connectionUtil.stiprumaiList
    val columnCount = connectionUtil.columnCount
    val rowCount = connectionUtil.rowCount
    val userLocation = connectionUtil.userLocation

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            async { connectionUtil.loadStiprumai() }
            async { connectionUtil.loadVartotojai() }
            async { connectionUtil.loadMatavimai() }
        }
    }

    fun getGridLayout() {
        viewModelScope.launch(Dispatchers.IO) {
            connectionUtil.getGridLayout()
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            connectionUtil.load()
        }
    }

    fun addVartotojaiMACManually(mac: String, s1: Int, s2: Int, s3: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            connectionUtil.addVartotojaiMACManually(mac = mac, s1 = s1, s2 = s2, s3 = s3)
        }
    }

    fun clearDataFromRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            connectionUtil.clearDataFromRoom()
        }
    }

    fun getLocation(user: VartotojaiMAC) {
        viewModelScope.launch (Dispatchers.IO) {
            connectionUtil.getLocation(user)
        }
    }
}