package com.kaiser.logica

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(entities = {Profile.class, Medicine.class}, version = 1
@Database (entities = [(CarroEntity::class)],version = 4)
abstract class AppDb : RoomDatabase() {

    abstract fun carroDao(): CarroDao
}