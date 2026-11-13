
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
        ProductoEntity::class,
        ClienteEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AlkaravisionDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun productoDao(): ProductoDao

    abstract fun clienteDao(): ClienteDao

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

        private val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS clientes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombre TEXT NOT NULL,
                        identidad TEXT NOT NULL,
                        telefono TEXT NOT NULL,
                        correo TEXT NOT NULL,
                        direccion TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    "ALTER TABLE productos ADD COLUMN foto TEXT"
                )
            }
        }


        private val MIGRATION_4_5 = object : Migration(4, 5) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
            CREATE TABLE clientes_nueva (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                identidad TEXT NOT NULL,
                telefono TEXT NOT NULL,
                correo TEXT NOT NULL,
                departamento TEXT NOT NULL,
                municipio TEXT NOT NULL,
                localidad TEXT NOT NULL,
                direccionExacta TEXT NOT NULL
            )
            """.trimIndent()
                )

                database.execSQL(
                    """
            INSERT INTO clientes_nueva (
                id,
                nombre,
                identidad,
                telefono,
                correo,
                departamento,
                municipio,
                localidad,
                direccionExacta
            )
            SELECT
                id,
                nombre,
                identidad,
                telefono,
                correo,
                '',
                '',
                '',
                direccion
            FROM clientes
            """.trimIndent()
                )

                database.execSQL("DROP TABLE clientes")

                database.execSQL(
                    "ALTER TABLE clientes_nueva RENAME TO clientes"
                )
            }
        }


        private val MIGRATION_5_6 = object : Migration(5, 6) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
            CREATE UNIQUE INDEX IF NOT EXISTS index_productos_codigo
            ON productos(codigo COLLATE NOCASE)
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE UNIQUE INDEX IF NOT EXISTS index_clientes_identidad
            ON clientes(identidad COLLATE NOCASE)
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE UNIQUE INDEX IF NOT EXISTS index_usuarios_usuario
            ON usuarios(usuario COLLATE NOCASE)
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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8
                    )
                    .build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}
