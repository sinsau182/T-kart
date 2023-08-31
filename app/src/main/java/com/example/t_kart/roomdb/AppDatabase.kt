package com.example.t_kart.roomdb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database as AndroidxRoomDatabase

@AndroidxRoomDatabase(entities = [ProductModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        private var database : AppDatabase? = null
        private val DATABASE_NAME = "tkart"

        @Synchronized
        fun getInstance(context : Context): AppDatabase{
            if(database == null){
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database!!
        }
    }



    abstract fun productDao() : ProductDao
}