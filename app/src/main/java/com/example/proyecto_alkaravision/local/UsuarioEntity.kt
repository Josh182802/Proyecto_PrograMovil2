package com.example.proyecto_alkaravision.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["usuario"], unique = true),
        Index(value = ["correo"])
    ]
)
data class UsuarioEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val usuario: String,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val correo: String
)
