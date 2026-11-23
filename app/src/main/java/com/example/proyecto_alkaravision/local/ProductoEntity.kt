package com.example.proyecto_alkaravision.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos",
    indices = [
        Index(value = ["codigo"], unique = true)
    ]
)
data class ProductoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val codigo: String,

    val nombre: String,
    val categoria: String,
    val marca: String,
    val precio: Double,
    val cantidad: Int,
    val descripcion: String,
    val foto: String? = null
)
