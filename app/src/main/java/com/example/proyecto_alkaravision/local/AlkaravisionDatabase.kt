package com.example.proyecto_alkaravision.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyecto_alkaravision.local.UsuarioDao
import com.example.proyecto_alkaravision.local.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AlkaravisionDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {

        @Volatile
        private var INSTANCE: AlkaravisionDatabase? = null

        fun obtenerBaseDatos(context: Context): AlkaravisionDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AlkaravisionDatabase::class.java,
                    "alkaravision_db"
                ).build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}