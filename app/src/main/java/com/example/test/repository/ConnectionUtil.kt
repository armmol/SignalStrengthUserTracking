package com.example.test.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test.apiService.ApiService
import com.example.test.model.Matavimas
import com.example.test.model.Stiprumai
import com.example.test.model.Vartotojai
import com.example.test.model.VartotojaiMAC
import com.example.test.room.AppDao
import javax.inject.Inject
import kotlin.math.pow

class ConnectionUtil @Inject constructor(
    private val apiService: ApiService,
    private val appDao: AppDao
) {
    companion object {
        private const val TAG = "Connection Util"
    }

    private val _columnCount = MutableLiveData<Int?>(null)
    val columnCount: LiveData<Int?> = _columnCount

    private val _rowCount = MutableLiveData<Int?>(null)
    val rowCount: LiveData<Int?> = _rowCount

    private val _vartotojaiMACList = MutableLiveData<List<VartotojaiMAC>?>(null)
    val vartotojaiMACList: LiveData<List<VartotojaiMAC>?> = _vartotojaiMACList

    private val _matavimaiList = MutableLiveData<List<Matavimas>?>(null)
    val matavimaiList: LiveData<List<Matavimas>?> = _matavimaiList

    private val _stiprumaiList = MutableLiveData<List<Stiprumai>?>(null)
    val stiprumaiList: LiveData<List<Stiprumai>?> = _stiprumaiList


    private val _userLocation = MutableLiveData<Pair<Int, Int>?>(null)
    val userLocation: LiveData<Pair<Int, Int>?> = _userLocation

    private suspend fun saveVartotojaiToRoom(list: List<Vartotojai>?) {
        list?.forEach {
            appDao.addVartotojaiToRoom(it)
        }
    }


    private suspend fun saveStiprumaiToRoom(list: List<Stiprumai>?) {
        list?.forEach {
            appDao.addStiprumaiToRoom(it)
        }
    }


    private suspend fun saveMatavimasToRoom(list: List<Matavimas>?) {
        list?.forEach {
            appDao.addMatavimasToRoom(it)
        }
    }

    private fun groupVartotojaiByMac(vartotojaiList: List<Vartotojai>?): List<VartotojaiMAC>? {
        return vartotojaiList?.groupBy { it.mac }
            ?.map { entry ->
                val mac = entry.key
                val s1 = entry.value.find { it.sensorius == "wiliboxas1" }?.stiprumas ?: 0
                val s2 = entry.value.find { it.sensorius == "wiliboxas2" }?.stiprumas ?: 0
                val s3 = entry.value.find { it.sensorius == "wiliboxas3" }?.stiprumas ?: 0
                VartotojaiMAC(macAddress = mac, s1 = s1, s2 = s2, s3 = s3)
            }
    }

    suspend fun loadMatavimai() {
        try {
            val list = appDao.getMatavimas()
            if (list.isNotEmpty()) {
                _matavimaiList.postValue(normalizeMatavimaiList(list))
            } else {
                val apiList = apiService.getMatavimai().body()
                if (!apiList.isNullOrEmpty()) {
                    saveMatavimasToRoom(normalizeMatavimaiList(apiList))
                    _matavimaiList.postValue(normalizeMatavimaiList(apiList))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught $e")
        }
    }

    suspend fun loadStiprumai() {
        try {
            val list = appDao.getStiprumai()
            if (list.isNotEmpty()) {
                _stiprumaiList.postValue(list)
            } else {
                val apiList = apiService.getStiprumai().body()
                if (!apiList.isNullOrEmpty()) {
                    saveStiprumaiToRoom(apiList)
                    _stiprumaiList.postValue(apiList)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught $e")
        }
    }

    suspend fun loadVartotojai() {
        try {
            val list = appDao.getVartotojai()
            if (list.isNotEmpty()) {
                _vartotojaiMACList.postValue(groupVartotojaiByMac(list))
            } else {
                val apiList = apiService.getVartotojai().body()
                if (!apiList.isNullOrEmpty()) {
                    saveVartotojaiToRoom(apiList)
                    _vartotojaiMACList.postValue(groupVartotojaiByMac(apiList))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught $e")
        }
    }

    suspend fun load() {
        try {
            _vartotojaiMACList.postValue(groupVartotojaiByMac(appDao.getVartotojai()))
            _stiprumaiList.postValue(appDao.getStiprumai())
            _matavimaiList.postValue(appDao.getMatavimas())
        } catch (e: Exception) {
            Log.e(TAG, "Error in load $e")
        }
    }

    suspend fun getGridLayout() {
        try {
            val list = appDao.getMatavimas()
            val maxX = list.maxByOrNull { it.x }?.x ?: Int.MIN_VALUE
            val maxY = list.maxByOrNull { it.y }?.y ?: Int.MIN_VALUE
            val minX = list.minByOrNull { it.x }?.x ?: Int.MAX_VALUE
            val minY = list.minByOrNull { it.y }?.y ?: Int.MAX_VALUE
            _columnCount.postValue(maxX - minX + 1)
            _rowCount.postValue(maxY - minY + 1)
        } catch (e: Exception) {
            Log.e(TAG, "Error in row count $e")
        }
    }

    private fun normalizeMatavimaiList(matavimaiList: List<Matavimas>): List<Matavimas> {
        val minX = matavimaiList.minOfOrNull { it.x } ?: 0
        val minY = matavimaiList.minOfOrNull { it.y } ?: 0
        val offsetX = if (minX < 0) -minX else 0
        val offsetY = if (minY < 0) -minY else 0

        return matavimaiList.map { matavimas ->
            Matavimas(
                id = matavimas.id,
                matavimas = matavimas.matavimas,
                x = matavimas.x + offsetX,
                y = matavimas.y + offsetY,
                atstumas = matavimas.atstumas
            )
        }
    }


    suspend fun addVartotojaiMACManually(mac: String, s1: Int, s2: Int, s3: Int) {
        val maxId = appDao.getVartotojai().maxByOrNull { it.id }?.id ?: 9
        appDao.addVartotojaiToRoom(
            Vartotojai(
                id = maxId + 1,
                mac = mac,
                sensorius = "wiliboxas1",
                stiprumas = s1
            )
        )
        appDao.addVartotojaiToRoom(
            Vartotojai(
                id = maxId + 2,
                mac = mac,
                sensorius = "wiliboxas2",
                stiprumas = s2
            )
        )
        appDao.addVartotojaiToRoom(
            Vartotojai(
                id = maxId + 3,
                mac = mac,
                sensorius = "wiliboxas3",
                stiprumas = s3
            )
        )
        load()
    }

    suspend fun clearDataFromRoom() {
        appDao.deleteAllMatavimaiFromRoom()
        appDao.deleteAllStiprumaiFromRoom()
        appDao.deleteAllVartotojaiFromRoom()
        load()
    }

    private fun createGrid(matavimai: List<Matavimas>): Map<Pair<Int, Int>, Matavimas> {
        return matavimai.associateBy { Pair(it.x, it.y) }
    }

    private fun aggregateSignalStrengths(
        matavimai: List<Matavimas>,
        stiprumai: List<Stiprumai>
    ): Map<Pair<Int, Int>, Matavimas> {
        val grid = matavimai.associateBy { Pair(it.x, it.y) }.toMutableMap()
        val strengthCounts = mutableMapOf<String, MutableMap<String, Int>>() // To count the number of readings
        stiprumai.groupBy { it.matavimas }.forEach { (matavimasId, strengths) ->
            val correspondingMatavimas =
                matavimai.firstOrNull { it.matavimas == matavimasId.toString() }
            correspondingMatavimas?.let { matavimas ->
                val key = Pair(matavimas.x, matavimas.y)
                val cell = grid[key] ?: Matavimas(
                    id = Int.MAX_VALUE,
                    x = matavimas.x,
                    y = matavimas.y,
                    atstumas = matavimas.atstumas,
                    matavimas = matavimas.matavimas
                )
                strengths.forEach { strength ->
                    val currentStrengthTotal = cell.aggregatedStrengths.getOrDefault(strength.sensorius, 0)
                    val count = strengthCounts.getOrPut(strength.sensorius) { mutableMapOf() }
                    val currentCount = count.getOrDefault(matavimasId.toString(), 0)
                    cell.aggregatedStrengths[strength.sensorius] = currentStrengthTotal + strength.stiprumas
                    count[matavimasId.toString()] = currentCount + 1
                }
                grid[key] = cell
            }
        }
        return grid
    }


    private fun findNearestGridCell(
        user: VartotojaiMAC,
        grid: Map<Pair<Int, Int>, Matavimas>
    ): Matavimas? {
        return grid.values.minByOrNull { matavimas ->
            euclideanDistance(user, matavimas)
        }
    }

    suspend fun getLocation(user: VartotojaiMAC) {
        _userLocation.postValue(null)
        val matavimai = appDao.getMatavimas()
        val stiprumai = appDao.getStiprumai()
        val grid = createGrid(matavimai)
        aggregateSignalStrengths(matavimai, stiprumai)
        val nearestMatavimas = findNearestGridCell(user, grid)
        if (nearestMatavimas != null && grid.containsKey(
                Pair(nearestMatavimas.x, nearestMatavimas.y)
            )
        ) {
            println("User ${user.macAddress} is likely at grid location: X=${nearestMatavimas.x}, Y=${nearestMatavimas.y}")
            _userLocation.postValue(Pair(nearestMatavimas.x, nearestMatavimas.y))
        } else {
            println("User ${user.macAddress} is outside the grid bounds or no nearest location found.")
        }
    }

    private fun euclideanDistance(user: VartotojaiMAC, matavimas: Matavimas): Double {
        // This logic should be adjusted based on how you want to compare the signal strengths
        val s1Difference =
            (user.s1 - (matavimas.aggregatedStrengths["wiliboxas1"] ?: 0)).toDouble().pow(2)
        val s2Difference =
            (user.s2 - (matavimas.aggregatedStrengths["wiliboxas2"] ?: 0)).toDouble().pow(2)
        val s3Difference =
            (user.s3 - (matavimas.aggregatedStrengths["wiliboxas3"] ?: 0)).toDouble().pow(2)
        return kotlin.math.sqrt(s1Difference + s2Difference + s3Difference)
    }
}
