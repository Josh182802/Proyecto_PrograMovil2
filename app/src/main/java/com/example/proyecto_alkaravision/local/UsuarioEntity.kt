package com.example.proyecto_alkaravision.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuario: String,

    val passwordHash: String,

    val salt: String
)