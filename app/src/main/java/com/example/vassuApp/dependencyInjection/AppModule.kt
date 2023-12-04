package com.example.vassuApp.dependencyInjection

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import com.example.vassuApp.apiService.ApiService
import com.example.vassuApp.repository.ConnectionUtil
import com.example.vassuApp.room.AppDao
import com.example.vassuApp.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val databaseName = "room_database"

    @Provides
    @Singleton
    fun forConnectionUtil(
        apisService: ApiService,
        appDao: AppDao
    ): ConnectionUtil {
        return ConnectionUtil(
            apisService,
            appDao
        )
    }

    @Provides
    @Singleton
    fun forApiHandler(): ApiService {
        return Retrofit.Builder().baseUrl("http://192.168.1.184/VassuServer/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun forPortalDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun forPortalDao(portalDatabase: AppDatabase): AppDao {
        SQLiteDatabase.deleteDatabase(File(databaseName))
        return portalDatabase.getPortalDao()
    }
}