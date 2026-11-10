package com.example.proyecto_alkaravision.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val identidad: String,

    val telefono: String,

    val correo: String,

    val direccion: String
)
