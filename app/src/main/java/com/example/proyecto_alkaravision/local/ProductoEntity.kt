package com.example.proyecto_alkaravision.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val nombre: String,
    val categoria: String,
    val marca: String,
    val precio: Double,
    val cantidad: Int,
    val descripcion: String,
    val foto: String? = null
)
