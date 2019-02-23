package com.ixiye.project.coffeedevice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ixiye.project.coffeedevice.data.bean.DrinkBean

@Database(entities = [DrinkBean::class], version = 1)
abstract class CoffeeDatabase : RoomDatabase() {
    abstract fun drinkListDao() : DrinkListDao
}