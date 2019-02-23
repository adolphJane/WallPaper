package com.ixiye.project.coffeedevice.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ixiye.project.coffeedevice.data.bean.DrinkBean

@Dao
interface DrinkListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrinkList(drinkList: List<DrinkBean>)

    @Query("SELECT * FROM drink")
    fun loadDrinkList() : LiveData<List<DrinkBean>>
}