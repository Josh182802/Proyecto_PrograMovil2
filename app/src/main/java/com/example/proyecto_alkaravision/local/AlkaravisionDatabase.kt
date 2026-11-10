package com.example.proyecto_alkaravision.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UsuarioEntity::class,
        ProductoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AlkaravisionDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun productoDao(): ProductoDao

    companion object {

        @Volatile
        private var INSTANCE: AlkaravisionDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS productos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        codigo TEXT NOT NULL,
                        nombre TEXT NOT NULL,
                        categoria TEXT NOT NULL,
                        marca TEXT NOT NULL,
                        precio REAL NOT NULL,
                        cantidad INTEGER NOT NULL,
                        descripcion TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun obtenerBaseDatos(context: Context): AlkaravisionDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AlkaravisionDatabase::class.java,
                    "alkaravision_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}
