package com.example.test.dependencyInjection

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import com.example.test.apiService.ApiService
import com.example.test.repository.ConnectionUtil
import com.example.test.room.AppDao
import com.example.test.room.AppDatabase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.sql.Connection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val databaseName = "room_database"

    @Provides
    @Singleton
    fun providesConnectionUtil(
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
    fun providesApiHandler(): ApiService {
        return Retrofit.Builder().baseUrl("http://192.168.1.184/TestDBConn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesPortalDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesPortalDao(portalDatabase: AppDatabase): AppDao {
        SQLiteDatabase.deleteDatabase(File(databaseName))
        return portalDatabase.getPortalDao()
    }
}