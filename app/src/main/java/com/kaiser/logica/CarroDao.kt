package com.kaiser.logica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CarroDao
{
    @Insert
    fun saveCarro(carro: CarroEntity)

    @Query(value = "Select * from CarroEntity")
    fun getAllCarro() : List<CarroEntity>

        @Query("DELETE FROM CarroEntity")
        fun nukeTable()

}
