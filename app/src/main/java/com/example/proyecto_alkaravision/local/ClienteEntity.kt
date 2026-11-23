package com.example.proyecto_alkaravision.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clientes",
    indices = [
        Index(value = ["identidad"], unique = true)
    ]
)
data class ClienteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val identidad: String,

    val telefono: String,

    val correo: String,

    val departamento: String,

    val municipio: String,

    val localidad: String,

    val direccionExacta: String
)